package dev.artenes.template.app.samples.services

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.artenes.timer.R
import dev.artenes.template.core.interfaces.Notifications
import dev.artenes.template.core.models.CountDownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class TimerService : Service() {

    @Inject
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var notifications: Notifications

    private lateinit var timer: TimerWithNotificationAndSoundModel

    override fun onCreate() {
        super.onCreate()
        timer = TimerWithNotificationAndSoundModel(scope, CountDownTimer(), notifications)
        Timber.d("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification =
            NotificationCompat.Builder(this, TimerWithNotificationAndSoundModel.CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Timer")
                .setContentText("Loading...")
                .setSilent(true)
                .build()

        ServiceCompat.startForeground(
            this,
            TimerWithNotificationAndSoundModel.NOTIFICATION_ID,
            notification,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE else 0
        )

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder = LocalBinder()

    inner class LocalBinder : Binder() {

        fun getTimer() = timer

    }

    override fun onDestroy() {
        Timber.d("onDestroy")
        scope.cancel("onDestroy")
    }

}