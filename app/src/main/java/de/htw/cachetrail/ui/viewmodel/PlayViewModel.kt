package de.htw.cachetrail.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htw.cachetrail.ServiceLocator
import de.htw.cachetrail.data.Trail
import kotlinx.coroutines.launch

class PlayViewModel: ViewModel() {

    private val trailRepository = ServiceLocator.getTrailRepository()

    private val _trails: MutableState<List<Trail>> = mutableStateOf(emptyList())
    val trails: State<List<Trail>> = _trails

    init {
        loadTrails()
    }

    private fun loadTrails() {
        viewModelScope.launch {
            _trails.value = trailRepository.getAllTrails()
        }
    }
}