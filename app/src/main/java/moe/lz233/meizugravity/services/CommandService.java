package moe.lz233.meizugravity.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class CommandService extends Service {
    public CommandService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class TCPServer {
        private static final int port = 80;
        private long lastQRCodeRequestTime = 0;

        public void listen() throws Exception {
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
                        String flags = null;
                        System.out.println("----------------------------------------\n" + str + "\n----------------------------------------");
                        outputStream.close();
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        }
    }
}
