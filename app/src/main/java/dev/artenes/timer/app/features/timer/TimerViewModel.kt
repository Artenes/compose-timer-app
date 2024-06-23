package dev.artenes.timer.app.features.timer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.artenes.timer.app.android.AndroidServiceConnection
import dev.artenes.timer.app.core.Timer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val serviceConnection: AndroidServiceConnection) :
    ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    init {
        viewModelScope.launch {
            getService().counter.collectLatest {
                onTick(it)
            }
        }
    }

    fun setMinutes(minutes: Int) {
        val state = _state.value
        val seconds = state.seconds
        val newTotalSeconds = (minutes * 60) + seconds
        _state.value = state.copy(
            minutes = minutes,
            totalSeconds = newTotalSeconds,
            startEnabled = newTotalSeconds > 0
        )
    }

    fun setSeconds(seconds: Int) {
        val state = _state.value
        val minutes = state.minutes
        val newTotalSeconds = (minutes * 60) + seconds
        _state.value = state.copy(
            seconds = seconds,
            totalSeconds = newTotalSeconds,
            startEnabled = newTotalSeconds > 0
        )
    }

    fun start() {
        viewModelScope.launch {
            getService().start(_state.value.totalSeconds)
        }
    }

    fun resume() {
        viewModelScope.launch {
            getService().resume()
        }
    }

    fun pause() {
        viewModelScope.launch {
            getService().pause()
        }
    }

    fun stop() {
        viewModelScope.launch {
            getService().stop()
        }
    }

    fun showNotification() {
        Timber.d("Show notification")
        viewModelScope.launch {
            getService().showNotification()
        }
    }

    fun hideNotification() {
        Timber.d("Hide notification")
        viewModelScope.launch {
            getService().hideNotification()
        }
    }

    private fun onTick(timer: Timer) {

        if (timer.state == Timer.State.STOPPED) {
            _state.value = _state.value.copy(
                totalSeconds = timer.initial,
                minutes = timer.initial / 60,
                seconds = timer.initial % 60,
                startVisible = true,
                resumeVisible = false,
                stopVisible = false,
                pauseVisible = false,
            )
            return
        }

        _state.value = _state.value.copy(
            totalSeconds = timer.seconds,
            minutes = timer.seconds / 60,
            seconds = timer.seconds % 60,
            startVisible = false,
            resumeVisible = timer.state == Timer.State.PAUSED,
            stopVisible = true,
            pauseVisible = timer.state == Timer.State.COUNTING,
        )

        Timber.v("View: ${timer.seconds}")

    }

    private suspend fun getService(): CountDownService {
        return (serviceConnection.startAndBind(CountDownService::class.java) as CountDownService.LocalBinder).getService()
    }

    override fun onCleared() {
        super.onCleared()
        serviceConnection.disconnect()
        Timber.d("Cleared")
    }

    data class State(
        val minutes: Int = 0,
        val seconds: Int = 0,
        val startEnabled: Boolean = false,
        val startVisible: Boolean = true,
        val resumeVisible: Boolean = false,
        val stopVisible: Boolean = false,
        val pauseVisible: Boolean = false,
        val totalSeconds: Int = 0,
        val lastSetTime: Int = 0
    )

}
