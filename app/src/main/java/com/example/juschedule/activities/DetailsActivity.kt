package com.example.juschedule.activities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.juschedule.R


class DetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        val title=intent.getStringExtra("title")
        val description=intent.getStringExtra("description")
        val year=intent.getStringExtra("year")
        val hourNum=intent.getStringExtra("hourNum").toString()
        val next=intent.getStringExtra("next")
        val previous=intent.getStringExtra("previous")
        var CourseType=intent.getStringExtra("CourseType")


        var t=findViewById<TextView>(R.id.title)
        var d=findViewById<TextView>(R.id.description)
        var y=findViewById<TextView>(R.id.year)
        var h=findViewById<TextView>(R.id.hourNum)
        var n=findViewById<TextView>(R.id.next)
        var p=findViewById<TextView>(R.id.previous)
        var c=findViewById<TextView>(R.id.CourseType)
        var Prerequisite=findViewById<TextView>(R.id.Prerequisite)
        var nextName=findViewById<TextView>(R.id.nextName)

        t.text=title
        d.text=description
        y.text=year
        h.text=hourNum
        n.text=next
        p.text=previous
        c.text=CourseType

        if (previous == "") {
            Prerequisite.visibility=View.GONE
        }
        if (next =="") {
            nextName.visibility=View.GONE
        }

        var arrowBack=findViewById<LinearLayout>(R.id.arrow_back)

        arrowBack.setOnClickListener {
            finish()
        }
        d.setOnClickListener {
            setClipboard(this, d.text.toString())

        }
    }
    private fun setClipboard(context: Context, text: String) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            val clipboard=
                context.getSystemService(CLIPBOARD_SERVICE) as android.text.ClipboardManager
            clipboard.text=text
        } else {
            val clipboard=context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clip=ClipData.newPlainText("Copied Text", text)
            clipboard.setPrimaryClip(clip)
        }
        Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()
    }
}

