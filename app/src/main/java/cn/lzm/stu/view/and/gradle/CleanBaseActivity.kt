package cn.lzm.stu.view.and.gradle

/**
 * @des:
 * @author: lizhiming
 * @date: 2022/10/24 17:58
 */
abstract class CleanBaseActivity : BaseActivity() {

    abstract fun getLayoutId() : Int

    override fun getLayoutResource(): Int {
        return getLayoutId()
    }

    override fun initView() {
    }

    override fun initData() {
    }
}