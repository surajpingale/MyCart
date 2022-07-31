package com.example.mycart.views.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import com.example.mycart.R
import com.example.mycart.application.MyCartApplication

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        val splashThread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(2000)
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    val appClass = (application as MyCartApplication)
                    val user = appClass.firebaseAuth.currentUser

                    if (user != null) {
                        val intent = Intent(
                            this@SplashActivity,
                            MainActivity::class.java
                        )
                        startActivity(intent)
                    } else {
                        val intent = Intent(
                            this@SplashActivity,
                            LoginActivity::class.java
                        )
                        startActivity(intent)
                    }
                    finish()
                }
            }
        }
        splashThread.start()
    }
}