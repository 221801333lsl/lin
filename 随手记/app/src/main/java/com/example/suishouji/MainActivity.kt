package com.example.suishouji

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


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
    }

}