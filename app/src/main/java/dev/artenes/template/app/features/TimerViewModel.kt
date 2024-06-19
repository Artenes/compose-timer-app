package dev.artenes.template.app.features

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(private val serviceConnection: AndroidServiceConnection) :
    ViewModel() {

    private val _state = MutableStateFlow(State(seconds = "", paused = true, lastSetTime = "0"))
    val state: StateFlow<State> = _state

    init {
        viewModelScope.launch {
            getService().counter.collectLatest {
                onTick(it)
            }
        }
    }

    fun setTime(value: String) {
        _state.value = _state.value.copy(seconds = value, lastSetTime = value)
    }

    fun start() {
        viewModelScope.launch {
            getService().start(_state.value.seconds.toInt())
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
                seconds = timer.initial.toString(),
                paused = true
            )
            return
        }

        _state.value = _state.value.copy(
            seconds = timer.seconds.toString(),
            paused = timer.state != Timer.State.COUNTING
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
        val seconds: String,
        val paused: Boolean,
        val lastSetTime: String
    )

}
