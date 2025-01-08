package de.htw.cachetrail.domain.usecase

import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.data.repository.ITrailsRepository
import de.htw.cachetrail.di.ServiceLocator
import kotlinx.coroutines.flow.Flow

class RetrieveTrailsUseCase(private val repository: ITrailsRepository = ServiceLocator.getTrailRepository()) {
    operator fun invoke(): Flow<List<Trail>> = repository.getAllTrails();
}

class AddStationUseCase(private val repository: ITrailsRepository = ServiceLocator.getTrailRepository()) {
    operator fun invoke(trailId: String, station: Station) =
        repository.addStation(trailId, station)
}

class DeleteStationUseCase(private val repository: ITrailsRepository = ServiceLocator.getTrailRepository()) {
    operator fun invoke(trailId: String, station: Station) =
        repository.deleteStation(trailId, station)
}

class AddTrailUseCase(private val repository: ITrailsRepository = ServiceLocator.getTrailRepository()) {
    operator fun invoke(trail: Trail) =
        repository.addTrail(trail)
}

class DeleteTrailUseCase(private val repository: ITrailsRepository = ServiceLocator.getTrailRepository()) {
    operator fun invoke(trailId: String) =
        repository.deleteTrail(trailId)
}