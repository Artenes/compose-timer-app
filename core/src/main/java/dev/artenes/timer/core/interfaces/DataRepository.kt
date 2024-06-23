package dev.artenes.timer.core.interfaces

/**
 * Main application's repository used to store entities
 * from the business model layer
 */
interface DataRepository {

    /**
     * Seed the database for tests.
     */
    fun seedForTests()

    /**
     * Wipe all data in repository
     */
    fun wipeData()

}