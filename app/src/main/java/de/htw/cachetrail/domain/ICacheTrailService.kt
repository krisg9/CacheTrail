package de.htw.cachetrail.domain

import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import kotlinx.coroutines.flow.Flow

interface ICacheTrailService {

    fun getAllTrails(): Flow<List<Trail>>

    fun addStation(trailId: String, station: Station)

    fun deleteStation(trailId: String, station: Station)

    fun addTrail(trail: Trail)

    fun deleteTrail(trailId: String)
}
