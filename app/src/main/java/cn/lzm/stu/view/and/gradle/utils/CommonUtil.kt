package cn.lzm.stu.view.and.gradle.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * @des:
 * @author: lizhiming
 * @date: 2022/9/22 18:09
 */
object CommonUtil {

    fun showToast(context : Context, msg : String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    fun printMsg (msg: String) {
        Log.e("custom_view", msg)
    }

}