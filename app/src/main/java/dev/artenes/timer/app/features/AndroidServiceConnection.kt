package dev.artenes.timer.app.features

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

class AndroidServiceConnection @Inject constructor(@ApplicationContext private val context: Context) {

    private var binder: IBinder? = null
    private var connection: ServiceConnection? = null

    private val mutex = Mutex()

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun startAndBind(clazz: Class<*>): IBinder {

        return mutex.withLock {
            suspendCancellableCoroutine { cont ->

                if (binder != null) {
                    Timber.d("Binder exists")
                    cont.resume(binder!!) {
                        //ignore
                    }
                    return@suspendCancellableCoroutine
                }

                val intent = Intent(context, clazz)

                connection = object : ServiceConnection {
                    override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                        Timber.d("Service connected")
                        binder = service
                        cont.resume(binder!!) {
                            //ignore
                        }
                    }

                    override fun onServiceDisconnected(name: ComponentName?) {
                        //ignore
                    }
                }

                context.startService(intent)
                context.bindService(intent, connection!!, Context.BIND_AUTO_CREATE)

            }
        }

    }

    fun disconnect() {
        context.unbindService(connection!!)
    }

}