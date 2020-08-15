package moe.lz233.meizugravity.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import okhttp3.Response;

public class CommandService extends Service {
    public CommandService() {
    }

    @Override
    public void onCreate() {
        try {
            Toast.makeText(this, "service", Toast.LENGTH_SHORT).show();
            new TCPServer().listen();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class TCPServer {
        private static final int port = 2333;

        public void listen() throws Exception {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try{
                        ServerSocket serverSocket = new ServerSocket(port);
                        while (true) {
                            Socket socket = serverSocket.accept();
                            new Thread(() -> {
                                try {
                                    String ip = socket.getInetAddress().getHostAddress();
                                    InputStream inputStream = socket.getInputStream();
                                    OutputStream outputStream = socket.getOutputStream();
                                    //读取请求数据
                                    byte[] bytes = new byte[inputStream.available()];
                                    inputStream.read(bytes);
                                    String str = new String(bytes);
                                    Log.d("CommandService",str);
                                    //Response response = new Response.Builder().
                                    String flags = str.substring(str.indexOf("GET") + 5, str.indexOf(" HTTP"));
                                    outputStream.write(addHeader(shellExec(flags.replace("%20"," "))).getBytes());
                                    Log.d("CommandService", "----------------------------------------\n" + str + "\n----------------------------------------");
                                    inputStream.close();
                                    outputStream.close();
                                    socket.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
    public String shellExec(String cmd) {
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
    private String addHeader(String string) {
        String header = "HTTP/1.1 200 OK\nContent-Type: text/html; charset=UTF-8\n\n";
        return header + string;
    }
}
