package com.example.suishouji

import android.content.Context
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView


class DelectAdapter(private val context: Context, val Notebeans:List<Noteitem>):
    RecyclerView.Adapter<DelectAdapter.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null

    //控制是否显示Checkbox
    private var showCheckBox:Boolean=false
    fun isShowCheckBox(): Boolean {
        return showCheckBox
    }
    fun setShowCheckBox(showCheckBox: Boolean) {
        this.showCheckBox = showCheckBox
    }
    //防止Checkbox错乱 做setTag  getTag操作
    private var mCheckStates: SparseBooleanArray? = SparseBooleanArray()

    private var isState = false

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mCbItem: CheckBox = itemView.findViewById(com.example.suishouji.R.id.hsztitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(com.example.suishouji.R.layout.delectitemview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var list:Noteitem=Notebeans[position]
        holder.mCbItem.setTag(position)
        holder.mCbItem.setText(list.title)
        //判断是否设置了监听器
        //判断当前checkbox的状态
        mCheckStates?.get(position, false)?.let { holder.mCbItem.setChecked(it) };
        //对checkbox的监听 保存选择状态 防止checkbox显示错乱
        holder.mCbItem.setOnCheckedChangeListener { buttonView, isChecked ->
            var pos:Int = buttonView.getTag() as Int;
            if (isChecked) {
                mCheckStates?.put(pos, true);
            } else {
                mCheckStates?.delete(pos);
            }
        }
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器-
            holder.mCbItem.setOnClickListener {
                val position1 = holder.layoutPosition
                mOnItemClickListener!!.onItemClick(holder.itemView, position1)
            }
        }
    }

    override fun getItemCount(): Int {
        return Notebeans.size
    }

    fun setIsState(isState: Boolean) {
        this.isState = isState
        notifyDataSetChanged()
    }
    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int):Boolean
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }
}