package dev.artenes.budget.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import dev.artenes.template.core.interfaces.ApplicationsRepository
import dev.artenes.template.core.interfaces.DataRepository
import dev.artenes.template.core.models.foundation.Event
import javax.inject.Inject

@HiltViewModel
class DevelopmentViewModel @Inject constructor(
    private val repository: DataRepository,
    private val applications: ApplicationsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state

    data class State(
        val snackBar: Event<String> = Event.NoEvent,
        val operationRunning: Boolean = false
    )

    fun seedDatabase() {
        _state.value = State(operationRunning = true)
        viewModelScope.launch {
            repository.seedForTests()
            _state.value =
                State(operationRunning = false, snackBar = Event.SuccessfulEvent("Database seeded"))
        }
    }

    fun clearDatabase() {
        _state.value = State(operationRunning = true)
        viewModelScope.launch {
            repository.wipeData()
            _state.value =
                State(
                    operationRunning = false,
                    snackBar = Event.SuccessfulEvent("Database cleared")
                )
        }
    }

    fun openApp() {
        applications.startOwn()
    }

}