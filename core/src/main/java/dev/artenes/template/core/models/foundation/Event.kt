package dev.artenes.template.core.models.foundation

/**
 * This one is useful to send events from the ViewModel to the View
 * such as when the view needs to be finished or to display a snack bar
 */
sealed class Event<out T> {

    data object NoEvent : Event<Nothing>()

    data object SuccessfulEmptyEvent : Event<Nothing>()

    data class SuccessfulEvent<T>(val payload: T) : Event<T>() {

        private var consumed = false

        fun consume(consumer: (T) -> Unit) {
            if (!consumed) {
                consumer(payload)
                consumed = true
            }
        }

        fun peek(): T = payload

    }

}