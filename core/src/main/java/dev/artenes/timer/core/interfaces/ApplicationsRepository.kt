package dev.artenes.timer.core.interfaces

/**
 * Interactions with device applications
 */
interface ApplicationsRepository {

    /**
     * Starts own application, opening its main screen.
     */
    fun startOwn()

}