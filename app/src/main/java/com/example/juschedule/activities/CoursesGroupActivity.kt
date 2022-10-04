package com.example.juschedule.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.example.juschedule.R

class CoursesGroupActivity : AppCompatActivity() {
   private lateinit var FacultyRequirement : CardView
    private lateinit var Compulsory : CardView
    private lateinit var Elective : CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_courses_group)


        FacultyRequirement = findViewById(R.id.FacltyRequirementsCard)
        Compulsory = findViewById(R.id.CompulsoryCard)
        Elective = findViewById(R.id.ElectiveCard)


        FacultyRequirement.setOnClickListener{
            startActivity(Intent(this, FacultyRequirementsActivity::class.java))
        }

        Compulsory.setOnClickListener{
            startActivity(Intent(this, CompulsorySpecializationActivity::class.java))
        }

        Elective.setOnClickListener{
            startActivity(Intent(this, SpecializationRequirementsActivity::class.java))
        }

        var arrowBack = findViewById<LinearLayout>(R.id.arrow_back)
        arrowBack.setOnClickListener {
            finish()
        }


        }
}
