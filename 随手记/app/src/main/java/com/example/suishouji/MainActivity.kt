package com.example.suishouji

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.mainactivity.*
import kotlinx.android.synthetic.main.newactivity.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)
        var imageButton1: ImageButton=findViewById(R.id.newButton)
        var imageButton2: ImageButton=findViewById(R.id.recyclebinbutton)
        var edittext: EditText=findViewById(R.id.search)
        imageButton1.setOnClickListener {
            var intent1=Intent()
            intent1.setClass(this,NewActivity::class.java)
            startActivity(intent1)
        }
        imageButton2.setOnClickListener {
            var intent2=Intent()
            intent2.setClass(this,RecyclebinActivity::class.java)
            startActivity(intent2)
        }
        init(this)
    }


    override fun onResume() {
        super.onResume()
        init(this)
    }

    private fun init(cxt:Context){
        ssjrecycleview.layoutManager= LinearLayoutManager(this)
        var lists=ArrayList<Noteitem>()
        var adapter:NoteAdapter=NoteAdapter(this,lists)
        ssjrecycleview.setAdapter(adapter)
    }
}