package com.example.juschedule.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.juschedule.R
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var uid:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2)
        var logo =findViewById<ImageView>(R.id.logo)

        logo.animate()
            .alpha(1f)
            .duration=2000


        auth = FirebaseAuth.getInstance()
        uid=auth.currentUser?.uid.toString()
        supportActionBar?.hide()
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            if(uid.isNotEmpty()){
            val intent =Intent(this, MainActivity::class.java)
            startActivity(intent)
                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right)

                finish()}
            else{ val intent =Intent(this, DashboardActivity::class.java)
                startActivity(intent)

            }
        },2000)
    }
}