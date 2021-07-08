package moe.lz233.meizugravity.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import me.wcy.lrcview.LrcView;
import moe.lz233.meizugravity.R;
import moe.lz233.meizugravity.utils.DownloadUtil;
import moe.lz233.meizugravity.view.ChanTextView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LrcActivity extends BaseActivity {
    private Handler handler = new Handler();
    private OkHttpClient client = new OkHttpClient();
    private String trackTitle = "";
    private boolean isShowLrc = true;
    private LrcView lrcView;
    private ImageView coverImageView;
    private LinearLayout detailLinearLayout;
    private ChanTextView titleChanTextView;
    private ChanTextView artistChanTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lrc);
        //
        lrcView = findViewById(R.id.lrcView);
        coverImageView = findViewById(R.id.coverImageView);
        detailLinearLayout = findViewById(R.id.detailLinearLayout);
        titleChanTextView = findViewById(R.id.titleChanTextView);
        artistChanTextView = findViewById(R.id.artistChanTextView);
        //
        initLrc();
    }

    private void initLrc() {
        new Thread(() -> {
            try {
                Headers neteaseHeaders = new Headers.Builder()
                        .add("User-Agent", "NeteaseMusic/5.4.1.1533213048(122);Dalvik/2.1.0 (Linux; U; Android 8.1.0; Redmi Note 5 MIUI/8.8.2)")
                        .add("Cookie", "buildver=1533213048; resolution=2030x1080; osver=8.1.0; appver=5.4.1; versioncode=122; mobilename=RedmiNote5; os=android; channel=netease")
                        .add("X-Real-IP", "112.88.230.176")
                        .add("Content-Type", "application/x-www-form-urlencoded; charset=utf-8").build();
                Request request1 = new Request.Builder().url("http://127.0.0.1:7766/Status").get().build();
                Response response1 = client.newCall(request1).execute();
                JSONObject jsonObject1 = new JSONObject(response1.body().string()).optJSONObject("data").optJSONObject("playList").optJSONArray("trackList").optJSONObject(0);
                String artistName = jsonObject1.optString("artistName");
                trackTitle = jsonObject1.optString("trackTitle");
                String coverUrl = jsonObject1.optString("coverUrl");
                //加载歌曲信息
                titleChanTextView.post(() -> {
                    titleChanTextView.setText(trackTitle);
                    artistChanTextView.setText(artistName);
                });
                //加载封面
                if (!coverUrl.equals("assets://default_logo.png")) {
                    new DownloadUtil().download(coverUrl, getCacheDir().getPath() + "/Cover/", "cover", new DownloadUtil.OnDownloadListener() {
                        @Override
                        public void onDownloadSuccess(File file) {
                            coverImageView.post(() -> {
                                //加载缩略的位图，防止内存不足
                                final BitmapFactory.Options options = new BitmapFactory.Options();
                                options.inSampleSize = 8;
                                Bitmap bitmap = BitmapFactory.decodeFile(getCacheDir().getPath() + "/Cover/cover", options);
                                coverImageView.setImageBitmap(bitmap);
                            });
                        }

                        @Override
                        public void onDownloading(int progress) {
                        }

                        @Override
                        public void onDownloadFailed(Exception e) {
                        }
                    });
                }
                //加载歌词
                RequestBody requestBody2 = new FormBody.Builder()
                        .add("s", artistName + " - " + trackTitle)
                        .add("type", "1")
                        .add("offset", "0")
                        .add("total", "true")
                        .add("limit", "1").build();
                Request request2 = new Request.Builder()
                        .url("https://music.163.com/api/search/pc")
                        .post(requestBody2)
                        .headers(neteaseHeaders)
                        .build();
                Response response2 = client.newCall(request2).execute();
                JSONObject jsonObject2 = new JSONObject(response2.body().string());
                String string2 = jsonObject2.optJSONObject("result").optJSONArray("songs").optJSONObject(0).optString("id");
                RequestBody requestBody3 = new FormBody.Builder()
                        .add("os", "pc")
                        .add("id", string2)
                        .add("lv", "-1")
                        .add("kv", "-1")
                        .add("tv", "-1").build();
                Request request3 = new Request.Builder()
                        .url("https://music.163.com/api/song/lyric")
                        .post(requestBody3)
                        .headers(neteaseHeaders)
                        .build();
                Response response3 = client.newCall(request3).execute();
                JSONObject jsonObject3 = new JSONObject(response3.body().string());
                String lrc = jsonObject3.optJSONObject("lrc").optString("lyric");
                String trc = jsonObject3.optJSONObject("tlyric").optString("lyric");
                lrcView.post(() -> {
                    if (trc.equals("")) {
                        lrcView.loadLrc(lrc);
                    } else {
                        lrcView.loadLrc(lrc, trc);
                    }
                });
                handler.post(runnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            finish();
        } else if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (isShowLrc) {
                isShowLrc = false;
                lrcView.setVisibility(View.INVISIBLE);
                coverImageView.setVisibility(View.VISIBLE);
                detailLinearLayout.setVisibility(View.VISIBLE);
            } else {
                isShowLrc = true;
                coverImageView.setVisibility(View.INVISIBLE);
                detailLinearLayout.setVisibility(View.INVISIBLE);
                lrcView.setVisibility(View.VISIBLE);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Request request = new Request.Builder().url("http://127.0.0.1:7766/Status").get().build();
            try {
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string()).optJSONObject("data");
                            String mTrackTitle = jsonObject.optJSONObject("playList").optJSONArray("trackList").optJSONObject(0).optString("trackTitle");
                            if (mTrackTitle.equals(trackTitle)) {
                                long lrcTime = jsonObject.optInt("elapsedTime") * 1000 + 1000;
                                lrcView.updateTime(lrcTime);
                            } else {
                                initLrc();
                                handler.removeCallbacks(runnable);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            handler.postDelayed(this, 300);
        }
    };
}