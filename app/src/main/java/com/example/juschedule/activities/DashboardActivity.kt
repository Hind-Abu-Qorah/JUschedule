package com.example.juschedule.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.juschedule.R
import com.example.juschedule.pojo.user
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference

class DashboardActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var dialog:Dialog
    private lateinit var user: user
    private lateinit var uid:String
    lateinit var uuid : TextView
    private lateinit var IDNum:TextView
    private lateinit var username:TextView
    private lateinit var Major:TextView
    private lateinit var passCredits:TextView
    private lateinit var currentlyCredits:TextView
    private lateinit var phoneNum:TextView
    private lateinit var id_nav:TextView
    private lateinit var name_nav:TextView
    private lateinit var status:TextView
    private lateinit var statusT:LinearLayout
    private lateinit var statusView:View
    private lateinit var profileImage: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        username =findViewById(R.id.username)
        IDNum =findViewById(R.id.IDNum)
        Major =findViewById(R.id.Major)
        passCredits =findViewById(R.id.passCredits)
        currentlyCredits =findViewById(R.id.currentlyCredits)
        phoneNum =findViewById(R.id.phoneNum)
        profileImage =findViewById(R.id.profile_image)
        status=findViewById(R.id.status)
        statusT=findViewById(R.id.statusT)
        statusView=findViewById(R.id.statusView)
        user=user()


        var menuIcon = findViewById<ImageView>(R.id.menu_icon)
        var drawer = findViewById<DrawerLayout>(R.id.drawer)
        var navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val headerView: View=navigationView.getHeaderView(0)
        id_nav =headerView.findViewById(R.id.id_nav)
        name_nav =headerView.findViewById(R.id.name_nav)

        auth = FirebaseAuth.getInstance()
        uid=auth.currentUser?.uid.toString()
        databaseReference=FirebaseDatabase.getInstance().getReference("users")
        @GlideModule
        class MyAppGlideModule : AppGlideModule() {
        }

        if(uid.isNotEmpty()){
            showProgressBar()
            getuserdata()

        }

        menuIcon.setOnClickListener {
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START)
            } else {
                drawer.openDrawer(GravityCompat.START)
            }
        }

        navigationView.setNavigationItemSelectedListener {
            drawer.closeDrawer(GravityCompat.START)
            when (it.itemId) {
                R.id.nav_dashboard -> {
                    drawer.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_courses -> {
                    startActivity(Intent(this , CoursesGroupActivity::class.java))
                    true
                }
                R.id.nav_completed->{
                    var intent=  Intent(this, CompletedCoursesActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_schedule ->{
                  var intent=  Intent(this, ScheduleActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_logout -> {
                    auth.signOut()
                    startActivity(Intent(this , MainActivity::class.java))
                    finish()
                    true
                }
            }
            false
        }


    }

    private fun getuserdata() {
        databaseReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                user = dataSnapshot.getValue(user::class.java)!!
                Glide.with(applicationContext)
                    .load(user.image)
                    .into(profileImage)
                IDNum.setText(user.uid)
                username.setText(user.uname)
                Major.setText(user.umajor)
                passCredits.setText(user.ufinished)
                currentlyCredits.setText(user.ucurrent)
                phoneNum.setText(user.UPHONE)
                name_nav.setText(user.uname)
                id_nav.setText(user.uid)
                status.setText(user.status)

                if(status.text =="" ){
                    status.visibility=View.GONE
                    statusView.visibility=View.GONE
                    statusT.visibility=View.GONE


                }
                if(user.status=="expected to graduate") {

                    status.setTextColor(resources.getColor(R.color.red))

                }
                hideProgressBar()

            }
            override fun onCancelled(error: DatabaseError) {
                hideProgressBar()
            }

        })
    }

    fun progressBar(){

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