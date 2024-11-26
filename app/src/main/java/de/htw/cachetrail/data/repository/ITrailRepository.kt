package de.htw.cachetrail.data.repository

import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import kotlinx.coroutines.flow.Flow

interface ITrailsRepository {

    fun getAllTrails(): Flow<List<Trail>>

    fun addTrail(trail: Trail)

    fun editTrail(trail: Trail)

    fun deleteTrail(trailId: String)

    fun addStation(trailId: String, station: Station)

    fun deleteStation(trailId: String, station: Station)
}