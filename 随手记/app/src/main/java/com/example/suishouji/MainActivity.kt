package com.example.suishouji

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.mainactivity.*
import kotlinx.android.synthetic.main.newactivity.*
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable
import org.jetbrains.anko.db.insert
import java.util.regex.Matcher
import java.util.regex.Pattern


class MainActivity: AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainactivity)
        var imageButton1: ImageButton=findViewById(R.id.newButton)
        var imageButton2: ImageButton=findViewById(R.id.recyclebinbutton)
        var edittext: EditText=findViewById(R.id.search)
        imageButton1.setOnClickListener {
            var intent1=Intent()
            intent1.setClass(this,NewActivity::class.java)
            startActivity(intent1)
        }
        imageButton2.setOnClickListener {
            var intent2=Intent()
            intent2.setClass(this,RecyclebinActivity::class.java)
            startActivity(intent2)
        }
        edittext.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP ) {
                //查询
                var key:String=edittext.text.toString()
                val p: Pattern = Pattern.compile("\\s*|\t|\r|\n")
                val m: Matcher = p.matcher(key)
                key = m.replaceAll("")
                var intent=Intent()
                var bundle=Bundle()
                intent.setClass(this,ReatchActivity::class.java)
                if(key!=null){
                    bundle.putCharSequence("key",key)
                    intent.putExtras(bundle)
                    startActivity(intent)
                }
                edittext.setText(null)
                return@OnKeyListener true
            }
            false
        })
        init(this)
    }


    override fun onResume() {
        super.onResume()
        init(this)
    }

    private fun init(cxt:Context){
        ssjrecycleview.layoutManager= LinearLayoutManager(this)
        var lists=ArrayList<Noteitem>()
        var myDatabaseHelper:SSJOpenHelper =SSJOpenHelper(this);
        var db:SQLiteDatabase=myDatabaseHelper.getWritableDatabase();
        var columns:Array<String>
        columns= arrayOf("title","time","content")
        var cursor: Cursor? = db.query("ssj",
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
        adapter.setOnItemClickListener(object:NoteAdapter.OnItemClickListener{
            override fun onItemClick(view: View, position: Int):Boolean {
                var intent=Intent()
                var bundle=Bundle()
                intent.setClass(cxt,ReviseActivity::class.java)
                bundle.putCharSequence("toresivetitle",adapter.Notebeans[position].title)
                bundle.putCharSequence("toresivetime", adapter.Notebeans[position].time)
                bundle.putCharSequence("toresivecontent",adapter.Notebeans[position].content)
                intent.putExtras(bundle)
                startActivity(intent)
                return true
            }
        })
        adapter.setOnLongClickListener(object:NoteAdapter.OnLongClickListener{
            @RequiresApi(Build.VERSION_CODES.N)
            override fun onLongClick(view: View, position: Int):Boolean {
                var aweq:String="3"
                var alertDialog: AlertDialog = AlertDialog.Builder(cxt).create()
                alertDialog.setMessage("您需要的操作是：")
                alertDialog.setButton(
                    DialogInterface.BUTTON_NEGATIVE,"删除",
                    DialogInterface.OnClickListener { dialog, which ->
                    })
                alertDialog.setButton(
                    DialogInterface.BUTTON_POSITIVE,"提醒",
                    DialogInterface.OnClickListener { dialog, which ->
                    })
                alertDialog.show()
                return true
            }
        })
        ssjrecycleview.setAdapter(adapter)
    }
}