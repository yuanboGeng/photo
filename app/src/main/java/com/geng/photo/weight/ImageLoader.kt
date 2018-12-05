package com.geng.photo.weight

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.text.TextUtils
import android.util.LruCache
import android.widget.ImageView

import com.geng.photo.util.BitmapUtil
import java.lang.ref.WeakReference

/**
 * Created by gengyuanbo on 2018/12/1.
 * Desc:
 */
object ImageLoader {
    private const val DEFAULT_MEM_CACHE_SIZE = 5 * 1024 * 1024
    const val DEFAULT_WIDTH = 400
    const val DEFAULT_HEIGHT = 200


    private var mImageCache: LruCache<String, Bitmap>? = null
    private var mIsLock: Boolean = false
    private var mDestroy: Boolean = false
    private var mContextWr: WeakReference<Context>? = null

    fun init(context: Context) {
        initMemCache(ImageLoaderUtil.calculateMemoryCacheSize(context))
    }

    private fun initMemCache(size: Int) {
        var realSize = if (size > 0) size else DEFAULT_MEM_CACHE_SIZE
        mImageCache = object : LruCache<String, Bitmap>(realSize) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount
            }
        }
    }

    fun with(context: Context): ImageLoader {
        mContextWr = WeakReference(context)
        return this
    }

    fun loadImage(imageView: ImageView, imageUrl: String): ImageLoader {
        mDestroy = false
        if (mImageCache == null) {
            init(mContextWr!!.get()!!)
        }
        if (mImageCache!!.get(imageUrl) != null) {
            //FIXME by GYB desc: fix me
            imageView.setImageBitmap(mImageCache!!.get(imageUrl))
            return this
        }

        val imageLoaderTask = ImageLoaderTask(imageView)
        if (!mIsLock) {
            imageLoaderTask.execute(imageUrl)
        }
        return this
    }

    fun lock() {
        mIsLock = true
    }

    fun unLock() {
        mIsLock = false
    }

    fun destroy() {
        mDestroy = true
        if (mImageCache != null) {
            mImageCache!!.evictAll()
        }
    }

    internal class ImageLoaderTask(private val mImageView: ImageView?) : AsyncTask<String, Int, Bitmap>() {
        private var mImageUrl: String? = null

        override fun doInBackground(vararg strings: String): Bitmap? {
            mImageUrl = strings[0]
            return if (mImageView == null || TextUtils.isEmpty(mImageUrl)) {
                null
            } else BitmapUtil.decodeSampledBitmap(mImageUrl!!, DEFAULT_WIDTH, DEFAULT_HEIGHT)
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            super.onPostExecute(bitmap)
            if (mImageView == null || bitmap == null || mDestroy) {
                return
            }
            if (mContextWr!!.get() !is Activity) {
                return
            }
            val activity = mContextWr!!.get() as Activity?
            if (activity!!.isFinishing) {
                return
            }
            mImageCache!!.put(mImageUrl, bitmap)

            if (mImageView.tag != null && mImageView.tag == mImageUrl) {
                mImageView.setImageBitmap(bitmap)
            }

        }

        override fun onPreExecute() {
            super.onPreExecute()
            if (mIsLock || mDestroy) {
                cancel(true)
            }
        }
    }


}
