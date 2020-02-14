package com.example.suishouji

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi

class TimechooseActivity: Activity() {
    var year:Int = 0
    var month:Int = 0
    var day:Int = 0
    var hour:Int = 0
    var minute:Int = 0
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timechooseactivity)
        var intent=getIntent()
        var bundle=intent.extras
        var title=bundle?.getString("tosettitle")
        var content=bundle?.getString("tosetcontent")
        var cancel:TextView=findViewById(R.id.cancelset)
        cancel.setOnClickListener {
            finish()
        }
        var set:TextView=findViewById(R.id.settime)
        set.setOnClickListener {
            var caleEnd: Calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Calendar.getInstance()
            } else {
                TODO("VERSION.SDK_INT < N")
            };
            caleEnd.set(year, month, day, hour, minute, 0)
            var timeEnd = caleEnd.getTime().getTime();
            var aaaa:CalendarEventUtil =CalendarEventUtil()
            aaaa.addCalendarEvent(this,title,content,System.currentTimeMillis())
            finish()
        }
        var timepicker:TimePicker=findViewById(R.id.timepicker)
        timepicker.setIs24HourView(true)
        timepicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            hour=hourOfDay
            this.minute=minute
        }
        var datepicker: DatePicker =findViewById(R.id.datepicker)
        var calendar: Calendar = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar.getInstance()
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        year= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar.get(Calendar.YEAR)
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        month= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar.get(Calendar.MONTH)
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        day= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            calendar.get(Calendar.DAY_OF_MONTH)
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        datepicker.init(year,month,day, DatePicker.OnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            this.year=year
            this.month=monthOfYear
            this.day=dayOfMonth
        })
    }
}