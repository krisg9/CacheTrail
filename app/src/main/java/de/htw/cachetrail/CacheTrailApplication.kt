package de.htw.cachetrail

import android.app.Application
import de.htw.cachetrail.di.ServiceLocator
import org.osmdroid.config.Configuration

class CacheTrailApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Configuration.getInstance()
            .load(
                this,
                applicationContext.getSharedPreferences("osm_pref", MODE_PRIVATE)
            )
        ServiceLocator.initialize(this)
    }
}