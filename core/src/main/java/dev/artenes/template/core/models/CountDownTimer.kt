package dev.artenes.template.core.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext

class CountDownTimer(private val dispatcher: CoroutineContext = Dispatchers.IO) {

    private var running = AtomicBoolean(false)

    private val _timer = MutableStateFlow<Int?>(null)

    suspend fun start(timeInSeconds: Int) {
        stop()
        _timer.value = timeInSeconds
        withContext(dispatcher) {
            for (secondsRemaining in (timeInSeconds - 1) downTo 0) {
                delay(1000L)
                _timer.value = secondsRemaining
            }
        }
    }

    fun stop() {
        running.set(false)
        _timer.value = -1
    }

    fun timer(): Flow<Int?> = _timer

    fun isRunning() = running.get()

}