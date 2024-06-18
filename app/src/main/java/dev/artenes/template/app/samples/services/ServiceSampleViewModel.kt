package dev.artenes.template.app.samples.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.artenes.template.android.AndroidServiceConnection
import javax.inject.Inject

@HiltViewModel
class ServiceSampleViewModel @Inject constructor(serviceConnection: AndroidServiceConnection) : ViewModel() {

    private val timer = TimerClient(serviceConnection, viewModelScope)

    fun startTimer() {
        timer.start(10)
    }

    fun stopTimer() {
        timer.stop()
    }

    override fun onCleared() {
        timer.cleanUp()
    }

}