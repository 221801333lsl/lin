package com.example.suishouji

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import java.util.zip.Inflater

class NoteAdapter(private val context: Context, val Notebeans:List<Noteitem>):
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnLongClickListener: OnLongClickListener? = null
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var notetitle:TextView=itemView.findViewById(R.id.showtitle)
        var notetime:TextView=itemView.findViewById(R.id.showtime)
        var notecontent:TextView=itemView.findViewById(R.id.showcontent)
        var notelocation:TextView=itemView.findViewById(R.id.showlocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.noteitemview,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return Notebeans.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var list:Noteitem=Notebeans[position]
        holder.notetitle.setText(list.title)
        holder.notetime.setText(list.time)
        holder.notecontent.setText(list.content)
        holder.notelocation.setText(list.location)
        //判断是否设置了监听器
        if (mOnItemClickListener != null) {
            //为ItemView设置监听器-
            holder.itemView.setOnClickListener {
                val position1 = holder.layoutPosition
                mOnItemClickListener!!.onItemClick(holder.itemView, position1)
            }
        }
        if (mOnLongClickListener != null) {
            //为ItemView设置监听器-
            holder.itemView.setOnLongClickListener {
                val position1 = holder.layoutPosition
                mOnLongClickListener!!.onLongClick(holder.itemView, position1)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int):Boolean
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }
    interface OnLongClickListener{
        fun onLongClick(view: View, position: Int):Boolean
    }

    fun setOnLongClickListener(onLongClickListener: OnLongClickListener) {
        this.mOnLongClickListener = onLongClickListener
    }
}