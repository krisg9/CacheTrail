package de.htw.cachetrail.di

import android.content.Context
import de.htw.cachetrail.data.repository.ITrailsRepository
import de.htw.cachetrail.data.repository.TrailRepository
import de.htw.cachetrail.data.datasource.TrailSharedPreferencesDataSource
import de.htw.cachetrail.domain.CacheTrailService
import de.htw.cachetrail.domain.ICacheTrailService
import de.htw.cachetrail.domain.usecase.AddStationUseCase
import de.htw.cachetrail.domain.usecase.AddTrailUseCase
import de.htw.cachetrail.domain.usecase.DeleteStationUseCase
import de.htw.cachetrail.domain.usecase.DeleteTrailUseCase
import de.htw.cachetrail.domain.usecase.RetrieveTrailsUseCase

object ServiceLocator {
    private var appContext: Context? = null

    private val dataSource: TrailSharedPreferencesDataSource by lazy {
        TrailSharedPreferencesDataSource(
            appContext!!
        )
    }

    fun getContext(): Context? {
        return appContext
    }

    fun initialize(context: Context) {
        this.appContext = context.applicationContext
    }

    private val trailRepository by lazy {
        TrailRepository(dataSource)
    }

    private val cacheTrailService by lazy {
        CacheTrailService(
            retrieveTrailsUseCase = RetrieveTrailsUseCase(),
            addStationUseCase = AddStationUseCase(),
            deleteStationUseCase = DeleteStationUseCase(),
            addTrailUseCase = AddTrailUseCase(),
            deleteTrailUseCase = DeleteTrailUseCase()
        )
    }

    fun getTrailRepository(): ITrailsRepository {
        return trailRepository
    }

    fun getTrailService(): ICacheTrailService {
        return cacheTrailService
    }
}