package dev.artenes.template.app.samples.services

import dev.artenes.timer.R
import dev.artenes.template.core.interfaces.Notifications
import dev.artenes.template.core.models.CountDownTimer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class TimerWithNotificationAndSoundModel @Inject constructor(
    private val scope: CoroutineScope,
    private val countDownTimer: CountDownTimer,
    private val notifications: Notifications
) {

    init {

        notifications.createChannel(
            id = CHANNEL_ID,
            name = "Count down channel",
            description = "Displays the countdown timer",
            importance = Notifications.Importance.NORMAL
        )

        scope.launch {
            countDownTimer.timer().collectLatest {

                if (it == null) {
                    Timber.d("No time")
                    return@collectLatest
                }

                if (it > 0) {
                    notifications.showNotification(
                        channelId = CHANNEL_ID,
                        icon = R.drawable.ic_launcher_foreground,
                        title = "Timer",
                        content = it.toString(),
                        id = NOTIFICATION_ID,
                        silent = true
                    )
                    return@collectLatest
                }

                if (it == 0) {
                    Timber.d("Time's up")
                    return@collectLatest
                }

            }
        }
    }

    fun timer() = countDownTimer.timer()

    fun start(seconds: Int) {
        scope.launch {
            countDownTimer.start(seconds)
        }
    }

    fun stop() {
        countDownTimer.stop()
    }

    fun isRunning() = countDownTimer.isRunning()

    companion object {

        const val CHANNEL_ID = "TIMER_CHANNEL"
        const val NOTIFICATION_ID = 3478

    }

}