package de.htw.cachetrail.data.datasource

import de.htw.cachetrail.data.Trail

class TrailLocalDataSource {
    fun getAllTrails(): List<Trail> {
        return listOf(
            Trail("1", "Old Berlin"),
            Trail("21", "Modern Berlin"),
            Trail("23", "East Berlin"),
            Trail("26", "West Berlin")
        )
    }
}