package com.example.suishouji

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable


class RecyclebinActivity: AppCompatActivity(){
    //用来保存选中的position
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.suishouji.R.layout.recyclebinactivity)
        var imageButton1: ImageButton=findViewById(com.example.suishouji.R.id.backbutton)
        imageButton1.setOnClickListener {
            finish()
        }

        var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this)
        var db: SQLiteDatabase =myDatabaseHelper.getWritableDatabase()
        db?.createTable("rb",true,
            "title" to TEXT,
            "time" to TEXT,
            "content" to TEXT
        )
    }
}