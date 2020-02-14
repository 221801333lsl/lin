package com.example.suishouji

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.ManagedSQLiteOpenHelper
import org.jetbrains.anko.db.TEXT
import org.jetbrains.anko.db.createTable
import org.jetbrains.anko.db.dropTable

class SSJOpenHelper(ctx: Context) :ManagedSQLiteOpenHelper(ctx, "MyDatabase", null, 1) {
    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "SSJDataBase"

        private var instance : SSJOpenHelper? = null

        @Synchronized
        fun getInstance(context: Context) : SSJOpenHelper{
            if(instance == null){
                instance = SSJOpenHelper(context.applicationContext)
            }
            return instance!!
        }
    }
    override fun onCreate(db: SQLiteDatabase?) {
        db?.createTable("ssj",true,
            "title" to TEXT,
            "time" to TEXT,
            "content" to TEXT,
            "location" to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}
var Context.database: SSJOpenHelper
    get() = SSJOpenHelper.getInstance(this)
    set(value) {}