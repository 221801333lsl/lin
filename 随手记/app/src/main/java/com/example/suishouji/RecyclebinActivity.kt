package com.example.suishouji

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


class RecyclebinActivity: AppCompatActivity(){
    //用来保存选中的position
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.suishouji.R.layout.recyclebinactivity)
        var imageButton1: ImageButton=findViewById(com.example.suishouji.R.id.backbutton)
        imageButton1.setOnClickListener {
            finish()
        }
    }
}