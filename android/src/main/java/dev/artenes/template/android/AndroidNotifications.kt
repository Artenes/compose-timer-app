package dev.artenes.template.android

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import dev.artenes.template.core.interfaces.Notifications
import dev.artenes.template.core.models.InvalidChannelImportanceException
import dev.artenes.template.core.models.MissingPermissionToDisplayNotificationException

@SuppressLint("InlinedApi")
class AndroidNotifications(
    private val context: Context,
    private val mainActivity: Class<*>
) : Notifications {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private var notificationIdCounter: Int = 0

    override fun createChannel(
        id: String,
        name: String,
        description: String,
        importance: Notifications.Importance
    ) {
        val importanceInt = parseImportanceForChannel(importance)
        val channel = NotificationChannel(id, name, importanceInt)
        channel.description = description
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    override fun showNotification(
        channelId: String,
        icon: Int,
        title: String,
        content: String,
        action: String,
        id: Int,
        silent: Boolean
    ) {

        val channel = notificationManager.getNotificationChannel(channelId)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setContentText(content)
            .setSilent(silent)
            .setPriority(parseImportanceForNotification(channel.importance))

        if (action.isNotEmpty()) {

            val deepLinkIntent = Intent(Intent.ACTION_VIEW, action.toUri(), context, mainActivity)
            val deepLinkPendingIntent = TaskStackBuilder.create(context).run {
                addNextIntentWithParentStack(deepLinkIntent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }
            builder.setContentIntent(deepLinkPendingIntent).setAutoCancel(true)

        }

        with(NotificationManagerCompat.from(context)) {
            if (!hasPermission()) {
                throw MissingPermissionToDisplayNotificationException(Manifest.permission.POST_NOTIFICATIONS)
            }
            val finalId = if (id == -1) makeNotificationId() else id
            notify(finalId, builder.build())
        }
    }

    private fun makeNotificationId(): Int {
        if (notificationIdCounter == Int.MAX_VALUE) {
            notificationIdCounter = 0
        }
        return ++notificationIdCounter
    }

    private fun hasPermission(): Boolean = ActivityCompat.checkSelfPermission(
        context,
        Manifest.permission.POST_NOTIFICATIONS
    ) == PackageManager.PERMISSION_GRANTED

    private fun parseImportanceForChannel(importance: Notifications.Importance): Int {
        return when (importance) {
            Notifications.Importance.NORMAL -> NotificationManager.IMPORTANCE_DEFAULT
            Notifications.Importance.HIGH -> NotificationManager.IMPORTANCE_HIGH
            Notifications.Importance.LOW -> NotificationManager.IMPORTANCE_LOW
        }
    }

    private fun parseImportanceForNotification(importance: Int): Int {
        return when (importance) {
            NotificationManager.IMPORTANCE_DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
            NotificationManager.IMPORTANCE_HIGH -> NotificationCompat.PRIORITY_HIGH
            NotificationManager.IMPORTANCE_LOW -> NotificationCompat.PRIORITY_LOW
            else -> throw InvalidChannelImportanceException(importance)
        }
    }

}