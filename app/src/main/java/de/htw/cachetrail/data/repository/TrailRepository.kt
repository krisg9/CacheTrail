package de.htw.cachetrail.data.repository

import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.data.datasource.TrailSharedPreferencesDataSource
import de.htw.cachetrail.data.model.Station
import kotlinx.coroutines.flow.Flow

class TrailRepository(
    private val trailsDataSource: TrailSharedPreferencesDataSource
) : ITrailsRepository {

    override fun getAllTrails(): Flow<List<Trail>> {
        return trailsDataSource.getAllTrails()
    }

    override fun addTrail(trail: Trail) {
        trailsDataSource.addTrail(trail)
    }

    override fun editTrail(trail: Trail) {
        trailsDataSource.editTrail(trail)
    }

    override fun deleteTrail(trailId: String) {
        trailsDataSource.deleteTrail(trailId)
    }

    override fun addStation(trailId: String, station: Station) {
        trailsDataSource.addStation(trailId, station)
    }

    override fun deleteStation(trailId: String, station: Station) {
        trailsDataSource.deleteStation(trailId, station)
    }
}