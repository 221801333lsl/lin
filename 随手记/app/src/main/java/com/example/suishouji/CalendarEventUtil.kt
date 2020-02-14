package com.example.suishouji

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.icu.util.TimeZone
import android.net.Uri
import android.os.Build
import android.provider.CalendarContract
import android.text.TextUtils
import androidx.annotation.RequiresApi


public class CalendarEventUtil {


    private val CALANDER_URL = "content://com.android.calendar/calendars"
    private val CALANDER_EVENT_URL = "content://com.android.calendar/events"
    private val CALANDER_REMIDER_URL = "content://com.android.calendar/reminders"

    private val CALENDARS_NAME = "test"
    private val CALENDARS_ACCOUNT_NAME = " "
    private val CALENDARS_ACCOUNT_TYPE = "com.android.exchange"
    private val CALENDARS_DISPLAY_NAME = "我是本人 "

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.N)
    private fun addCalendarAccount(context: Context) {
        val timeZone = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            TimeZone.getDefault()
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val value = ContentValues()
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME)
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME)
        value.put(CalendarContract.Calendars.VISIBLE, 1)
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE)
        value.put(
            CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
            CalendarContract.Calendars.CAL_ACCESS_OWNER
        )
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1)
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.id)
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME)
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0)
        var calendarUri = Uri.parse(CALANDER_URL)
        calendarUri = calendarUri.buildUpon()
            .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
            .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
            .build()
        context.contentResolver.insert(calendarUri, value)
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.N)
    private fun checkCalendarAccount(context: Context): Int {
        var userCursor = context.contentResolver
            .query(Uri.parse(CALANDER_URL), null, null, null, null)
        return try {
            if (userCursor == null) //查询返回空值
                return -1
            while (userCursor.moveToNext()) {
                val nameColumnIndex =
                    userCursor.getColumnIndex(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME)
                if (userCursor.getString(nameColumnIndex) == CALENDARS_DISPLAY_NAME) {
                    return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID))
                }
            }
            addCalendarAccount(context)
            userCursor = context.contentResolver
                .query(Uri.parse(CALANDER_URL), null, null, null, null)
            userCursor!!.moveToLast()
            userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID))
        } finally {
            userCursor?.close()
        }
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(Build.VERSION_CODES.N)
    fun addCalendarEvent(
        context: Context,
        title: String?,
        description: String?,
        beginTime: Long
    ) { // 获取日历账户的id
        val calId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            checkCalendarAccount(context)
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        if (calId < 0) { // 获取账户id失败直接返回，添加日历事件失败
            return
        }
        val event = ContentValues()
        event.put("title", title)
        event.put("description", description)
        // 插入账户的id
        event.put("calendar_id", calId)
        event.put(CalendarContract.Events.DTSTART, beginTime)
        event.put(CalendarContract.Events.DTEND, beginTime + 10)
        event.put(CalendarContract.Events.HAS_ALARM, 1) //设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai") //这个是时区，必须有，
        //添加事件
        val newEvent =
            context.contentResolver.insert(Uri.parse(CALANDER_EVENT_URL), event)
                ?: // 添加日历事件失败直接返回
                return
        //事件提醒的设定
        val values = ContentValues()
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent))
        // 提前60分钟有提醒
        values.put(CalendarContract.Reminders.MINUTES, 1)
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT)
        val uri =
            context.contentResolver.insert(Uri.parse(CALANDER_REMIDER_URL), values)
                ?: // 添加闹钟提醒失败直接返回
                return
    }
}