package dev.artenes.template.app.features

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.artenes.timer.R
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

    private var shouldShowNotification = false

    private val state: Timer.State
        get() = _counter.value.state

    private var job: Job? = null

    override fun onCreate() {
        super.onCreate()
        Timber.d("Created")
        createChannel()
    }

    @SuppressLint("MissingPermission")
    fun start(seconds: Int) {
        Timber.d("Started")
        job = scope.launch {
            count(seconds)
        }
    }

    fun resume() {
        job = scope.launch {
            count(_counter.value.seconds)
        }
    }

    fun pause() {
        Timber.d("Paused")
        job?.cancel()
        _counter.value = _counter.value.copy(state = Timer.State.PAUSED)
    }

    fun stop() {
        Timber.d("Stopped")
        job?.cancel()
        _counter.value = Timer()
    }

    fun showNotification() {
        if (state == Timer.State.STOPPED) {
            Timber.d("show notification STOPPED")
            return
        }

        val notification = buildNotification(getString(R.string.loading))

        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE else 0
        )

        shouldShowNotification = true

        Timber.d("Show notification")
    }

    fun hideNotification() {
        Timber.d("Hide notification")
        shouldShowNotification = false
        stopForeground(STOP_FOREGROUND_REMOVE)
    }

    private fun createChannel() {
        val importanceInt = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(CHANNEL_ID, getString(R.string.timer_channel), importanceInt)
        channel.description = getString(R.string.to_display_counter)
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
            channel
        )
    }

    @SuppressLint("MissingPermission")
    private fun buildNotification(text: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Timer")
            .setContentText(text)
            .setSilent(true)
            .build()
    }

    @SuppressLint("MissingPermission")
    private suspend fun count(seconds: Int) {
        for (count in seconds downTo 0) {
            _counter.value = Timer(count, Timer.State.COUNTING)
            if (shouldShowNotification) {
                val notification = buildNotification(count.toString())
                NotificationManagerCompat.from(this@CountDownService)
                    .notify(NOTIFICATION_ID, notification)
            }
            delay(1000L)
            Timber.d("Counting $count")
        }
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

    companion object {

        const val CHANNEL_ID = "TIMER_CHANNEL"
        const val NOTIFICATION_ID = 1234

    }

}