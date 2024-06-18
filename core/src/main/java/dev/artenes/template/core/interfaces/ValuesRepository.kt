package dev.artenes.template.core.interfaces

/**
 * Repository to retrieve translatable messages
 */
interface ValuesRepository {

    /**
     * Gets the string value for the given id
     */
    fun get(id: Int, vararg args: Any): String

}