package dev.artenes.template.app.features

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class TimerViewModel @Inject constructor(@ApplicationContext private val context: Context) :
    ViewModel(),
    ServiceConnection {

    private val _state = MutableStateFlow(State(seconds = "", paused = true, lastSetTime = "0"))
    val state: StateFlow<State> = _state

    private var service: CountDownService? = null

    init {
        val intent = Intent(context, CountDownService::class.java)
        context.startService(intent)
        context.bindService(intent, this, Context.BIND_AUTO_CREATE)
        Timber.d("init")
    }

    fun setTime(value: String) {
        _state.value = _state.value.copy(seconds = value, lastSetTime = value)
    }

    fun start() {
        service?.start(_state.value.seconds.toInt())
    }

    fun stop() {
        service?.stop()
    }

    data class State(
        val seconds: String,
        val paused: Boolean,
        val lastSetTime: String
    )

    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
        Timber.d("Binded")
        this.service = (service as CountDownService.LocalBinder).getService()
        viewModelScope.launch {
            this@TimerViewModel.service!!.counter.collectLatest {

                if (it.state == Timer.State.STOPPED) {
                    _state.value = _state.value.copy(
                        seconds = _state.value.lastSetTime,
                        paused = true
                    )
                    return@collectLatest
                }

                _state.value = _state.value.copy(
                    seconds = it.seconds.toString(),
                    paused = it.state != Timer.State.COUNTING
                )
                Timber.d("View: ${it.seconds}")

            }
        }
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        Timber.d("Unbind")
        service = null
    }

    override fun onCleared() {
        context.unbindService(this)
        Timber.d("Cleared")
    }

}