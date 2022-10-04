package com.example.juschedule.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.juschedule.R
import com.example.juschedule.adapters.CompulsoryAdapter
import com.example.juschedule.pojo.Courses
import com.google.firebase.database.*

class CompulsorySpecializationActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var coursesList: java.util.ArrayList<Courses>
    lateinit var myRecyclerview: RecyclerView
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compulsory_specialization)

        var search=findViewById<SearchView>(R.id.search)

        myRecyclerview=findViewById(R.id.recyclerview)
        myRecyclerview.layoutManager=LinearLayoutManager(this)
        myRecyclerview.setHasFixedSize(true)

        coursesList=arrayListOf<Courses>()
        showProgressBar()
        getuserdata()


        var arrowBack=findViewById<LinearLayout>(R.id.arrow_back)
        arrowBack.setOnClickListener {
            finish()
        }
//search bar
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                search(newText)
                return true
            }
        })

    }

    private fun getuserdata() {
        databaseReference=FirebaseDatabase.getInstance().getReference("courses")
            .child("CompulsorySpecialization")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    Log.e("BATOOL", "onDataChange: " + snapshot )
                    if (snapshot.exists()) {
                        for (courssnapshot in snapshot.children) {
                            val course=courssnapshot.getValue(Courses::class.java)
                            coursesList.add(course!!)
                            Log.e("BATOOL ", " $course")
                        }
                        myRecyclerview.adapter=
                            CompulsoryAdapter(coursesList, this@CompulsorySpecializationActivity)
                    }
                    hideProgressBar()
                } catch (e: Exception) {
                    Log.e("BATOOL", "onDataChange: " + e)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e("BATOOL", "onCancelled: " + error)
            }

        })
    }

    private fun search(newText: String) {
        val searchGuides=ArrayList<Courses>()
        for (g in coursesList) {
            if (g.coursesName!!.toLowerCase().contains(newText.toLowerCase())
                || g.description!!.toLowerCase().contains(newText.toLowerCase())
            ) {
                searchGuides.add(g)
            }
        }
        val searchAdapter=CompulsoryAdapter(searchGuides, this)
        myRecyclerview.adapter=searchAdapter
    }

    fun onClickListener(course: Courses) {
        val intent=Intent(this, DetailsActivity::class.java)
        intent.putExtra("title", course.coursesName)
        intent.putExtra("description", course.description)
        intent.putExtra("year", course.year)
        intent.putExtra("hourNum", course.hourNum)
        intent.putExtra("next", course.nextCourses)
        intent.putExtra("previous", course.Prerequisite)
        intent.putExtra("CourseType", course.CourseType)
        startActivity(intent)
    }

    fun showProgressBar() {
        dialog=Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun hideProgressBar() {
        dialog.dismiss()
    }
}