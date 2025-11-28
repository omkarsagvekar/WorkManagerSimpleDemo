package com.example.kotlinworkmanagerexample

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlin.math.log

class ExampleWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override suspend fun doWork(): Result {
        showNotification("Download resumed", "This notification repeats every 15 minutes!")

        val inputText = inputData.getString("INPUT") ?: "No input"

        Log.d("ExampleWorker", "Worker running… Input = $inputText")

        // Return output
        val output = workDataOf("OUTPUT" to "Work finished successfully!")
        return Result.success(output)
    }


    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun showNotification(title: String, message: String) {
        val channelId = "work_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "WorkManager Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            applicationContext.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notificationId = System.currentTimeMillis().toInt()
        Log.d("TESTING", "showNotification notificationId: "+notificationId.toString())

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_menu_sort_by_size)
            .build()

        NotificationManagerCompat.from(applicationContext)
            .notify(notificationId, notification)
    }
}
