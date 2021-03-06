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
        var location1:TextView=findViewById(R.id.resivelocation)
        title.setText(bundle?.getString("toresivetitle"))
        time.setText(bundle?.getString("toresivetime"))
        content.setText(bundle?.getString("toresivecontent"))
        location1.setText(bundle?.getString("toresivelocation"))
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

            //获取位置信息
            var location=getLocation(this)
            var longitude:String=" "
            var latitude:String=" "
            if(location!=null)
            {
                longitude=location.longitude.toString()
                latitude=location.latitude.toString()
            }
            var resivedlocation="经度："+longitude+" 纬度："+latitude

            var resivedtitle:String=title.text.toString()
            var resivedcontent:String=content.text.toString()
            values.put("title", resivedtitle)
            values.put("time", resivedtime)
            values.put("content", resivedcontent)
            values.put("location", resivedlocation)
            var array:Array<String>
            array= arrayOf(time.text.toString())
            db.update("ssj", values, "time=?", array)
            Toast.makeText(this,"修改成功",Toast.LENGTH_SHORT).show()
            finish()
        }
    }@SuppressLint("NewApi")
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