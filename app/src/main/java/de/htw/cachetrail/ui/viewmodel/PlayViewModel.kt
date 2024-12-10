package de.htw.cachetrail.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.di.ServiceLocator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class PlayViewModel : ViewModel() {

    private val trailsRepository = ServiceLocator.getTrailRepository()

    val trails: StateFlow<List<Trail>> = trailsRepository.getAllTrails().stateIn(
        viewModelScope, SharingStarted.Lazily, emptyList()
    )
}