package com.example.kotlinworkmanagerexample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.util.UUID

class WorkViewModel : ViewModel() {

    private val workManager = WorkManager.getInstance()

    private val _workResult = MutableLiveData<String>()
    val workResult: LiveData<String> = _workResult

    var workId: UUID? = null

    fun startWork() {
        val input = workDataOf("INPUT" to "Hello Worker!")

        val request = OneTimeWorkRequestBuilder<ExampleWorker>()
            .setInputData(input)
            .build()

        workId = request.id

        workManager.enqueue(request)

        // Observe worker state
        workManager.getWorkInfoByIdLiveData(request.id)
            .observeForever { info ->
                if (info != null && info.state.isFinished) {
                    val output = info.outputData.getString("OUTPUT")
                    _workResult.postValue(output ?: "No output")
                }
            }
    }
}
