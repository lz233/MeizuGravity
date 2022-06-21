package moe.lz233.meizugravity.cloudmusic

import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.okhttp.OkHttpDataSource
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.zhy.mediaplayer_exo.playermanager.MediaPlayerService
import moe.lz233.meizugravity.cloudmusic.logic.network.ServiceCreator
import moe.lz233.meizugravity.design.App
import org.conscrypt.Conscrypt
import java.security.Security

class App : App(BuildConfig.APPLICATION_ID) {

    override fun onCreate() {
        super.onCreate()
        Security.insertProviderAt(Conscrypt.newProvider(), 1)
        //设置固定码率 比特率 某些音频格式文件，在seekTo的时候无法找到对应的节点导致无法跳播，此代码可以解决这个问题
        /*val extractorsFactory = DefaultExtractorsFactory().setConstantBitrateSeekingEnabled(true)
        val audioAttributes = AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()*/
        MediaPlayerService.init(this, SimpleExoPlayer.Builder(this, DefaultExtractorsFactory())
                //.setAudioAttributes(audioAttributes, true)
                .setMediaSourceFactory(DefaultMediaSourceFactory(OkHttpDataSource.Factory(ServiceCreator.exoPlayerOkHttpClient)))
                .build())
    }
}