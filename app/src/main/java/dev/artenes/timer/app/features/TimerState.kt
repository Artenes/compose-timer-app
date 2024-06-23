package dev.artenes.timer.app.features

data class Timer(
    val seconds: Int = -1,
    val state: State = State.STOPPED,
    val initial: Int = 0
) {

    enum class State {

        STOPPED,
        COUNTING,
        DONE,
        PAUSED

    }

}