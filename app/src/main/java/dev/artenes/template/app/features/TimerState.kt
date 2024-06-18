package dev.artenes.template.app.features

data class Timer(
    val seconds: Int,
    val state: State
) {

    enum class State {

        STOPPED,
        COUNTING,
        PAUSED

    }

}