package dev.artenes.template.core.models.foundation

/**
 * This is useful for when you need to pass down to the view
 * both a value and its string representation to be displayed
 * to the user.
 */
data class FormattedValue<T>(
    val original: T,
    val formatted: String
)