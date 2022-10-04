package com.example.juschedule.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.juschedule.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(LayoutInflater.from(applicationContext))
        setContentView(binding.root)

        user=FirebaseAuth.getInstance()

        binding.forgotPassword.setOnClickListener {
            val intent=Intent(Intent.ACTION_VIEW)
            intent.data=Uri.parse("https://regweb2.ju.edu.jo:4443/selfregapp/home.xhtml")
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            LoginUser()
        }
        checkIfUserIsLogged()
    }

    private fun checkIfUserIsLogged() {
        if (user.currentUser != null) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }

    private fun LoginUser() {
        val email=binding.name.text.toString()
        val password=binding.pass.text.toString()

        if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(
                this,
                "please fill in your user name and your password ",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            user.signInWithEmailAndPassword(email, password).addOnCompleteListener { mTask ->
                if (mTask.isSuccessful) {
                    user.currentUser?.let { Log.e("TAG", it.uid) }
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this,
                        mTask.exception!!.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

