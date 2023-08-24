package com.grihshobha.delhipress_grih.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.grihshobha.delhipress_grih.utils.SessionManagement
import android.os.Bundle
import com.grihshobha.delhipress_grih.R
import android.content.Intent
import com.grihshobha.delhipress_grih.activity.home.TestActivityConcent
import com.grihshobha.delhipress_grih.activity.login.LoginScreen

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        val sessionManagement = SessionManagement(this)
        if (sessionManagement.concentUserDetail != null) {
            val i = Intent(this, TestActivityConcent::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
            finish()
        } else {
            val i = Intent(this, LoginScreen::class.java)
            startActivity(i)
            finish()
        }
    }
}