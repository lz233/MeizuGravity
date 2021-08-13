package moe.lz233.meizugravity.cloudmusic

import android.content.Context
import android.content.SharedPreferences
import androidx.multidex.MultiDexApplication
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.zhy.mediaplayer_exo.playermanager.MediaPlayerService
import moe.lz233.meizugravity.cloudmusic.logic.network.ServiceCreator
import org.conscrypt.Conscrypt
import java.security.Security

class App : MultiDexApplication() {
    companion object {
        lateinit var context: Context
        lateinit var sp: SharedPreferences
        lateinit var editor: SharedPreferences.Editor
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        sp = context.getSharedPreferences(BuildConfig.APPLICATION_ID, MODE_PRIVATE)
        editor = sp.edit()
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
        //设置固定码率 比特率 某些音频格式文件，在seekTo的时候无法找到对应的节点导致无法跳播，此代码可以解决这个问题
        /*val extractorsFactory = DefaultExtractorsFactory().setConstantBitrateSeekingEnabled(true)
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()*/
        MediaPlayerService.init(this, SimpleExoPlayer.Builder(this, DefaultExtractorsFactory())
                //.setAudioAttributes(audioAttributes, true)
                .setMediaSourceFactory(DefaultMediaSourceFactory(OkHttpDataSource.Factory(ServiceCreator.okHttpClient)))
                .build())
    }
}