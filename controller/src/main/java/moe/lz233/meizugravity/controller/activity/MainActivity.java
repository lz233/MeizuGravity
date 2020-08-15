package moe.lz233.meizugravity.controller.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tananaev.adblib.AdbBase64;
import com.tananaev.adblib.AdbConnection;
import com.tananaev.adblib.AdbCrypto;
import com.tananaev.adblib.AdbStream;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import javax.xml.bind.DatatypeConverter;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;

import moe.lz233.meizugravity.controller.R;
import moe.lz233.meizugravity.controller.utils.GetUtil;

public class MainActivity extends BaseActivity {
    public static String TAG = "MeowWearDebug";
    //private Socket socket = null;
    //private AdbCrypto crypto;
    //private AdbConnection adb;
    private AdbStream stream;
    TextView tv;
    private FloatingActionButton backFloatingActionButton;
    private Handler mHandler = new Handler() {
        public void handleMessage(@NonNull Message msg) {
            Toast.makeText(MainActivity.this,(String)msg.obj,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        backFloatingActionButton = findViewById(R.id.backFloatingActionButton);
        //
        setSupportActionBar(toolbar);
        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = connectSocket("127.0.0.1", 5555);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (socket != null) {
                    try {
                        crypto = AdbCrypto.setupCrypto("pub.key", "priv.key", getApplicationContext());
                        adb = AdbConnection.create(socket, crypto);
                        adb.connect();
                        stream = adb.open("shell:");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new Thread(() -> {
                        while (!stream.isClosed()) {
                            try {
                                Message message = new Message();
                                message.obj = new String(stream.read(), StandardCharsets.US_ASCII);
                                mHandler.sendMessage(message);
                            } catch (InterruptedException | IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    try {
                        stream.write("input keyevent 4\n");
                        backFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                try {
                                    stream.write("input keyevent 4\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();*/
        //
        backFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetUtil().sendGet("http://192.168.43.194:2333/input%20keyevent%204", new GetUtil.GetCallback() {
                    @Override
                    public void onGetDone(String result) {

                    }
                });
            }
        });
    }

    public static Socket connectSocket(String ip, int port) throws IOException {
        // Connect the socket to the remote host
        return new Socket(ip, port);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
