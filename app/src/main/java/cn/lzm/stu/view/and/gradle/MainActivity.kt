package cn.lzm.stu.view.and.gradle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.lzm.stu.view.and.gradle.adapter.MainAdapter
import cn.lzm.stu.view.and.gradle.interf.ICommonRecyclerItemClickListener
import cn.lzm.stu.view.and.gradle.page.CircleProgressActivity
import cn.lzm.stu.view.and.gradle.page.CustomTitleViewActivity
import cn.lzm.stu.view.and.gradle.page.FourCornerActivity
import cn.lzm.stu.view.and.gradle.page.ImageWithDescActivity
import cn.lzm.stu.view.and.gradle.utils.CommonUtil

class MainActivity : BaseActivity() {

    private lateinit var mAdapter: MainAdapter

    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun initView() {
        mAdapter = MainAdapter(this)
        val recyclerView = findViewById<RecyclerView>(R.id.main_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }

    override fun initData() {
        val mainItemList = listOf(
            "四角定点显示View",
            "简单文本-随机数字View",
            "图片带文字介绍",
            "交替圆环进度",
            "待定")
        mAdapter.setNewData(mainItemList, object : ICommonRecyclerItemClickListener {
            override fun onItemClick(itemData: Any, position: Int) {
                clickEvent(position)
            }
        })
    }


    private fun clickEvent(position: Int) {
        when (position) {
            0 -> startActivity(Intent(this, FourCornerActivity::class.java))
            1 -> startActivity(Intent(this, CustomTitleViewActivity::class.java))
            2 -> startActivity(Intent(this, ImageWithDescActivity::class.java))
            3 -> startActivity(Intent(this, CircleProgressActivity::class.java))
            else -> {
                CommonUtil.showToast(this, "待定")
            }
        }
    }
}
