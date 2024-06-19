package dev.artenes.template.app.features

data class Timer(
    val seconds: Int = -1,
    val state: State = State.STOPPED,
    val initial: Int = 0
) {

    enum class State {

        STOPPED,
        COUNTING,
        PAUSED

    }

}