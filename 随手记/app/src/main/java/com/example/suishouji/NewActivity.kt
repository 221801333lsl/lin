package com.example.suishouji

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.jetbrains.anko.alert
import org.jetbrains.anko.db.insert
import java.util.*
import android.location.LocationListener as LocationListener1

class NewActivity: Activity() {
    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("WrongViewCast", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newactivity)
        var time:TextView=findViewById(R.id.time)
        var title:EditText=findViewById(R.id.title)
        var content:EditText=findViewById(R.id.content)
        var location1:TextView=findViewById(R.id.location)

        //获得当前时间
        var gettime= SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        var date: Date = Date(System.currentTimeMillis())
        time.setText(gettime.format(date))

        //获取位置信息
        var location=getLocation(this)
        var longitude:String="2"
        var latitude:String="2"
        if(location!=null)
        {
            longitude=location.longitude.toString()
            latitude=location.latitude.toString()
        }
        location1.setText("经度："+longitude+" 纬度："+latitude)

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
            database.use{
                insert("ssj",
                    "title" to title.text.toString(),
                    "time" to time.text.toString(),
                    "content" to content.text.toString(),
                    "location" to location1.text.toString()
                )
            }
            Toast.makeText(this,"保存成功",Toast.LENGTH_SHORT).show()
            finish()
        }
    }
    private fun getLocation(context: Context): Location? {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val checkCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val checkCallPhonePermission =ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED || checkCameraPermission != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null) {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        return location
    }
}