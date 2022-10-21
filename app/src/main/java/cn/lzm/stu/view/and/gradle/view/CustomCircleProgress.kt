package cn.lzm.stu.view.and.gradle.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import cn.lzm.stu.view.and.gradle.R

/**
 * @des: 交替圆环view
 * @author: lizhiming
 * @date: 2022/10/20 16:13
 */
class CustomCircleProgress : View {

    //第一圈的颜色
    private var mFirstColor : Int = 0

    //第二圈的颜色
    private var mSecondColor: Int = 0

    //圈的宽度
    private var mCircleWidth: Int = 0

    //画笔
    private var mPaint: Paint
    //当前进度
    private var mProgress: Int = 0

    //速度
    private var mSpeed: Int = 0

    //是否开始下一个
    private var isNext: Boolean = false


    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        val typeArr = context!!.theme.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar, defStyleAttr, 0)
        val arrCount = typeArr.indexCount

        for(index in 0 until arrCount) {
            when(val attrIndex = typeArr.getIndex(index)) {
                R.styleable.CustomProgressBar_firstColor -> mFirstColor = typeArr.getColor(attrIndex, Color.GREEN)
                R.styleable.CustomProgressBar_secondColor -> mSecondColor = typeArr.getColor(attrIndex, Color.RED)
                R.styleable.CustomProgressBar_circleWidth -> mCircleWidth = typeArr.getDimensionPixelSize(attrIndex, TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX, 20F, resources.displayMetrics).toInt())
                R.styleable.CustomProgressBar_speed -> mSpeed = typeArr.getInt(attrIndex, 20)
            }
        }
        typeArr.recycle()
        mPaint = Paint()

        //绘图线程
        Thread {
            while (true) {
                mProgress++
                if (mProgress == 360) {
                    mProgress = 0
                    isNext = !isNext
                }

                postInvalidate()
                try {
                    Thread.sleep(mSpeed.toLong())
                } catch (excep : InterruptedException) {
                    excep.printStackTrace()
                }

            }
        }.start()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {

        val center = width / 2 //获取圆心的x坐标
        val radius = center - mCircleWidth / 2 //半径

        mPaint.strokeWidth = mCircleWidth.toFloat() //设置圆环宽度
        mPaint.isAntiAlias = true //消除锯齿
        mPaint.style = Paint.Style.STROKE //设置空心

        val oval = RectF((center-radius).toFloat(), (center-radius).toFloat(),
            (center+radius).toFloat(), (center+radius).toFloat()) //用于定义圆弧的形状和大小的界限

        if(isNext) {
            mPaint.color = mSecondColor //设置圆环颜色
            canvas?.drawCircle(center.toFloat(), center.toFloat(), radius.toFloat(), mPaint) //画出圆环
            mPaint.color = mFirstColor //设置圆环进度颜色
            canvas?.drawArc(oval, -90F, mProgress.toFloat(), false, mPaint) //画出进度圆弧
        } else { //首次，从第一颜色设置开始
            mPaint.color = mFirstColor //设置圆环颜色
            canvas?.drawCircle(center.toFloat(), center.toFloat(), radius.toFloat(), mPaint) //画出圆环
            mPaint.color = mSecondColor //设置圆环进度颜色
            canvas?.drawArc(oval, -90F, mProgress.toFloat(), false, mPaint) //画出进度圆弧
        }


    }
}