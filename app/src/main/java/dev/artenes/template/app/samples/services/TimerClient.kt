package dev.artenes.template.app.samples.services

import android.os.IBinder
import dev.artenes.template.android.AndroidServiceConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class TimerClient(
    private val serviceConnection: AndroidServiceConnection,
    private val scope: CoroutineScope
) : AndroidServiceConnection.ConnectionState {

    private var countdownTimer: TimerWithNotificationAndSoundModel? = null
    private val _timer = MutableStateFlow<Int?>(null)
    private var lastRequest = -1

    fun start(timeInSeconds: Int) {
        if (countdownTimer == null) {
            lastRequest = timeInSeconds
            serviceConnection.bind(TimerService::class.java, this)
            return
        }
        countdownTimer?.start(timeInSeconds)
    }

    fun stop() {
        countdownTimer?.stop()
    }

    fun timer(): Flow<Int?> = _timer

    fun isRunning() = countdownTimer?.isRunning()

    fun cleanUp() {
        serviceConnection.unbind()
    }

    override fun onConnected(binder: IBinder) {
        Timber.d("Start")
        countdownTimer = (binder as TimerService.LocalBinder).getTimer()
        countdownTimer?.start(lastRequest)
        lastRequest = -1
        scope.launch {
            countdownTimer?.timer()?.collectLatest {
                _timer.value = it
            }
        }
    }

    override fun onDisconnected() {
        countdownTimer = null
    }
}