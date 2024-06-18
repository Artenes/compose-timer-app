package dev.artenes.template.core.interfaces

interface Notifications {

    fun createChannel(id: String, name: String, description: String, importance: Importance)

    fun showNotification(
        channelId: String,
        icon: Int,
        title: String,
        content: String,
        action: String = "",
        id: Int = -1,
        silent: Boolean = false
    )

    enum class Importance {
        HIGH,
        NORMAL,
        LOW
    }
}