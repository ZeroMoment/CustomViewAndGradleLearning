package cn.lzm.stu.view.and.gradle.view

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import cn.lzm.stu.view.and.gradle.R
import cn.lzm.stu.view.and.gradle.utils.CommonUtil
import kotlin.math.max
import kotlin.math.min

/**
 * @des: 图片简介view
 * @author: lizhiming
 * @date: 2022/9/26 16:12
 */
class ImageWithDescView : View {

    //图片资源
    private var mImage : Bitmap? = null
    //图片缩放模式
    private var mImageScale = 0

    //文本
    private var mTitleText : String = "TestText"
    //文本颜色
    private var mTitleTextColor : Int = Color.BLUE
    //文本大小
    private var mTitleTextSize : Int = 16

    //图片的绘制范围
    private var mRect: Rect
    //文本绘制范围
    private var mTextBound: Rect

    private var mPaint : Paint

    //控件宽高
    private var mWidth =0
    private var mHeight = 0

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

        val typeArray = context?.theme?.obtainStyledAttributes(attrs, R.styleable.CustomImageView, defStyleAttr, 0)
        val attrsCount = typeArray?.indexCount

        for (index in 0 until attrsCount!!) {
            when (val attrResId = typeArray.getIndex(index)) {
                R.styleable.CustomImageView_image -> mImage = BitmapFactory.decodeResource(resources, typeArray.getResourceId(attrResId, 0))
                R.styleable.CustomImageView_imageScaleType -> mImageScale = typeArray.getInt(attrResId, 0)
                R.styleable.CustomImageView_titleText -> mTitleText = typeArray.getString(attrResId)!!
                R.styleable.CustomImageView_titleTextColor -> mTitleTextColor = typeArray.getColor(attrResId, Color.BLUE)
                R.styleable.CustomImageView_titleTextSize -> mTitleTextSize = typeArray.getDimensionPixelSize(attrResId,
                    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16F, resources.displayMetrics).toInt())
            }
        }

        typeArray.recycle()

        mRect = Rect()
        mPaint = Paint()
        mTextBound = Rect()

        mPaint.textSize = mTitleTextSize.toFloat()
        //计算了描绘字体需要的范围
        mPaint.getTextBounds(mTitleText, 0 , mTitleText.length, mTextBound)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        /**
         * 宽度计算
         */
        var specMode = MeasureSpec.getMode(widthMeasureSpec)
        var specSize = MeasureSpec.getSize(widthMeasureSpec)

        if(specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize
        } else {
            //由图片决定的宽
            val desireByImg = paddingLeft + paddingRight + (mImage?.width ?: 0)
            //由字体决定的宽
            val desireByTxt = paddingLeft + paddingRight + mTextBound.width()

            if(specMode == MeasureSpec.AT_MOST) {
                //取两个宽度最大值
                val desireVale = max(desireByImg, desireByTxt)
                //设置宽度，因为是wrap_content，取满足宽度的最小值即可
                mWidth = min(desireVale, specSize)
            }

        }

        /**
         * 高度计算
         */
        specMode = MeasureSpec.getMode(heightMeasureSpec)
        specSize = MeasureSpec.getSize(heightMeasureSpec)

        if(specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize
        } else {
            //高度需要两者累加
            val desireVale = paddingTop + paddingBottom + (mImage?.height ?: 0) + mTextBound.height()

            if(specMode == MeasureSpec.AT_MOST) {
                //设置高度，因为是wrap_content，取满足高度的最小值即可
                mHeight = min(desireVale, specSize)
            }

        }

        setMeasuredDimension(mWidth, mHeight)

    }

    override fun onDraw(canvas: Canvas?) {


        //图片的范围
        mRect.left = paddingLeft
        mRect.right = mWidth - paddingRight
        mRect.top = paddingTop
        mRect.bottom = mHeight - paddingBottom


        mPaint.color = mTitleTextColor
        mPaint.style = Paint.Style.FILL

        //当前设置的控件宽度小于字体需要的宽度，将文本末尾改为xx...
        if(mTextBound.width() > mWidth) {
            val textPaint = TextPaint(mPaint)
            val txtValue = TextUtils.ellipsize(mTitleText, textPaint,
                (mWidth - paddingLeft - paddingRight).toFloat(), TextUtils.TruncateAt.END).toString()
            canvas?.drawText(txtValue, paddingLeft.toFloat(), (mHeight - paddingBottom).toFloat(), mPaint)
        } else { //正常情况，将字体居中
            canvas?.drawText(mTitleText, (mWidth/2 - mTextBound.width() / 2).toFloat(), (mHeight - paddingBottom).toFloat(), mPaint)
        }

        //图片的范围需要去掉文本使用掉的范围
        mRect.bottom -= mTextBound.height()

        mPaint.color = Color.BLACK
        mImage?.let {
            CommonUtil.printMsg("mImageScale:$mImageScale")
            if(mImageScale == 0) {//fitxy
                canvas?.drawBitmap(it, null, mRect, mPaint)
            } else { //默认居中

                //计算居中范围
                mRect.left = mWidth / 2 - it.width / 2
                mRect.right = mWidth /2 + it.width /2
                mRect.top = (mHeight - mTextBound.height()) /2 - it.height / 2
                mRect.bottom = (mHeight - mTextBound.height()) / 2 + it.height / 2

                canvas?.drawBitmap(it, null, mRect, mPaint)
            }
        }

        //边框
        mPaint.strokeWidth = 4f
        mPaint.style = Paint.Style.STROKE
        mPaint.color = Color.CYAN
        canvas?.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), mPaint)
    }
}