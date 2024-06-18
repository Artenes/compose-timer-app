package dev.artenes.template.core.interfaces

/**
 * Interactions with device applications
 */
interface ApplicationsRepository {

    /**
     * Starts own application, opening its main screen.
     */
    fun startOwn()

}