package com.example.suishouji

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView

class SavedActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.savedactivity)
        var intent=getIntent()
        var bundle=intent.extras
        var time: TextView =findViewById(R.id.savedtime)
        var title: TextView =findViewById(R.id.savedtitle)
        var content: TextView =findViewById(R.id.savedcontent)
        var location:TextView =findViewById(R.id.savedlocation)
        title.setText(bundle?.getString("tosavedtitle"))
        time.setText(bundle?.getString("tosavedtime"))
        content.setText(bundle?.getString("tosavedcontent"))
        location.setText(bundle?.getString("tosavedlocation"))
    }
}