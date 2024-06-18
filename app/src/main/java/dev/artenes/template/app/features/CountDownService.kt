package dev.artenes.template.app.features

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@AndroidEntryPoint
class CountDownService : Service() {

    @Inject
    lateinit var scope: CoroutineScope

    private val running = AtomicBoolean(false)

    private val _counter = MutableStateFlow(Timer(0, Timer.State.STOPPED))
    val counter: StateFlow<Timer> = _counter

    override fun onCreate() {
        super.onCreate()
        Timber.d("Created")
    }

    fun start(seconds: Int) {
        Timber.d("Started")
        running.set(true)
        scope.launch {
            for (count in seconds downTo 0) {
                if (!running.get()) {
                    _counter.value = _counter.value.copy(state = Timer.State.PAUSED)
                    return@launch
                }
                _counter.value = Timer(count, Timer.State.COUNTING)
                delay(1000L)
                Timber.d("Counting $count")
            }
        }
    }

    fun stop() {
        Timber.d("Stopped")
        running.set(false)
        _counter.value = Timer(0, Timer.State.STOPPED)
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("Destroyed")
    }

    override fun onBind(intent: Intent?): IBinder {
        return LocalBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    inner class LocalBinder : Binder() {

        fun getService(): CountDownService = this@CountDownService

    }

}