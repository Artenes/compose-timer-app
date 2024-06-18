package dev.artenes.template.core.models.foundation

/**
 * This one is useful to map the action of
 * waiting for a content to be loaded
 * from an async resource.
 */
sealed class DataState<out T> {
    data object Uninitialized : DataState<Nothing>()
    data object Loading : DataState<Nothing>()
    data class Success<T>(val data: T) : DataState<T>()
    data class Error(val exception: Throwable) : DataState<Nothing>()
}