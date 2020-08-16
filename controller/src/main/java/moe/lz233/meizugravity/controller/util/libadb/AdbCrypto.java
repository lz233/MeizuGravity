package moe.lz233.meizugravity.controller.util.libadb;

import android.content.Context;

import android.util.Base64;
import android.util.Log;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

/**
 * This class encapsulates the ADB cryptography functions and provides
 * an interface for the storage and retrieval of keys.
 *
 * @author Cameron Gutman
 */
public class AdbCrypto {

    /**
     * An RSA keypair encapsulated by the AdbCrypto object
     */
    private KeyPair keyPair;

    /**
     * The base 64 conversion interface to use
     */
    private AdbBase64 base64;

    /**
     * The ADB RSA key length in bits
     */
    private static final int KEY_LENGTH_BITS = 2048;

    /**
     * The ADB RSA key length in bytes
     */
    private static final int KEY_LENGTH_BYTES = KEY_LENGTH_BITS / 8;

    /**
     * The ADB RSA key length in words
     */
    private static final int KEY_LENGTH_WORDS = KEY_LENGTH_BYTES / 4;

    /**
     * The RSA signature padding as an int array
     */
    private static final int[] SIGNATURE_PADDING_AS_INT = new int[]{
            0x00, 0x01, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff,
            0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0xff, 0x00,
            0x30, 0x21, 0x30, 0x09, 0x06, 0x05, 0x2b, 0x0e, 0x03, 0x02, 0x1a, 0x05, 0x00,
            0x04, 0x14
    };

    /**
     * The RSA signature padding as a byte array
     */
    private static byte[] SIGNATURE_PADDING;

    static {
        SIGNATURE_PADDING = new byte[SIGNATURE_PADDING_AS_INT.length];

        for (int i = 0; i < SIGNATURE_PADDING.length; i++)
            SIGNATURE_PADDING[i] = (byte) SIGNATURE_PADDING_AS_INT[i];
    }

    /**
     * Converts a standard RSAPublicKey object to the special ADB format
     *
     * @param pubkey RSAPublicKey object to convert
     * @return Byte array containing the converted RSAPublicKey object
     */
    private static byte[] convertRsaPublicKeyToAdbFormat(RSAPublicKey pubkey) {
        /*
         * ADB literally just saves the RSAPublicKey struct to a file.
         *
         * typedef struct RSAPublicKey {
         * int len; // Length of n[] in number of uint32_t
         * uint32_t n0inv;  // -1 / n[0] mod 2^32
         * uint32_t n[RSANUMWORDS]; // modulus as little endian array
         * uint32_t rr[RSANUMWORDS]; // R^2 as little endian array
         * int exponent; // 3 or 65537
         * } RSAPublicKey;
         */

        /* ------ This part is a Java-ified version of RSA_to_RSAPublicKey from adb_host_auth.c ------ */
        BigInteger r32, r, rr, rem, n, n0inv;

        r32 = BigInteger.ZERO.setBit(32);
        n = pubkey.getModulus();
        r = BigInteger.ZERO.setBit(KEY_LENGTH_WORDS * 32);
        rr = r.modPow(BigInteger.valueOf(2), n);
        rem = n.remainder(r32);
        n0inv = rem.modInverse(r32);

        int[] myN = new int[KEY_LENGTH_WORDS];
        int[] myRr = new int[KEY_LENGTH_WORDS];
        BigInteger[] res;
        for (int i = 0; i < KEY_LENGTH_WORDS; i++) {
            res = rr.divideAndRemainder(r32);
            rr = res[0];
            rem = res[1];
            myRr[i] = rem.intValue();

            res = n.divideAndRemainder(r32);
            n = res[0];
            rem = res[1];
            myN[i] = rem.intValue();
        }

        /* ------------------------------------------------------------------------------------------- */

        ByteBuffer bbuf = ByteBuffer.allocate(524).order(ByteOrder.LITTLE_ENDIAN);


        bbuf.putInt(KEY_LENGTH_WORDS);
        bbuf.putInt(n0inv.negate().intValue());
        for (int i : myN)
            bbuf.putInt(i);
        for (int i : myRr)
            bbuf.putInt(i);

        bbuf.putInt(pubkey.getPublicExponent().intValue());
        return bbuf.array();
    }

    /**
     * Creates a new AdbCrypto object from a key pair loaded from files.
     *
     * @param base64     Implementation of base 64 conversion interface required by ADB
     * @param privateKey File containing the RSA private key
     * @param publicKey  File containing the RSA public key
     * @return New AdbCrypto object
     * @throws IOException              If the files cannot be read
     * @throws NoSuchAlgorithmException If an RSA key factory cannot be found
     * @throws InvalidKeySpecException  If a PKCS8 or X509 key spec cannot be found
     */
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static AdbCrypto loadAdbKeyPair(AdbBase64 base64, File privateKey, File publicKey) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        AdbCrypto crypto = new AdbCrypto();

        int privKeyLength = (int) privateKey.length();
        int pubKeyLength = (int) publicKey.length();
        byte[] privKeyBytes = new byte[privKeyLength];
        byte[] pubKeyBytes = new byte[pubKeyLength];

