package com.geng.photo.weight

import android.app.ActivityManager
import android.content.Context

/**
 * Created by gengyuanbo on 2018/12/3.
 * Desc:
 */
object ImageLoaderUtil {

    fun calculateMemoryCacheSize(context: Context): Int {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memClass = activityManager.memoryClass

        return 1024 * 1024 * memClass / 7
    }

}
