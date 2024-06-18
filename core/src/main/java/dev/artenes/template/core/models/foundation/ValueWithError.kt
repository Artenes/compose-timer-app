package dev.artenes.template.core.models.foundation

/**
 * This is useful for when you want to expose a value from your ViewModel
 * that can have an error state as such:
 *
 *     private val _description = MutableStateFlow(ValueWithError())
 *     val description: StateFlow<ValueWithError> = _description
 *
 * With this you have just one member variable in the ViewModel instead of two
 * to represent the value and the error message
 */
data class ValueWithError<T>(
    val value: T,
    val error: String? = null
) {

    fun hasError() = error != null

}