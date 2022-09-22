package cn.lzm.stu.view.and.gradle

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @des: 基类
 * @author: lizhiming
 * @date: 2022/9/22 18:03
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract fun getLayoutResource() : Int
    abstract fun initView()
    abstract fun initData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResource())

        initView()
        initData()
    }

}