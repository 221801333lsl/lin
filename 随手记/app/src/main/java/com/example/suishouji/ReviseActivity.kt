package com.example.suishouji

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.sqlite.SQLiteDatabase
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.insert
import java.util.*

class ReviseActivity: Activity() {
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.resiveactivity)
        var intent=getIntent()
        var bundle=intent.extras
        var time:TextView=findViewById(R.id.resivetime)
        var title:EditText=findViewById(R.id.resivetitle)
        var content:EditText=findViewById(R.id.resivecontent)
        title.setText(bundle?.getString("toresivetitle"))
        time.setText(bundle?.getString("toresivetime"))
        content.setText(bundle?.getString("toresivecontent"))
        //保存或退出
        var imageButton1: ImageButton =findViewById(R.id.cancelresive)
        var imageButton2: ImageButton =findViewById(R.id.saveresive)
        imageButton1.setOnClickListener {
            var alertDialog:AlertDialog=AlertDialog.Builder(this).create()
            alertDialog.setMessage("是否退出修改？")
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
            var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this);
            var db: SQLiteDatabase =myDatabaseHelper.getWritableDatabase();
            var values: ContentValues = ContentValues()

            //获得当前时间
            var gettime= SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
            var date: Date = Date(System.currentTimeMillis())
            var resivedtime= gettime.format(date).toString()

            var resivedtitle:String=title.text.toString()
            var resivedcontent:String=content.text.toString()
            values.put("title", resivedtitle)
            values.put("time", resivedtime)
            values.put("content", resivedcontent)
            var array:Array<String>
            array= arrayOf(time.text.toString())
            db.update("ssj", values, "time=?", array)
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}