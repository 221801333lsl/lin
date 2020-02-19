package com.example.suishouji

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.recyclebinactivity.*
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.insert


class RecyclebinActivity: AppCompatActivity(){
    //用来保存选中的position
    private var list: Array<Int>? = null
    //是否显示ｃｈｅｃｋｂｏｘ
    private var isShowCheck = false
    //记录选中的ｃｈｅｃｋｂｏｘ
    private var checkList: List<String>? = ArrayList()
    private lateinit var btnDel: TextView
    private lateinit var btnSelectAll: TextView
    private lateinit var btnBack: TextView
    private lateinit var del_back:LinearLayout
    lateinit var adapter:DelectAdapter


    var aweq:String="3"
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.suishouji.R.layout.recyclebinactivity)
        del_back=findViewById(com.example.suishouji.R.id.del_back)
        btnDel =  findViewById(com.example.suishouji.R.id.btn_del)
        btnSelectAll =  findViewById(com.example.suishouji.R.id.btn_select_all)
        btnBack = findViewById(com.example.suishouji.R.id.btn_back)

        btnDel.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                delSelections()
            }
        }
        btnBack.setOnClickListener{
            backSelections()
        }
        btnSelectAll.setOnClickListener{
            var alertDialog: AlertDialog = AlertDialog.Builder(this).create()
            alertDialog.setMessage("您需要的操作是：")
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"还原",
                DialogInterface.OnClickListener { dialog, which ->
                    var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this)
                    var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase()
                    var columns:Array<String>
                    columns= arrayOf("title","time","content","location")
                    var cursor: Cursor? = db.query("rb",
                        columns,null,null,null,null,"time desc")
                    if (cursor != null) {
                        if(cursor.moveToNext()) {
                            do {
                                var gettitle:String?=cursor.getString(cursor.getColumnIndex("title"))
                                var gettime:String?=cursor.getString(cursor.getColumnIndex("time"))
                                var getcontent:String?=cursor.getString(cursor.getColumnIndex("content"))
                                var getlocation:String?=cursor.getString(cursor.getColumnIndex("location"))
                                database.use {
                                    insert(
                                        "ssj",
                                        "title" to gettitle,
                                        "time" to gettime,
                                        "content" to getcontent,
                                        "location" to getlocation
                                    )
                                }
                            }while (cursor.moveToNext())
                        }
                    }
                    db?.dropTable("rb", true)
                    Toast.makeText(this,"还原成功", Toast.LENGTH_SHORT).show()
                    finish()
                })
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"移除",
                DialogInterface.OnClickListener { dialog, which ->
                    var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this);
                    var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase();
                    db?.dropTable("rb", true)
                    Toast.makeText(this,"移除成功", Toast.LENGTH_SHORT).show()
                    finish()
                })
            alertDialog.show()
        }

        var imageButton1: ImageButton=findViewById(com.example.suishouji.R.id.backbutton)
        imageButton1.setOnClickListener {
            finish()
        }
        init(this)
    }

    override fun onResume() {
        super.onResume()
        init(this)
    }

    private fun init(cxt:Context){
        hszrecycleview.layoutManager= LinearLayoutManager(this)
        var lists=ArrayList<Noteitem>()
        var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this)
        var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase()
        var columns:Array<String>
        columns= arrayOf("title","time","content","location")
        var cursor: Cursor? = db.query("rb",
            columns,null,null,null,null,"time desc")
        if (cursor != null) {
            if(cursor.moveToNext()) {
                do {
                    var gettitle:String?=cursor.getString(cursor.getColumnIndex("title"))
                    var gettime:String?=cursor.getString(cursor.getColumnIndex("time"))
                    var getcontent:String?=cursor.getString(cursor.getColumnIndex("content"))
                    var getlocation:String?=cursor.getString(cursor.getColumnIndex("location"))
                    var bean = Noteitem()
                    with(bean)
                    {
                        title = gettitle
                        time = gettime
                        if (getcontent?.length!! > 32) {
                            content = getcontent.substring(0, 32) + "..."
                        } else {
                            content = getcontent
                        }
                        location=getlocation
                    }
                    lists.add(bean)
                }while (cursor.moveToNext())
            }
        }
        adapter=DelectAdapter(this,lists)
        adapter.setOnItemClickListener(object:DelectAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int):Boolean {
                if(checkList?.contains(position.toString())!!){
                    checkList = checkList?.minus(position.toString())
                }
                else{
                    checkList = checkList?.plus(position.toString())
                }
                return true
            }
        })
        hszrecycleview.setAdapter(adapter)
    }
    //删除
    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun delSelections() {
        var alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage("您需要的操作是：")
        if (checkList ==null) {
            alertDialog.setMessage("当前未选中项目")
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"确认",
                DialogInterface.OnClickListener { dialog, which ->
                })
        } else {
            alertDialog.setMessage("确认移除所选项目？")
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"移除",
                DialogInterface.OnClickListener { dialog, which ->
                    for (i in checkList!!.indices) {
                        if(adapter.Notebeans[i].title !=null){
                            aweq=adapter.Notebeans[i].title.toString()
                        }
                        var delectarray:Array<String>?
                        delectarray= arrayOf(aweq)
                        database.use { delete("rb", "title" + " = ?",delectarray) }
                        Toast.makeText(this,"移除成功", Toast.LENGTH_SHORT).show()
                    }
                    onResume()
                })
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消",
                DialogInterface.OnClickListener { dialog, which ->
                })
        }
        alertDialog.show()
    }

    private fun backSelections() {
        var alertDialog: AlertDialog = AlertDialog.Builder(this).create()
        alertDialog.setMessage("您需要的操作是：")
        if (checkList ==null) {
            alertDialog.setMessage("当前未选中项目")
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"确认",
                DialogInterface.OnClickListener { dialog, which ->
                })
        } else {
            alertDialog.setMessage("确认还原所选项目？")
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"还原",
                DialogInterface.OnClickListener { dialog, which ->
                    for (i in checkList!!.indices) {
                        database.use {
                            insert(
                                "ssj",
                                "title" to adapter.Notebeans[i].title.toString(),
                                "time" to adapter.Notebeans[i].time.toString(),
                                "content" to adapter.Notebeans[i].content.toString(),
                                "location" to adapter.Notebeans[i].location.toString()
                            )
                        }
                        if(adapter.Notebeans[i].title !=null){
                            aweq=adapter.Notebeans[i].title.toString()
                        }
                        var delectarray:Array<String>?
                        delectarray= arrayOf(aweq)
                        database.use { delete("rb", "title" + " = ?",delectarray) }
                        Toast.makeText(this,"还原成功", Toast.LENGTH_SHORT).show()
                    }
                    onResume()
                })
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE,"取消",
                DialogInterface.OnClickListener { dialog, which ->
                })
        }
        alertDialog.show()
    }
}