        FileInputStream privIn = new FileInputStream(privateKey);
        FileInputStream pubIn = new FileInputStream(publicKey);

        privIn.read(privKeyBytes);
        pubIn.read(pubKeyBytes);

        privIn.close();
        pubIn.close();

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(privKeyBytes);
        EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(pubKeyBytes);

        crypto.keyPair = new KeyPair(keyFactory.generatePublic(publicKeySpec),
                keyFactory.generatePrivate(privateKeySpec));
        crypto.base64 = base64;

        return crypto;
    }

    /**
     * Creates a new AdbCrypto object from a key pair loaded from files.
     *
     * @param base64  Implementation of base 64 conversion interface required by ADB
     * @param keyPair RSA key pair
     * @return New AdbCrypto object
     */
    public static AdbCrypto loadAdbKeyPair(AdbBase64 base64, KeyPair keyPair) {
        AdbCrypto crypto = new AdbCrypto();

        crypto.keyPair = keyPair;
        crypto.base64 = base64;

        return crypto;
    }

    /**
     * Creates a new AdbCrypto object by generating a new key pair.
     *
     * @param base64 Implementation of base 64 conversion interface required by ADB
     * @return A new AdbCrypto object
     * @throws NoSuchAlgorithmException If an RSA key factory cannot be found
     */
    private static AdbCrypto generateAdbKeyPair(AdbBase64 base64) throws NoSuchAlgorithmException {
        AdbCrypto crypto = new AdbCrypto();

        KeyPairGenerator rsaKeyPg = KeyPairGenerator.getInstance("RSA");
        rsaKeyPg.initialize(KEY_LENGTH_BITS);

        crypto.keyPair = rsaKeyPg.genKeyPair();
        crypto.base64 = base64;

        return crypto;
    }

    /**
     * Signs the ADB SHA1 payload with the private key of this object.
     *
     * @param payload SHA1 payload to sign
     * @return Signed SHA1 payload
     * @throws GeneralSecurityException If signing fails
     */
    byte[] signAdbTokenPayload(byte[] payload) throws GeneralSecurityException {
        Cipher c = Cipher.getInstance("RSA/ECB/NoPadding");

        c.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());

        c.update(SIGNATURE_PADDING);

        return c.doFinal(payload);
    }

    /**
     * Gets the RSA public key in ADB format.
     *
     * @return Byte array containing the RSA public key in ADB format.
     // * @throws IOException If the key cannot be retrived
     */
    byte[] getAdbPublicKeyPayload() {
        byte[] convertedKey = convertRsaPublicKeyToAdbFormat((RSAPublicKey) keyPair.getPublic());

        /* The key is base64 encoded with a user@host suffix and terminated with a NUL */

        String keyString = base64.encodeToString(convertedKey) +
                " unknown@unknown" +
                '\0';
        return keyString.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Saves the AdbCrypto's key pair to the specified files.
     *
     * @param privateKey The file to store the encoded private key
     * @param publicKey  The file to store the encoded public key
     * @throws IOException If the files cannot be written
     */
    private void saveAdbKeyPair(File privateKey, File publicKey) throws IOException {
        FileOutputStream privOut = new FileOutputStream(privateKey);
        FileOutputStream pubOut = new FileOutputStream(publicKey);

        privOut.write(keyPair.getPrivate().getEncoded());
        pubOut.write(keyPair.getPublic().getEncoded());

        privOut.close();
        pubOut.close();
    }

    /**
     * This function loads a keypair from the specified files if one exists, and if not,
     * it creates a new keypair and saves it in the specified files
     *
     * @param pubKeyFile The file to store the encoded private key
     * @param privKeyFile  The file to store the encoded public key
     * @param context  The file to store the encoded public key
     * @throws IOException If the files cannot be written or failed to read from file
     * @throws NoSuchAlgorithmException If RSA algorithm was unsupported with the crypto packages available
     * @throws InvalidKeySpecException If key spec was invalid
     */
    public static AdbCrypto setupCrypto(String pubKeyFile, String privKeyFile, Context context) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        String absolutePath = context.getFilesDir().getAbsolutePath();
        File pub = new File(absolutePath + "/" + pubKeyFile);
        File priv = new File(absolutePath + "/" + privKeyFile);
        AdbCrypto c = null;
        // Try to load a key pair from the files
        if (pub.exists() && priv.exists()) {
            c = AdbCrypto.loadAdbKeyPair(getBase64Impl(), priv, pub);
        }
        if (c == null) {
            // We couldn't load a key, so let's generate a new one
            c = AdbCrypto.generateAdbKeyPair(getBase64Impl());
            // Save it
            c.saveAdbKeyPair(priv, pub);
            //Log.d(MainActivity.TAG, "Generated new keypair: " + absolutePath + "/" + pub.getName() + "+" + priv.getName());
        } else {
            //Log.d(MainActivity.TAG, "Loaded existing keypair: " + absolutePath + "/" + pub.getName() + "+" + priv.getName());
        }
        return c;
    }

    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuilder sbf = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    /**
     * This implements the AdbBase64 interface required for AdbCrypto
     */
    public static AdbBase64 getBase64Impl() {
        return (data) -> Base64.encodeToString(data, Base64.DEFAULT);
    }
}
