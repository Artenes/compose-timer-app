package dev.artenes.template.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import dev.artenes.template.core.interfaces.DataRepository
import dev.artenes.template.data.dao.SampleDao
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Singleton
class AppRepository @Inject constructor(
    private val sampleDao: SampleDao,
    private val dispatcher: CoroutineContext = Dispatchers.IO
): DataRepository {

    override fun seedForTests() {
        //seed database for tests
    }

    override fun wipeData() {
        //wipe all data
    }

    suspend fun save(entity: SampleEntity) {
        withContext(dispatcher) {

        }
    }

}