package dev.artenes.template.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import dev.artenes.template.core.interfaces.ApplicationsRepository

class AndroidApplicationsRepository(
    private val context: Context,
    private val entryPoint: ComponentName
) : ApplicationsRepository {

    override fun startOwn() {
        val intent = Intent().apply {
            component = entryPoint
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }

}