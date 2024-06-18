package dev.artenes.template.core.interfaces

import kotlinx.coroutines.flow.Flow

/**
 * Repository for settings storage
 */
interface SettingsRepository {

    /**
     * Saves a key-value pair
     */
    suspend fun save(key: String, value: String)

    /**
     * Gets the value for the given key
     */
    suspend fun get(key: String, default: String = ""): String

    /**
     * Listen for changes in a given key
     */
    fun listen(key: String, default: String = ""): Flow<String>

}