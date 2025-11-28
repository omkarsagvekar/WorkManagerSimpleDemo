package com.example.kotlinworkmanagerexample

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.example.kotlinworkmanagerexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WorkViewModel
    private lateinit var binding: ActivityMainBinding

    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
            if (isGranted){
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED){
                requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        viewModel = ViewModelProvider(this)[WorkViewModel::class.java]

//        val btnStart = findViewById<Button>(R.id.btnStart)
//        val txtStatus = findViewById<TextView>(R.id.txtStatus)
//        val btnStartNotification = findViewById<Button>(R.id.btnStartNotification)

        binding.btnStart.setOnClickListener {
            viewModel.startWork()
        }

        binding.btnStartNotification.setOnClickListener {
            viewModel.startWorkManagerForPeriodicNotification()
        }

        viewModel.workResult.observe(this) { result ->
            binding.txtStatus.text = result
        }

        binding.btnStopWorkManager.setOnClickListener {
//            viewModel.workId?.let {
//                WorkManager.getInstance().cancelWorkById(it)
//            }
//
//            viewModel.workIdForPeridic?.let {
//                WorkManager.getInstance().cancelWorkById(it)
//            }
            WorkManager.getInstance(this).cancelAllWork()

        }
    }
}