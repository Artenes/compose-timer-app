package dev.artenes.timer

import dev.artenes.timer.android.doubles.ValuesRepositoryDouble
import dev.artenes.timer.core.interfaces.DataRepository
import dev.artenes.timer.core.interfaces.ValuesRepository
import dev.artenes.timer.data.AppRepository
import dev.artenes.timer.data.dao.doubles.SampleDaoDouble
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class TestFixtures {

    val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val valuesRepository: ValuesRepository
    private val repository: DataRepository

    init {
        valuesRepository = ValuesRepositoryDouble()
        repository = AppRepository(
            SampleDaoDouble(),
            testDispatcher
        )
    }

    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    fun tearDown() {
        Dispatchers.resetMain()
    }

}