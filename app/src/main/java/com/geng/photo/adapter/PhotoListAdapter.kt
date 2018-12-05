package com.geng.photo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.geng.photo.R
import com.geng.photo.weight.ImageLoader
import java.util.*

/**
 * Created by gengyuanbo on 2018/12/1.
 * Desc:
 */
class PhotoListAdapter(private var mContext: Context, data: List<String>?) : BaseAdapter() {
    private var mPhotoList: List<String>? = ArrayList()
    private var mInflater: LayoutInflater

    init {
        if (data != null) {
            mPhotoList = data
        }
        mInflater = LayoutInflater.from(mContext)
    }

    override fun getCount(): Int {
        return mPhotoList!!.size
    }

    override fun getItem(position: Int): Any? {
        return if (mPhotoList != null && position >= 0 && position < mPhotoList!!.size) {
            mPhotoList!![position]
        } else null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val viewHolder: ViewHolder
        if (view == null) {
            viewHolder = ViewHolder()
            view = mInflater.inflate(R.layout.photo_adapter_item, null)
            viewHolder.mIvPhoto = view!!.findViewById(R.id.iv_photo)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }
        val imgUrl = mPhotoList!![position]
        viewHolder.mIvPhoto!!.tag = imgUrl
        viewHolder.mIvPhoto!!.setImageResource(R.mipmap.ic_launcher)
        ImageLoader.with(mContext).loadImage(viewHolder.mIvPhoto!!, imgUrl)
        return view
    }

    internal inner class ViewHolder {
        var mIvPhoto: ImageView? = null
    }

}
