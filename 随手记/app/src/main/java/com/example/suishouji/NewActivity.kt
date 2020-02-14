package com.example.suishouji

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class NewActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newactivity)
        var time:TextView=findViewById(R.id.time)
        var title:EditText=findViewById(R.id.title)
        var content:EditText=findViewById(R.id.content)
        var location1:TextView=findViewById(R.id.location)

        //保存或退出
        var imageButton1: ImageButton =findViewById(R.id.cancel)
        var imageButton2: ImageButton =findViewById(R.id.save)
        imageButton1.setOnClickListener {
            var alertDialog:AlertDialog=AlertDialog.Builder(this).create()
            alertDialog.setMessage("是否退出编辑？")
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"退出",
                DialogInterface.OnClickListener {dialog, which ->
                    finish()
                })
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"取消",
                DialogInterface.OnClickListener {dialog, which ->
                })
            alertDialog.show()
        }
        imageButton2.setOnClickListener {
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}