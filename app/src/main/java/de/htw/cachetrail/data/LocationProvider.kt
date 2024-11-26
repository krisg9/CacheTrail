package de.htw.cachetrail.data

import android.location.Location

interface LocationProvider {
    fun getCurrentLocation(): Location
}