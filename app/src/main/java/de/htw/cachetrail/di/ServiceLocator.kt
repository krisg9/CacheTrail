package de.htw.cachetrail.di

import android.content.Context
import de.htw.cachetrail.data.repository.ITrailsRepository
import de.htw.cachetrail.data.repository.TrailRepository
import de.htw.cachetrail.data.datasource.TrailSharedPreferencesDataSource

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

    fun getTrailRepository(): ITrailsRepository {
        return trailRepository
    }
}