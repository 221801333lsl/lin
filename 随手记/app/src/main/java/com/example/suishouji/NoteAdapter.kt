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
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var notetitle:TextView=itemView.findViewById(R.id.showtitle)
        var notetime:TextView=itemView.findViewById(R.id.showtime)
        var notecontent:TextView=itemView.findViewById(R.id.showcontent)
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
    }
}