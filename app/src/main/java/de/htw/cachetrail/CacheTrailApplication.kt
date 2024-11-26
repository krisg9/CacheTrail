package de.htw.cachetrail

import android.app.Application
import de.htw.cachetrail.di.ServiceLocator

class CacheTrailApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.initialize(this)
    }
}