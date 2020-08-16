package moe.lz233.meizugravity.controller.util;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.core.content.FileProvider;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;

import moe.lz233.meizugravity.controller.BuildConfig;

public class FileUtil {

    public static File createFile(String FilePath) {
        return new File(FilePath);
    }

    public static boolean isFile(String FilePath) {
        boolean isFile = false;
        try {
            File file = new File(FilePath);
            isFile = file.isFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isFile;
    }

    public static boolean isDirectory(String DirectoryPath) {
        File file = new File(DirectoryPath);
        return file.isDirectory();
    }

    public static boolean deleteFile(String FilePath) {
        if (isFile(FilePath)) {
            File file = new File(FilePath);
            return file.delete();
        } else {
            return false;
        }
    }

    public static boolean deleteDir(File dir) {
        Boolean isSucceed = true;
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    if (isSucceed) {
                        isSucceed = deleteDir(file);
                    } else {
                        deleteDir(file);
                    }
                } else {
                    if (isSucceed) {
                        isSucceed = file.delete();
                    } else {
                        file.delete();
                    }
                }
            }
            if (isSucceed) {
                isSucceed = dir.delete();
            } else {
                dir.delete();
            }

        }
        return isSucceed;
    }

    public static void copyFile(final String From, final String To, final Boolean move, Boolean isBlocking) {
        final Boolean[] isFinish = new Boolean[1];
        new Thread(() -> {
            try {
                String Directory = To.substring(0, To.lastIndexOf("/"));
                if (!isDirectory(Directory)) {
                    File file = new File(Directory);
                    file.mkdir();
                }
                InputStream in = new FileInputStream(From);
                OutputStream out = new FileOutputStream(To);
                byte[] buff = new byte[1024];
                int len;
                while ((len = in.read(buff)) != -1) {
                    out.write(buff, 0, len);
                }
                in.close();
                out.close();
                if (move) {
                    deleteFile(From);
                }
                isFinish[0] = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        if (isBlocking) {
            while ((isFinish[0] == null)) {

            }
        }
    }

    /**
     * 从assets目录中复制整个文件夹内容,考贝到 /data/data/包名/files/目录中
     *
     * @param activity activity 使用CopyFiles类的Activity
     * @param filePath String  文件路径,如：/assets/aa
     */
    public static void copyAssetsDir2Phone(Activity activity, String filePath) {
        try {
            String[] fileList = activity.getAssets().list(filePath);
            if (fileList.length > 0) {//如果是目录
                File file = new File(activity.getFilesDir().getAbsolutePath() + File.separator + filePath);
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileList) {
                    filePath = filePath + File.separator + fileName;

                    copyAssetsDir2Phone(activity, filePath);

                    filePath = filePath.substring(0, filePath.lastIndexOf(File.separator));
                    Log.e("oldPath", filePath);
                }
            } else {//如果是文件
                InputStream inputStream = activity.getAssets().open(filePath);
                File file = new File(activity.getFilesDir().getAbsolutePath() + File.separator + filePath);
                if (!file.exists() || file.length() == 0) {
                    FileOutputStream fos = new FileOutputStream(file);
                    int len = -1;
                    byte[] buffer = new byte[1024];
                    while ((len = inputStream.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    inputStream.close();
                    fos.close();
                } else {
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readTextFromFile(String FilePath) {
        try {
            if (isFile(FilePath)) {
                FileReader reader = new FileReader(FilePath);
                BufferedReader br = new BufferedReader(reader);
                StringBuffer stringBuffer = new StringBuffer();
                String temp;
                while ((temp = br.readLine()) != null) {
                    stringBuffer.append(temp + "\n");
                }
                return stringBuffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeTextToFile(String strcontent, String filePath, String fileName) {
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                //Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            //Log.e("TestFile", "Error on write File:" + e);
        }
    }

    public static Uri getUriFromFile(File file, Context context) {
        Uri imageUri = null;
        if (file != null && file.exists() && file.isFile()) {
            imageUri = FileProvider.getUriForFile(context, "com.lz233.onetext.fileprovider", file);
        }
        return imageUri;
    }

    public static String shellExec(String cmd) {
        Runtime mRuntime = Runtime.getRuntime();
        StringBuffer mRespBuff = new StringBuffer();
        try {
            //Process中封装了返回的结果和执行错误的结果
            Process mProcess = mRuntime.exec(cmd);
            BufferedReader mReader = new BufferedReader(new InputStreamReader(mProcess.getInputStream()));
            char[] buff = new char[1024];
            int ch = 0;
            while ((ch = mReader.read(buff)) != -1) {
                mRespBuff.append(buff, 0, ch);
            }
            mReader.close();
            System.out.print(mRespBuff.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mRespBuff.toString();
    }
}
