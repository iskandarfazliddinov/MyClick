package com.example.myclick

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.course.db.AppDatabaseCheck
import com.example.myclick.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var appDatabaseCheck: AppDatabaseCheck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appDatabaseCheck = AppDatabaseCheck.getInstance(this)

        if (appDatabaseCheck.userDaos().getAll().isNotEmpty()) {

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }


    }
}
