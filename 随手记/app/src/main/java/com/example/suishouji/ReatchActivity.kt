package com.example.suishouji

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.mainactivity.*
import kotlinx.android.synthetic.main.reatchactivity.*
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
            "content" to TEXT
        )
        var columns:Array<String>
        columns= arrayOf("title","time","content")
        var cursor: Cursor? = db.query("ssj",
            columns,null,null,null,null,"time desc")
        if (cursor != null) {
            if(cursor.moveToNext()) {
                do {
                    var gettitle: String = cursor.getString(cursor.getColumnIndex("title"));
                    var gettime: String = cursor.getString(cursor.getColumnIndex("time"));
                    var getcontent: String = cursor.getString(cursor.getColumnIndex("content"));
                    if (key?.let { gettime.indexOf(it) }!! >= 0 ||key?.let { gettitle.indexOf(it) }!! >= 0
                        ||key?.let { getcontent.indexOf(it) }!! >= 0) {
                        database.use {
                            insert(
                                "rt",
                                "title" to gettitle,
                                "time" to gettime,
                                "content" to getcontent
                            )
                        }
                    }
                } while (cursor.moveToNext());
            }
        }
        init(this)
    }

    private fun init(cxt:Context){
        cxrecycleview.layoutManager= LinearLayoutManager(this)
        var lists=ArrayList<Noteitem>()
        var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this);
        var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase();
        var columns:Array<String>
        columns= arrayOf("title","time","content")
        var cursor: Cursor? = db.query("rt",
            columns,null,null,null,null,"time desc")
        if (cursor != null) {
            if(cursor.moveToNext()) {
                do {
                    var gettitle:String=cursor.getString(cursor.getColumnIndex("title"));
                    var gettime:String=cursor.getString(cursor.getColumnIndex("time"));
                    var getcontent:String=cursor.getString(cursor.getColumnIndex("content"));
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
                    }
                    lists.add(bean)
                }while (cursor.moveToNext());
            }
        }
        var adapter:NoteAdapter=NoteAdapter(this,lists)
        cxrecycleview.setAdapter(adapter)
    }
}