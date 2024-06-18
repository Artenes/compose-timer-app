package dev.artenes.template.core.models.foundation

sealed class FunctionResult<out T> {

    data class Success<T>(val data: T) : FunctionResult<T>()

    data object EmptySuccess : FunctionResult<Nothing>()

    data class Error(val exception: Throwable) : FunctionResult<Nothing>()

}