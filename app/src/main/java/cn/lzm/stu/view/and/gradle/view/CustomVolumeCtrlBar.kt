package cn.lzm.stu.view.and.gradle.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import cn.lzm.stu.view.and.gradle.R

/**
 * @des: 音量调节
 * @author: lizhiming
 * @date: 2022/10/24 17:53
 */
class CustomVolumeCtrlBar : View {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typeArr = context!!.theme.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0)
        val arrCount = typeArr.indexCount
    }
}