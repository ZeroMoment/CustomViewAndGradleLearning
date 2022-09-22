package cn.lzm.stu.view.and.gradle.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.lzm.stu.view.and.gradle.R
import cn.lzm.stu.view.and.gradle.interf.ICommonRecyclerItemClickListener

/**
 * @des: 主页列表适配器
 * @author: lizhiming
 * @date: 2022/9/22 17:22
 */
class MainAdapter(context: Context) : RecyclerView.Adapter<MainHolder>() {

    private val mContext = context

    private var mItemList: List<String>? = null
    private var mItemClickListener: ICommonRecyclerItemClickListener? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setNewData(inList: List<String>, listener : ICommonRecyclerItemClickListener) {
        this.mItemList = inList
        this.mItemClickListener = listener
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.item_main_list, parent, false)
        return MainHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        mItemList?.let {
            val itemStr = it[position]
            holder.itemTv.text = itemStr

            holder.itemView.setOnClickListener {
                mItemClickListener?.onItemClick(itemStr, position)
            }
        }
    }

    override fun getItemCount(): Int {
        if(mItemList == null) return 0
        return mItemList!!.size
    }

}

class MainHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val itemTv: TextView = itemView.findViewById(R.id.item_main_tv)

}