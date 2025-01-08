package de.htw.cachetrail.domain

import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.domain.usecase.AddStationUseCase
import de.htw.cachetrail.domain.usecase.AddTrailUseCase
import de.htw.cachetrail.domain.usecase.DeleteStationUseCase
import de.htw.cachetrail.domain.usecase.DeleteTrailUseCase
import de.htw.cachetrail.domain.usecase.RetrieveTrailsUseCase
import kotlinx.coroutines.flow.Flow

class CacheTrailService(
    private val retrieveTrailsUseCase: RetrieveTrailsUseCase,
    private val addStationUseCase: AddStationUseCase,
    private val deleteStationUseCase: DeleteStationUseCase,
    private val addTrailUseCase: AddTrailUseCase,
    private val deleteTrailUseCase: DeleteTrailUseCase
) : ICacheTrailService {

    override fun getAllTrails(): Flow<List<Trail>> = retrieveTrailsUseCase()
    override fun addStation(trailId: String, station: Station) = addStationUseCase(trailId, station)
    override fun deleteStation(trailId: String, station: Station) =
        deleteStationUseCase(trailId, station)

    override fun addTrail(trail: Trail) = addTrailUseCase(trail)
    override fun deleteTrail(trailId: String) = deleteTrailUseCase(trailId)
}