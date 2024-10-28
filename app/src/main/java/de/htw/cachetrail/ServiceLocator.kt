package de.htw.cachetrail

import de.htw.cachetrail.data.repository.ITrailsRepository
import de.htw.cachetrail.data.repository.TrailRepository
import de.htw.cachetrail.data.datasource.TrailLocalDataSource

object ServiceLocator {
    fun getTrailRepository(): ITrailsRepository = TrailRepository(TrailLocalDataSource())
}