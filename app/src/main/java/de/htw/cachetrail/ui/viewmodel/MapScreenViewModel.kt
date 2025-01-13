package de.htw.cachetrail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.di.ServiceLocator
import de.htw.cachetrail.domain.ICacheTrailService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapScreenViewModel(private val trailService: ICacheTrailService = ServiceLocator.getTrailService()) :
    ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState>
        get() = _gameState

    init {
        resetGame()
    }

    fun loadTrail(trailId: String) {
        viewModelScope.launch {
            val trail = trailService.getAllTrails().firstOrNull()?.find { it.id == trailId }
            _gameState.update {
                it.copy(
                    currentTrail = trail,
                    currentStationIndex = 0,
                    feedback = null,
                    isGameCompleted = false
                )
            }
        }
    }

    fun submitAnswer(answer: String) {
        val currentStation = _gameState.value.currentStation
        if (currentStation != null && answer.equals(currentStation.answer, ignoreCase = true)) {
            moveToNextStation()
        } else {
            _gameState.update { it.copy(feedback = Feedback.INCORRECT) }
        }
    }

    private fun moveToNextStation() {
        _gameState.update { state ->
            val nextIndex = state.currentStationIndex + 1
            val isCompleted = nextIndex >= (state.currentTrail?.stations?.size ?: 0)

            state.copy(
                feedback = if (isCompleted) Feedback.COMPLETED else null,
                currentStationIndex = nextIndex,
                isGameCompleted = isCompleted
            )
        }
    }

    private fun resetGame() {
        _gameState.value = GameState()
    }

    data class GameState(
        val currentTrail: Trail? = null,
        val currentStationIndex: Int = 0,
        val feedback: Feedback? = null,
        val isGameCompleted: Boolean = false
    ) {
        val currentStation: Station?
            get() = currentTrail?.stations?.getOrNull(currentStationIndex)
    }
}

enum class Feedback {
    CORRECT,
    INCORRECT,
    COMPLETED
}
