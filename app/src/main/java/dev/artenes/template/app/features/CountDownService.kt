package dev.artenes.template.app.features

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class CountDownService : Service() {

    @Inject
    lateinit var scope: CoroutineScope

    private val _counter = MutableStateFlow(Timer())
    val counter: StateFlow<Timer> = _counter

    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        Timber.d("Created")
    }

    fun start(seconds: Int) {
        Timber.d("Started")
        job = scope.launch {
            for (count in seconds downTo 0) {
                _counter.value = Timer(count, Timer.State.COUNTING)
                delay(1000L)
                Timber.d("Counting $count")
            }
        }
    }

    fun stop() {
        Timber.d("Stopped")
        job?.cancel()
        _counter.value = Timer()
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