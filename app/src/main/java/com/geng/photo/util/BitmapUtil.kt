package com.geng.photo.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

/**
 * Created by gengyuanbo on 2018/12/1.
 * Desc:
 */
object BitmapUtil {

    fun decodeSampledBitmap(path: String, compressWidth: Int, compressHeight: Int): Bitmap? {
        var bitmap: Bitmap? = null
        var compressImage: Bitmap? = null
        try {
            val options = BitmapFactory.Options()
            // get photo size
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)

            options.inSampleSize = calculateInSampleSize(options, compressWidth, compressHeight)
            options.inPreferredConfig = Bitmap.Config.RGB_565
            options.inJustDecodeBounds = false
            bitmap = BitmapFactory.decodeFile(path, options)
            compressImage = compressImage(bitmap, 70)
        } catch (e: Exception) {
            //FIXME by GYB desc: report
        } finally {
            if (bitmap != null) {
                bitmap.recycle()
            }
        }
        return compressImage
    }

    private fun calculateInSampleSize(
            options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    private fun compressImage(image: Bitmap?, quality: Int): Bitmap? {
        var bitmap: Bitmap? = null
        var bos: ByteArrayOutputStream? = null
        var bis: ByteArrayInputStream? = null
        try {
            bos = ByteArrayOutputStream()
            image!!.compress(Bitmap.CompressFormat.WEBP, quality, bos)
            var options = 90
            while (bos.toByteArray().size / 1024 > 100) {
                bos.reset()
                image.compress(Bitmap.CompressFormat.WEBP, options, bos)
                options -= 10
            }
            bis = ByteArrayInputStream(bos.toByteArray())
            bitmap = BitmapFactory.decodeStream(bis, null, null)
        } catch (e: Exception) {
            //FIXME by GYB desc: report
        } finally {
            if (bos != null) {
                bos.reset()
            }
            if (bis != null) {
                bis.reset()
            }
        }

        return bitmap
    }

}
