package com.example.suishouji

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.os.FileObserver.DELETE
import android.provider.Contacts.SettingsColumns.KEY
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.mainactivity.*
import kotlinx.android.synthetic.main.reatchactivity.*
import kotlinx.android.synthetic.main.resiveactivity.*
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.insert


class ReatchActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reatchactivity)
        var textView1:TextView=findViewById(R.id.backmain)
        var textView2: TextView=findViewById(R.id.reatchsearch)
        var intent=getIntent()
        var bundle=intent.extras
        var key:String?=bundle?.getString("key")
        textView1.setOnClickListener {
            textView2.setText(null)
            var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this);
            var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase();
            db?.dropTable("rt", true)
            finish()
        }
        textView2.setText(key)
        var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this);
        var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase();
        db?.createTable("rt",true,
            "title" to TEXT,
            "time" to TEXT,
            "content" to TEXT,
            "location" to TEXT
        )
        var columns:Array<String>
        columns= arrayOf("title","time","content","location")
        var cursor: Cursor? = db.query("ssj",
            columns,null,null,null,null,"time desc")
        if (cursor != null) {
            if(cursor.moveToNext()) {
                do {
                    var gettitle: String = cursor.getString(cursor.getColumnIndex("title"));
                    var gettime: String = cursor.getString(cursor.getColumnIndex("time"));
                    var getcontent: String = cursor.getString(cursor.getColumnIndex("content"));
                    var getlocation: String = cursor.getString(cursor.getColumnIndex("location"));
                    if (key?.let { gettime.indexOf(it) }!! >= 0 ||key?.let { gettitle.indexOf(it) }!! >= 0
                        ||key?.let { getcontent.indexOf(it) }!! >= 0) {
                        database.use {
                            insert(
                                "rt",
                                "title" to gettitle,
                                "time" to gettime,
                                "content" to getcontent,
                                "location" to getlocation
                            )
                        }
                    }
                } while (cursor.moveToNext());
            }
        }
        init(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this);
        var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase();
        db?.dropTable("rt", true)
    }

    private fun init(cxt:Context){
        cxrecycleview.layoutManager= LinearLayoutManager(this)
        var lists=ArrayList<Noteitem>()
        var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this);
        var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase();
        var columns:Array<String>
        columns= arrayOf("title","time","content","location")
        var cursor: Cursor? = db.query("rt",
            columns,null,null,null,null,"time desc")
        if (cursor != null) {
            if(cursor.moveToNext()) {
                do {
                    var gettitle:String=cursor.getString(cursor.getColumnIndex("title"));
                    var gettime:String=cursor.getString(cursor.getColumnIndex("time"));
                    var getcontent:String=cursor.getString(cursor.getColumnIndex("content"));
                    var getlocation:String=cursor.getString(cursor.getColumnIndex("location"));
                    var bean = Noteitem()
                    with(bean)
                    {
                        title = gettitle
                        time = gettime
                        if (getcontent.length > 32) {
                            content = getcontent.substring(0, 32) + "..."
                        } else {
                            content = getcontent
                        }
                        location=getlocation
                    }
                    lists.add(bean)
                }while (cursor.moveToNext());
            }
        }
        var adapter:NoteAdapter=NoteAdapter(this,lists)
        adapter.setOnItemClickListener(object:NoteAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int):Boolean {
                var intent=Intent()
                var bundle=Bundle()
                intent.setClass(cxt,SavedActivity::class.java)
                bundle.putCharSequence("tosavedtitle",adapter.Notebeans[position].title)
                bundle.putCharSequence("tosavedtime", adapter.Notebeans[position].time)
                bundle.putCharSequence("tosavedcontent",adapter.Notebeans[position].content)
                bundle.putCharSequence("tosavedlocation",adapter.Notebeans[position].location)
                intent.putExtras(bundle)
                startActivity(intent)
                return true
            }
        })
        cxrecycleview.setAdapter(adapter)
    }
}