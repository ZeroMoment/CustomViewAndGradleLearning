package cn.lzm.stu.view.and.gradle.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import kotlin.math.max

/**
 * @des: 定义一个ViewGroup，内部可以传入0到4个childView，分别依次显示在左上角，右上角，左下角，右下角
 * @author: lizhiming
 * @date: 2022/9/21 18:05
 */
class FourCornerLayout:ViewGroup {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    /**
     * 对于我们这个例子，我们只需要ViewGroup能够支持margin即可，那么我们直接使用系统的MarginLayoutParams
     */
    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    /**
     * 计算所有ChildView的宽度和高度 然后根据ChildView的计算结果，设置自己的宽和高
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        //获得此viewgroup上级容器为其推荐的宽和高，以及计算模式
        val sizeWidth = MeasureSpec.getSize(widthMeasureSpec)
        val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        //计算出所有的childView的宽和高
        measureChildren(widthMeasureSpec, heightMeasureSpec)

        /**
         * 如果是wrap_content，需要计算宽高
         */
        var wrapWidth = 0
        var wrapHeight = 0

        val childSize = childCount
        var childWidth = 0
        var childHeight = 0
        var childParams : MarginLayoutParams? = null

        //用于计算左边两个childView的高度
        var leftHeight = 0
        //用户计算右边两个childView的高度，最终高度取二者之间大值
        var rightHeight = 0

        //用于计算上边两个childView的宽度
        var topWidth = 0
        //用于计算下边两个childView的宽度，最终宽度取二者之间大值
        var bottomWidth = 0

        /**
         * 根据childView计算出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是wrap_content时
         */
        for (index in 0 until childSize) {

            val childView = getChildAt(index)
            childWidth = childView.measuredWidth
            childHeight = childView.measuredHeight
            childParams = childView.layoutParams as MarginLayoutParams

            //上面的两个childView
            if(index == 0 || index == 1) {
                topWidth += childWidth + childParams.leftMargin + childParams.rightMargin
            }

            //下面的两个childView
            if(index == 2 || index == 3) {
                bottomWidth += childWidth + childParams.leftMargin + childParams.rightMargin
            }

            //----------横着累加宽度，竖着累加高度，然后取最大值即可-------------------

            //左边的两个childView
            if(index == 0 || index == 2) {
                leftHeight += childHeight + childParams.topMargin + childParams.bottomMargin
            }

            //右边的两个childView
            if(index == 1 || index == 3) {
                rightHeight += childHeight + childParams.topMargin + childParams.bottomMargin
            }
        }

        wrapWidth = max(topWidth, bottomWidth)
        wrapHeight = max(leftHeight, rightHeight)

        /**
         * 如果是wrap_content设置为我们计算的宽高
         * 否则：直接设置父容器计算的值
         */
        setMeasuredDimension(if(widthMode == MeasureSpec.EXACTLY) sizeWidth else wrapWidth,
            if(heightMode == MeasureSpec.EXACTLY) sizeHeight else wrapHeight)

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        val childSize = childCount
        var childWidth = 0
        var childHeight = 0
        var childParams : MarginLayoutParams? = null


        /**
         * 遍历所有childView 根据其宽高，以及margin进行布局
         */
        for (index in 0 until childSize) {
            var childView = getChildAt(index)
            childWidth = childView.measuredWidth
            childHeight = childView.measuredHeight
            childParams = childView.layoutParams as MarginLayoutParams

            /**
             * 子view摆放对应的四个顶点为止
             */
            var childLeft= 0
            var childTop = 0
            var childRight = 0
            var childBottom = 0

            /**
             * 四个控件，只需计算对应的左上两个顶点即可，右下可用根据控件宽高去累加一次即可
             */
            when(index) {

                0 -> {
                    childLeft = childParams.leftMargin
                    childTop = childParams.topMargin
                }
                1 -> {
                    childLeft = width - childWidth - childParams.rightMargin - childParams.leftMargin
                    childTop = childParams.topMargin
                }
                2 -> {
                    childLeft = childParams.leftMargin
                    childTop = height - childHeight - childParams.bottomMargin
                }
                3 -> {
                    childLeft = width - childWidth - childParams.rightMargin - childParams.leftMargin
                    childTop = height - childHeight - childParams.bottomMargin
                }

            }

            childRight = childLeft + childWidth
            childBottom = childTop + childHeight

            childView.layout(childLeft, childTop, childRight, childBottom)

        }

    }
}