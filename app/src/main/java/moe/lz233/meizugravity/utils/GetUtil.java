package moe.lz233.meizugravity.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class GetUtil {
    public static final String USER_AGENT = "Dalvik/2.1.0 (Linux; U; Android 5.1.1; G011A Build/LMY48Z) (#Build; google; G011A; google-user 5.1.1 20171130.276299 release-keys; 5.1.1) +CoolMarket/10.4-2007081";
    private Context context;

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return 字符串
     */
    public static String streamToString(InputStream is) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = is.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.close();
            is.close();
            byte[] byteArray = out.toByteArray();
            return new String(byteArray);
        } catch (Exception e) {
            return null;
        }
    }

    public void sendGet(String getUrl, ArrayList<String[]> prop, GetCallback callback) {
        GetTask task = new GetTask(getUrl, prop, callback);
        task.executeOnExecutor(Executors.newCachedThreadPool());
    }

    /*public void translate(Context context, String targetLan, String content, TranslateCallback callback) {
        translate(context, LAN_AUTO, targetLan, content, callback);
    }*/
    public interface GetCallback {
        void onGetDone(String result);
    }

    /**
     * 使用异步任务来翻译，翻译完成后回调callback
     */
    class GetTask extends AsyncTask<Void, Void, String> {
        String getUrl;
        ArrayList<String[]> prop;
        GetCallback callback;
        //private LoadingDalog loadingDalog = null; // 这个请自己定义加载中对话框，或者干脆不使用

        GetTask(String getUrl, ArrayList<String[]> prop, GetCallback callback) {
            this.getUrl = getUrl;
            this.prop = prop;
            this.callback = callback;
        }

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                // 新建一个URL对象
                URL url = new URL(getUrl);
                // 打开一个HttpURLConnection连接
                HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
                // 设置连接主机超时时间
                urlConn.setConnectTimeout(5 * 1000);
                //设置从主机读取数据超时
                urlConn.setReadTimeout(5 * 1000);
                // 设置是否使用缓存  默认是true
                urlConn.setUseCaches(true);
                // 设置为Post请求
                urlConn.setRequestMethod("GET");
                //urlConn设置请求头信息
                urlConn.setRequestProperty("User-Agent", USER_AGENT);
                if (!(prop == null)) {
                    for (int i = 0; i < prop.size(); i++) {
                        String[] temp = prop.get(i);
                        urlConn.setRequestProperty(temp[0], temp[1]);
                    }
                }
                //设置请求中的媒体类型信息。
//            urlConn.setRequestProperty("Content-Type", "application/json");
                //设置客户端与服务连接类型
//            urlConn.addRequestProperty("Connection", "Keep-Alive");
                // 开始连接
                urlConn.connect();
                // 判断请求是否成功
                int statusCode = urlConn.getResponseCode();
                if (statusCode == 200) {
                    // 获取返回的数据
                    result = streamToString(urlConn.getInputStream());
                }
                // 关闭连接
                urlConn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                result = "";
            }
            Log.d("TAG", "get结果：" + result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if (callback != null) {
                callback.onGetDone(result);
            }
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
    }

}
