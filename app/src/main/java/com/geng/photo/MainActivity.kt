package com.geng.photo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.geng.photo.adapter.PhotoListAdapter
import com.geng.photo.listener.ListViewScrollListener
import com.geng.photo.util.PhotoUtil
import com.geng.photo.weight.ImageLoader
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var mPhotoListAdapter: PhotoListAdapter? = null
    private var mDataSource: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ImageLoader.init(this)
        getPhotoList()
        initView()
    }

    private fun getPhotoList() {
        mDataSource = PhotoUtil.getSystemPhotoList(this)

    }

    private fun initView() {
        mPhotoListAdapter = PhotoListAdapter(this, mDataSource)
        photo_list.adapter = mPhotoListAdapter
        photo_list.emptyView = empty_tv
        mPhotoListAdapter!!.notifyDataSetChanged()
        photo_list.setOnScrollListener(ListViewScrollListener(mPhotoListAdapter))
    }

    override fun onStart() {
        super.onStart()
        getPhotoList()
        mPhotoListAdapter!!.notifyDataSetChanged()
    }

    override fun onDestroy() {
        ImageLoader.destroy()
        super.onDestroy()
    }
}


