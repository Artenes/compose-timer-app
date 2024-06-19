package dev.artenes.template.app.features

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import dagger.hilt.android.AndroidEntryPoint
import dev.artenes.template.app.MainActivity
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
        _counter.value = _counter.value.copy(initial = seconds)
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
        _counter.value = _counter.value.copy(state = Timer.State.STOPPED, seconds = -1)
    }

    fun showNotification() {
        if (state == Timer.State.STOPPED) {
            Timber.d("show notification STOPPED")
            return
        }

        val notification = createNotification(getString(R.string.loading))

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
    private fun createNotification(text: String): Notification {

        val intent = Intent(
            Intent.ACTION_VIEW,
            "https://timer.artenes.dev".toUri(),
            this,
            MainActivity::class.java
        )

        val deepLinkPendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Timer")
            .setContentText(text)
            .setContentIntent(deepLinkPendingIntent)
            .setSilent(true)

        val pause = Intent(this, CountDownService::class.java).apply {
            action = "PAUSE"
        }

        val stop = Intent(this, CountDownService::class.java).apply {
            action = "STOP"
        }

        val resume = Intent(this, CountDownService::class.java).apply {
            action = "RESUME"
        }

        val pausePending = PendingIntent.getService(this, 0, pause, PendingIntent.FLAG_IMMUTABLE)

        val stopPending = PendingIntent.getService(this, 0, stop, PendingIntent.FLAG_IMMUTABLE)

        val resumePending = PendingIntent.getService(this, 0, resume, PendingIntent.FLAG_IMMUTABLE)

        if (state == Timer.State.COUNTING) {

            builder.addAction(R.drawable.ic_launcher_foreground, "Pause", pausePending)

        } else if (state == Timer.State.PAUSED) {

            builder.addAction(R.drawable.ic_launcher_foreground, "Resume", resumePending)

        }

        builder.addAction(R.drawable.ic_launcher_foreground, "Stop", stopPending)

        return builder.build()

    }

    @SuppressLint("MissingPermission")
    private suspend fun count(seconds: Int) {
        for (count in seconds downTo 0) {
            _counter.value = _counter.value.copy(state = Timer.State.COUNTING, seconds = count)
            if (shouldShowNotification) {
                val notification = createNotification(count.toString())
                NotificationManagerCompat.from(this@CountDownService)
                    .notify(NOTIFICATION_ID, notification)
            }
            delay(1000L)
            Timber.v("Counting $count")
        }
    }

    @SuppressLint("MissingPermission")
    private fun refreshNotification() {
        if (shouldShowNotification) {
            val notification = createNotification(_counter.value.seconds.toString())
            NotificationManagerCompat.from(this@CountDownService)
                .notify(NOTIFICATION_ID, notification)
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

        val action = intent?.action

        when (action) {

            "STOP" -> {
                Timber.d("Action stop")
                stop()
                hideNotification()
            }

            "PAUSE" -> {
                Timber.d("Action pause")
                pause()
                refreshNotification()
            }

            "RESUME" -> {
                Timber.d("Action resume")
                resume()
                refreshNotification()
            }

        }

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