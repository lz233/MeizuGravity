package com.zhy.mediaplayer_exo.playermanager.common

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.zhy.mediaplayer_exo.playermanager.manager.MediaManager


fun getBitmap(
        context: Context,
        coverUrl: String,
        block: (bitmap: android.graphics.Bitmap) -> Unit
) {
    if (!TextUtils.isEmpty(MediaManager.currentId())) {
        MediaManager.getCacheBitmap()?.let {
            block(it)
            return
        }
    }
    Glide.with(context)
            .asBitmap()
            .load(coverUrl)
            .override(500, 500)
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                        resource: Bitmap?,
                        model: Any?,
                        target: Target<Bitmap>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                ): Boolean {
                    resource?.let {
                        MediaManager.setCacheBitmap(it)
                        block(it)
                    }
                    return true
                }
            })
            .into(ImageView(context))
}