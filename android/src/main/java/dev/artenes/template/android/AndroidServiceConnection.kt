package dev.artenes.template.android

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder

class AndroidServiceConnection(private val context: Context) {

    private var serviceConnection: ServiceConnection? = null

    fun bind(clazz: Class<*>, callback: ConnectionState) {

        if (serviceConnection != null) {
            return
        }

        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                callback.onConnected(service!!)
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                callback.onDisconnected()
            }
        }

        val intent = Intent(context, clazz)
        context.startForegroundService(intent)
        context.bindService(intent, serviceConnection!!, Context.BIND_AUTO_CREATE)

    }

    fun unbind() {

        if (serviceConnection == null) {
            return
        }

        context.unbindService(serviceConnection!!)
        serviceConnection = null

    }

    interface ConnectionState {

        fun onConnected(binder: IBinder)

        fun onDisconnected()

    }

}