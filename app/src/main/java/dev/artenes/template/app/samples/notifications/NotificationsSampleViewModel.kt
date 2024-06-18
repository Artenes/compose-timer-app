package dev.artenes.template.app.samples.notifications

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.artenes.timer.R
import dev.artenes.template.core.interfaces.Notifications
import javax.inject.Inject

@HiltViewModel
class NotificationsSampleViewModel @Inject constructor(
    private val notifications: Notifications
) : ViewModel() {

    init {
        notifications.createChannel(
            id = CHANNEL_ID_NORMAL_PRIORITY,
            name = "Normal priority notifications",
            description = "These will make sound",
            importance = Notifications.Importance.NORMAL
        )
        notifications.createChannel(
            id = CHANNEL_ID_HIGH_PRIORITY,
            name = "High priority notifications",
            description = "These will make sound and popup on screen",
            importance = Notifications.Importance.HIGH
        )
    }

    fun showNormalPriorityNotification() {

        notifications.showNotification(
            CHANNEL_ID_NORMAL_PRIORITY,
            R.drawable.ic_launcher_foreground,
            "Something happened",
            "This is just a test"
        )

    }

    fun showHighPriorityNotification() {

        notifications.showNotification(
            CHANNEL_ID_HIGH_PRIORITY,
            R.drawable.ic_launcher_foreground,
            "Something happened",
            "This is just a test"
        )

    }

    companion object {

        const val CHANNEL_ID_NORMAL_PRIORITY = "NORMAL_PRIORITY_CHANNEL"
        const val CHANNEL_ID_HIGH_PRIORITY = "HIGH_PRIORITY_CHANNEL"

    }

}