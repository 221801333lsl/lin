package com.example.suishouji

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
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
        var location1:TextView=findViewById(R.id.newlocation)

        //获得当前时间
        var gettime= SimpleDateFormat("yyyy.MM.dd HH:mm:ss")
        var date: Date = Date(System.currentTimeMillis())
        time.setText(gettime.format(date))

        //获取位置信息

        var location=getLocation(this)
        var longitude:String=" "
        var latitude:String=" "
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
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(Build.VERSION_CODES.M)
    private fun getLocation(context: Context): Location? {
        //如果手机的SDK版本使用新的权限模型，检查是否获得了位置权限，如果没有就申请位置权限，如果有权限就刷新位置
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val checkCameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val checkCallPhonePermission =ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED || checkCameraPermission != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION)
            }
        }
        var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (location == null) {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED
        }
        else if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

        }
        else {
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
        }
        return location
    }

    //定义一个权限COde，用来识别Location权限
    private val LOCATION_PERMISSION = 1

    //使用匿名内部类创建了LocationListener的实例
    val locationListener = object : android.location.LocationListener {
        override fun onProviderDisabled(provider: String?) {
        }

        override fun onProviderEnabled(provider: String?) {
        }

        override fun onLocationChanged(location: Location?) {
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}