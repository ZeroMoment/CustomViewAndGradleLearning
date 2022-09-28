package cn.lzm.stu.view.and.gradle.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import cn.lzm.stu.view.and.gradle.R
import java.util.*
import kotlin.collections.HashSet

/**
 * @des: 简单文本view
 *
 * 构造里新增一个点击事件，在简单文本view基础上，实现随机码
 *
 * @author: lizhiming
 * @date: 2022/9/23 14:33
 */
class CustomTitleView : View {

    //文本
    private var mTitleText : String = "TestText"
    //文本颜色
    private var mTitleTextColor : Int = Color.BLUE
    //文本大小
    private var mTitleTextSize : Int = 16

    //绘制范围
    private var mBound: Rect

    private var mPaint : Paint

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        val typeArray  = context?.theme?.obtainStyledAttributes(attrs, R.styleable.CustomTitleView, defStyleAttr, 0)
        val attrCount = typeArray?.indexCount
        for (index in 0 until attrCount!!) {
            when (val attrResId = typeArray.getIndex(index)) {
                R.styleable.CustomTitleView_titleText -> mTitleText = typeArray.getString(attrResId)!!
                R.styleable.CustomTitleView_titleTextColor -> mTitleTextColor = typeArray.getColor(attrResId, Color.BLUE)
                R.styleable.CustomTitleView_titleTextSize -> mTitleTextSize = typeArray.getDimensionPixelSize(attrResId,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16F, resources.displayMetrics).toInt())
            }
        }

        typeArray.recycle()

        /**
         * 获得绘制文本的宽和高
         */
        mPaint = Paint()
        mPaint.textSize = mTitleTextSize.toFloat()

        mBound = Rect()
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length, mBound)

        setListener()

    }

    private fun setListener() {
        this.setOnClickListener {
            mTitleText = randomText()
            postInvalidate()
        }
    }

    /**
     * 生成随机数字
     */
    private fun randomText() : String {
        val random = Random()
        val setInteger = HashSet<Int>()
        while (setInteger.size < 4) {
            val randomInt = random.nextInt(10)
            setInteger.add(randomInt)
        }

        val sb = StringBuffer()
        for (intValue in setInteger) {
            sb.append("$intValue")
        }

        return sb.toString()
    }

    /**
     * 当设置wrap_content时，系统不能给准确测量，会是match_parent的效果
     * 想要包裹内容的效果，需要自己实现测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var realWidth = 0
        var realHeight = 0

        if(widthMode == MeasureSpec.EXACTLY) {
            realWidth = widthSize
        } else {
            mPaint.textSize = mTitleTextSize.toFloat()
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length, mBound)
            val textWidth = mBound.width()
            val desiredWidth = paddingLeft + textWidth + paddingRight
            realWidth = desiredWidth
        }

        if(heightMode == MeasureSpec.EXACTLY) {
            realHeight = heightSize
        } else {
            mPaint.textSize = mTitleTextSize.toFloat()
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length, mBound)
            val textHeight = mBound.height()
            val desiredHeight = paddingTop + textHeight + paddingBottom
            realHeight = desiredHeight
        }

        setMeasuredDimension(realWidth, realHeight)

    }

    override fun onDraw(canvas: Canvas?) {

        //画个填充背景
        mPaint.color = Color.CYAN
        canvas?.drawRect(0F, 0F, measuredWidth.toFloat(), measuredHeight.toFloat(), mPaint)

        //设置文本绘制
        mPaint.color = mTitleTextColor
        canvas?.drawText(mTitleText, (width/2 - mBound.width()/2).toFloat(), (height/2 + mBound.height()/2).toFloat(), mPaint)

    }
}

