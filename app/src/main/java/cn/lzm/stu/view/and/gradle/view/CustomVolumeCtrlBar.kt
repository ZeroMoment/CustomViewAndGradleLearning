package cn.lzm.stu.view.and.gradle.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import cn.lzm.stu.view.and.gradle.R
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * @des: 音量调节（算法没太懂）
 * @author: lizhiming
 * @date: 2022/10/24 17:53
 */
class CustomVolumeCtrlBar : View {

    //第一圈颜色
    private var mFirstColor: Int = Color.GREEN
    //第二圈颜色
    private var mSecondColor: Int = Color.CYAN
    //圈的宽度
    private var mCircleWidth : Int = 20

    //画笔
    private var mPaint: Paint

    //当前进度
    private var mCurrentCount = 3
    //中间的图片
    private var mImage :Bitmap? = null
    //块块间隙
    private var mSplitSize = 10

    //块块个数(应该是间隙的个数:12个间隙，就是13个进度块块)
    private var mCount = 12

    private var mRect:Rect

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typeArr = context!!.theme.obtainStyledAttributes(attrs, R.styleable.CustomVolumeControlBar, defStyleAttr, 0)
        val arrCount = typeArr.indexCount

        for(index in 0 until arrCount) {
            when(val attrIndex = typeArr.getIndex(index)) {
                R.styleable.CustomVolumeControlBar_firstColor -> mFirstColor = typeArr.getColor(attrIndex, Color.GREEN)
                R.styleable.CustomVolumeControlBar_secondColor -> mSecondColor = typeArr.getColor(attrIndex, Color.CYAN)
                R.styleable.CustomVolumeControlBar_image -> mImage = BitmapFactory.decodeResource(resources, typeArr.getResourceId(attrIndex, 0))
                R.styleable.CustomVolumeControlBar_circleWidth -> mCircleWidth = typeArr.getDimensionPixelSize(attrIndex, TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_PX, 20F, resources.displayMetrics).toInt())
                R.styleable.CustomVolumeControlBar_dotCount -> mCount = typeArr.getInt(attrIndex, 20)
                R.styleable.CustomVolumeControlBar_splitSize -> mSplitSize = typeArr.getInt(attrIndex, 20)
            }
        }
        typeArr.recycle()
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = mCircleWidth.toFloat()
        mPaint.strokeCap = Paint.Cap.ROUND //设置圆头
        mPaint.style = Paint.Style.STROKE

        mRect = Rect()
    }

    override fun onDraw(canvas: Canvas?) {

        canvas?.let {

            val center = width / 2 //圆心x坐标
            val radius = center - mCircleWidth/2 //半径

            //画块块
            drawOval(it, center, radius)

            /**
             * 计算内正方形的位置（限制中心图片显示的最大区域）
             */
            val relRadius = radius - mCircleWidth/2 //内圆半径

            //内切正方形距离顶部
            mRect.left = ((relRadius - sqrt(2.0) *1.0f/2*relRadius) +mCircleWidth).toInt()
            //顶部
            mRect.top = ((relRadius - sqrt(2.0) *1.0f/2*relRadius) +mCircleWidth).toInt()
            mRect.bottom = (mRect.left + sqrt(2.0) *relRadius).toInt()
            mRect.right = (mRect.left + sqrt(2.0) *relRadius).toInt()

            mImage?.let { img -> {
                /**
                 * 如果图片比较小，放正中心
                 */
                if(img.width < sqrt(2.0)*relRadius) {
                    mRect.left = (mRect.left + sqrt(2.0) *relRadius * 1.0f/2- img.width*1.0f/2).toInt()
                    mRect.top = (mRect.top + sqrt(2.0) *relRadius * 1.0f/2- img.height*1.0f/2).toInt()
                    mRect.bottom = mRect.left + img.width
                    mRect.right = mRect.top + img.height
                }

                it.drawBitmap(img, null, mRect, mPaint)
            }}
        }

    }

    private var mOval: RectF? = null
    /**
     * 根据参数画出每个小块
     */
    private fun drawOval(canvas: Canvas, center:Int, radius:Int) {

        //根据需要画的个数以及间隙计算每个块块所占的比例*360
        val itemSize = (360*1.0f-mCount*mSplitSize) / mCount

        mOval = RectF((center - radius).toFloat(),
            (center - radius).toFloat(), (center +radius).toFloat(), (center + radius).toFloat()
        )
        mPaint.color = mFirstColor //设置圆环的颜色
        for (index in 0 until mCount) {
            canvas.drawArc(mOval!!, index*(itemSize+mSplitSize), itemSize, false, mPaint)
        }

        mPaint.color = mSecondColor //进度圆环的颜色
        for (index in 0 until mCurrentCount) {
            canvas.drawArc(mOval!!, index*(itemSize+mSplitSize), itemSize, false, mPaint) //根据进度画圆弧
        }
    }

    private var xDown = 0
    private var xUp = 0
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event == null)
            return super.onTouchEvent(event)

        when(event.action) {
            MotionEvent.ACTION_DOWN -> xDown = event.y.toInt()
            MotionEvent.ACTION_UP -> {
                xUp = event.y.toInt()
                if(xUp > xDown) //下滑，音量减小效果
                    down()
                else
                    up()
            }
        }

        return true
    }

    private fun up(){
        mCurrentCount++
        postInvalidate()
    }

    private fun down(){
        mCurrentCount--
        postInvalidate()
    }
}