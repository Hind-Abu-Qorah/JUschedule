package com.example.juschedule.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.juschedule.R
import com.example.juschedule.adapters.CompletedAdapter
import com.example.juschedule.adapters.facultyAdapter
import com.example.juschedule.pojo.Courses
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.ArrayList

class CompletedCoursesActivity : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference
    lateinit var myRecyclerview: RecyclerView
    private lateinit var dialog: Dialog
    private lateinit var auth: FirebaseAuth
    private lateinit var uid: String
    lateinit var FinishedList: ArrayList<Courses>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_corses)

        auth=FirebaseAuth.getInstance()
        uid=auth.currentUser?.uid.toString()

        var search = findViewById<SearchView>(R.id.search)

        myRecyclerview = findViewById(R.id.recyclerview)
        myRecyclerview.layoutManager= LinearLayoutManager(this)
        myRecyclerview.setHasFixedSize(true)

        FinishedList=arrayListOf<Courses>()
        showProgressBar()
        getFinishedCourses()
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

    private fun getFinishedCourses() {
        databaseReference=FirebaseDatabase.getInstance().getReference("userCourses")
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (courssnapshot in snapshot.children) {
                        val course=courssnapshot.getValue(Courses::class.java)
                        FinishedList.add(course!!)
                    }
                    myRecyclerview.adapter = CompletedAdapter(FinishedList,this@CompletedCoursesActivity)

                }
                hideProgressBar()
            }

            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
            }
        })
    }

    private fun search(newText: String) {
        val searchGuides: java.util.ArrayList<Courses> = java.util.ArrayList<Courses>()
        for (g in FinishedList) {
            if (g.coursesName!!.toLowerCase().contains(newText.toLowerCase())
                || g.description!!.toLowerCase().contains(newText.toLowerCase())
            ) {
                searchGuides.add(g)
            }
        }
        val searchAdapter = CompletedAdapter(searchGuides ,this)
        myRecyclerview.adapter = searchAdapter
    }
    fun onClickListener(course: Courses) {
        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra("title",course.coursesName)
        intent.putExtra("description",course.description)
        intent.putExtra("year",course.year)
        intent.putExtra("hourNum",course.hourNum)
        intent.putExtra("next",course.nextCourses)
        intent.putExtra("previous",course.Prerequisite)
        intent.putExtra("CourseType",course.CourseType)
        startActivity(intent)
    }
    fun showProgressBar() {
        dialog=Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }


    private fun hideProgressBar(){
        dialog.dismiss()
    }
}