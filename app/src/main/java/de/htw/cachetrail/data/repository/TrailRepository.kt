package de.htw.cachetrail.data.repository

import de.htw.cachetrail.data.Trail
import de.htw.cachetrail.data.datasource.TrailLocalDataSource

class TrailRepository(
    private val trailsDataSource: TrailLocalDataSource
)
    : ITrailsRepository {

    override fun getAllTrails(): List<Trail> {
        return trailsDataSource.getAllTrails()
    }
}

interface ITrailsRepository {
    fun getAllTrails(): List<Trail>
}