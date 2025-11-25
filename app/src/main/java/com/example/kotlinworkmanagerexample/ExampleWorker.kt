package com.example.kotlinworkmanagerexample

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf

class ExampleWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val inputText = inputData.getString("INPUT") ?: "No input"

        Log.d("ExampleWorker", "Worker running… Input = $inputText")

        // Return output
        val output = workDataOf("OUTPUT" to "Work finished successfully!")
        return Result.success(output)
    }
}
