package com.aab.locationreminder.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aab.locationreminder.R
import com.aab.locationreminder.databinding.ActivityRemiderBinding

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRemiderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRemiderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}