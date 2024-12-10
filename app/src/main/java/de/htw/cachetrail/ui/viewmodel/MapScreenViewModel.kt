package de.htw.cachetrail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.di.ServiceLocator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MapScreenViewModel : ViewModel() {

    private val trailsRepository = ServiceLocator.getTrailRepository()

    private val _currentTrail = MutableStateFlow(Trail("", ""))
    val currentTrail: StateFlow<Trail> get() = _currentTrail.asStateFlow()

    private var _currentStationIndex = 0

    private val _currentStation = MutableStateFlow(
        Station("", 0.0, 0.0, "", "")
    )

    val station: StateFlow<Station> get() = _currentStation

    private val _feedback = MutableStateFlow<String?>(null)
    val feedback: StateFlow<String?> get() = _feedback

    fun chooseTrail(trailId: String) {
        viewModelScope.launch {
            val trail = trailsRepository.getAllTrails().first().find { trail ->
                trail.id == trailId
            }
            trail?.let {
                _currentTrail.value = trail
                _currentStation.value = _currentTrail.value.stations[0]
            }
        }
    }

    fun submitAnswer(answer: String) {
        if (answer.equals(
                _currentStation.value.answer,
                ignoreCase = true
            )
        ) {
            _feedback.value = "Correct! Moving to the next station."
            _currentStation.value = _currentTrail.value.stations[_currentStationIndex]
        } else {
            _feedback.value = "Incorrect answer. Try again!"
        }
    }

    private fun moveToNextStation() {
        if (_currentStationIndex < _currentTrail.value.stations.size - 1) {
            _currentStationIndex++
        }
    }


    private fun resetIndex() {
        _currentStationIndex = 0
    }
}