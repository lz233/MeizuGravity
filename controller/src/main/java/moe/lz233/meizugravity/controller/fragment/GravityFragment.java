package moe.lz233.meizugravity.controller.fragment;

import android.animation.LayoutTransition;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import moe.lz233.meizugravity.controller.R;
import moe.lz233.meizugravity.controller.service.AdbService;
import moe.lz233.meizugravity.controller.util.DownloadUtil;
import moe.lz233.meizugravity.controller.view.AdjustImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static moe.lz233.meizugravity.controller.util.UriUtil.getHostUri;

public class GravityFragment extends BaseFragment {
    private OkHttpClient client = new OkHttpClient();

    private SwipeRefreshLayout gravitySwipeRefreshLayout;
    private LinearLayout gravityLinearLayout;
    private AdjustImageView coverAdjustImageView;
    private TextView trackTitleTextView;
    private TextView artistNameTextView;
    private ExtendedFloatingActionButton acousticsExtendedFloatingActionButton;
    private FloatingActionButton prevFloatingActionButton;
    private FloatingActionButton playFloatingActionButton;
    private FloatingActionButton nextFloatingActionButton;
    public GravityFragment() {
    }

    public GravityFragment(SharedPreferences sharedPreferences, SharedPreferences.Editor editor) {
        super(sharedPreferences, editor);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_gravity, container, false);
        //
        gravitySwipeRefreshLayout = rootView.findViewById(R.id.gravitySwipeRefreshLayout);
        gravityLinearLayout = rootView.findViewById(R.id.gravityLinearLayout);
        coverAdjustImageView = rootView.findViewById(R.id.coverAdjustImageView);
        trackTitleTextView = rootView.findViewById(R.id.trackTitleTextView);
        artistNameTextView = rootView.findViewById(R.id.artistNameTextView);
        acousticsExtendedFloatingActionButton = rootView.findViewById(R.id.acousticsExtendedFloatingActionButton);
        prevFloatingActionButton = rootView.findViewById(R.id.prevFloatingActionButton);
        playFloatingActionButton = rootView.findViewById(R.id.playFloatingActionButton);
        nextFloatingActionButton = rootView.findViewById(R.id.nextFloatingActionButton);
        //
        gravityLinearLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        getSongInfo();
        //
        gravitySwipeRefreshLayout.setOnRefreshListener(() -> getSongInfo());
        acousticsExtendedFloatingActionButton.setOnClickListener(view -> {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getActivity());
            builder.setTitle(R.string.acoustics);
            builder.setItems(new String[]{getString(R.string.acousticsType0), getString(R.string.acousticsType1), getString(R.string.acousticsType2), getString(R.string.acousticsType3), getString(R.string.acousticsType4), getString(R.string.acousticsType5)}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        RequestBody requestBody = RequestBody.create(new JSONObject().put("EQMode", i).toString(), json);
                        Request request = new Request.Builder().post(requestBody).url(getHostUri(getActivity(), "7766") + "SetEQMode").build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                acousticsExtendedFloatingActionButton.post(() -> acousticsExtendedFloatingActionButton.setText(getString(R.string.acoustics) + getEQName(i)));
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            AlertDialog materialDialogs = builder.create();
            materialDialogs.setCancelable(true);
            materialDialogs.show();
        });
        prevFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 88")));
        prevFloatingActionButton.setOnLongClickListener(view -> {
            client.newCall(new Request.Builder().get().url(getHostUri(getActivity(), "7766") + "Prev").build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                }
            });
            return true;
        });
        playFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 85")));
        playFloatingActionButton.setOnLongClickListener(view -> {
            client.newCall(new Request.Builder().get().url(getHostUri(getActivity(),"7766")+"Status").build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        String status = new JSONObject(response.body().string()).getJSONObject("data").optString("status");
                        String cmd;
                        switch (status){
                            default:
                                cmd = "Play";
                                break;
                            case "STARTED":
                                cmd = "Pause";
                                break;
                        }
                        client.newCall(new Request.Builder().get().url(getHostUri(getActivity(),"7766")+cmd).build()).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {

                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                            }
                        });
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            return true;
        });
        nextFloatingActionButton.setOnClickListener(view -> getActivity().startService(new Intent(getActivity(), AdbService.class).putExtra("cmd", "input keyevent 87")));
        nextFloatingActionButton.setOnLongClickListener(view -> {
            client.newCall(new Request.Builder().get().url(getHostUri(getActivity(), "7766") + "Next").build()).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                }
            });
            return true;
        });
        return rootView;
    }

    private String getEQName(int i) {
        switch (i) {
            default:
                return getString(R.string.acousticsType0);
            case 1:
                return getString(R.string.acousticsType1);
            case 2:
                return getString(R.string.acousticsType2);
            case 3:
                return getString(R.string.acousticsType3);
            case 4:
                return getString(R.string.acousticsType4);
            case 5:
                return getString(R.string.acousticsType5);
        }
    }

    private void getSongInfo() {
        gravitySwipeRefreshLayout.setRefreshing(true);
        client.newCall(new Request.Builder().get().url(getHostUri(getActivity(), "7766") + "Status").build()).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                gravitySwipeRefreshLayout.post(() -> gravitySwipeRefreshLayout.setRefreshing(false));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string()).getJSONObject("data");
                    JSONObject trackJsonObject = jsonObject.getJSONObject("playList").getJSONArray("trackList").getJSONObject(0);
                    trackTitleTextView.post(() -> {
                        acousticsExtendedFloatingActionButton.setText(getString(R.string.acoustics) + getEQName(jsonObject.optInt("EQMode")));
                        trackTitleTextView.setText(trackJsonObject.optString("trackTitle"));
                        artistNameTextView.setText(trackJsonObject.optString("artistName"));
                    });
                    String coverUrl = trackJsonObject.optString("coverUrl");
                    if (new File(getActivity().getCacheDir().getPath() + "/Cover/cover").exists() & (!coverUrl.equals("assets://default_logo.png"))) {
                        coverAdjustImageView.post(() -> coverAdjustImageView.setImageBitmap(BitmapFactory.decodeFile(getActivity().getCacheDir().getPath() + "/Cover/cover")));
                    } else {
                        coverAdjustImageView.post(() -> coverAdjustImageView.setImageDrawable(getActivity().getDrawable(R.drawable.gravity)));
                    }
                    if ((!coverUrl.equals("assets://default_logo.png")) & (!trackJsonObject.optString("trackTitle").equals(sharedPreferences.getString("trackTitle", "")))) {
                        new DownloadUtil().download(coverUrl, getActivity().getCacheDir().getPath() + "/Cover/", "cover", new DownloadUtil.OnDownloadListener() {
                            @Override
                            public void onDownloadSuccess(File file) {
                                coverAdjustImageView.post(() -> {
                                    coverAdjustImageView.setImageBitmap(BitmapFactory.decodeFile(getActivity().getCacheDir().getPath() + "/Cover/cover"));
                                    gravitySwipeRefreshLayout.setRefreshing(false);
                                });
                            }

                            @Override
                            public void onDownloading(int progress) {

                            }

                            @Override
                            public void onDownloadFailed(Exception e) {
                                gravitySwipeRefreshLayout.post(() -> gravitySwipeRefreshLayout.setRefreshing(false));
                            }
                        });
                    } else {
                        gravitySwipeRefreshLayout.post(() -> gravitySwipeRefreshLayout.setRefreshing(false));
                    }
                    editor.putString("trackTitle", trackJsonObject.optString("trackTitle"));
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
