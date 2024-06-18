package dev.artenes.template.core.models.foundation

data class SelectableItem<T>(
    val value: T,
    val label: String,
    val selected: Boolean = false,
    val isInvalid: Boolean = false,
    val position: Int = -1
) {

    constructor(value: T) : this(value, value.toString())

}