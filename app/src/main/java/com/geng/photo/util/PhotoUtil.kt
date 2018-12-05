package com.geng.photo.util

import android.content.Context
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.util.*

/**
 * Created by gengyuanbo on 2018/12/1.
 * Desc:
 */
object PhotoUtil {
    private val TAG = PhotoUtil::class.java.simpleName!!

    fun getSystemPhotoList(context: Context): ArrayList<String>? {
        val result = ArrayList<String>()
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val contentResolver = context.contentResolver
        val cursor = contentResolver.query(uri, null, null, null, null)
        if (cursor == null || cursor.count <= 0) return null
        while (cursor.moveToNext()) {
            val index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val path = cursor.getString(index)
            val file = File(path)
            if (file.exists()) {
                result.add(path)
                Log.i(TAG, path)
            }
        }

        return result
    }

}
