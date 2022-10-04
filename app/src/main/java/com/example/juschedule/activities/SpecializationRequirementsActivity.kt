package com.example.juschedule.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.juschedule.R
import com.example.juschedule.adapters.SpecializationAdapter
import com.example.juschedule.pojo.Courses
import com.google.firebase.database.*

class SpecializationRequirementsActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    lateinit var coursesList : java.util.ArrayList<Courses>
    lateinit var myRecyclerview: RecyclerView
    private lateinit var dialog: Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major_courses)


        var search = findViewById<SearchView>(R.id.search)

        myRecyclerview = findViewById(R.id.recyclerview)
        myRecyclerview.layoutManager= LinearLayoutManager(this)
        myRecyclerview.setHasFixedSize(true)

        coursesList=arrayListOf<Courses>()
        getuserdata()


        var arrowBack = findViewById<LinearLayout>(R.id.arrow_back)
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
        databaseReference =FirebaseDatabase.getInstance().getReference("courses").child("SpecializationRequirements")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (courssnapshot in snapshot.children){
                        val course =courssnapshot.getValue(Courses::class.java)
                        coursesList.add(course!!)
                        Log.e("TAG"," amdc dm$course")
                    }
                    myRecyclerview.adapter = SpecializationAdapter(coursesList,this@SpecializationRequirementsActivity)

                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })    }

     fun onClickListener(item: Courses) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("title",item.coursesName)
        intent.putExtra("description",item.description)
         intent.putExtra("year",item.year)
         intent.putExtra("hourNum",item.hourNum)
         intent.putExtra("next",item.nextCourses)
         intent.putExtra("previous",item.Prerequisite)
         intent.putExtra("CourseType",item.CourseType)
         startActivity(intent)
    }


    private fun search(newText: String) {
        val searchGuides: java.util.ArrayList<Courses> = java.util.ArrayList<Courses>()
        for (g in coursesList) {
            if (g.coursesName!!.toLowerCase().contains(newText.toLowerCase())
        || g.description!!.toLowerCase().contains(newText.toLowerCase())
            ) {
                searchGuides.add(g)
            }
        }
        val searchAdapter = SpecializationAdapter(searchGuides ,this)
        myRecyclerview.adapter = searchAdapter
    }

}