package de.htw.cachetrail.data.datasource

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyString
import org.mockito.Mockito.atLeastOnce
import org.mockito.Mockito.eq
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class TrailSharedPreferencesDataSourceTest {

    private lateinit var context: Context
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var dataSource: TrailSharedPreferencesDataSource
    private val gson = Gson()

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)
        editor = mock(SharedPreferences.Editor::class.java)

        `when`(sharedPreferences.edit()).thenReturn(editor)
        `when`(editor.putString(anyString(), anyString())).thenReturn(editor)
        `when`(context.getSharedPreferences("trails", Context.MODE_PRIVATE)).thenReturn(
            sharedPreferences
        )
        `when`(sharedPreferences.getString("trails", "[]")).thenReturn("[]")

        dataSource = TrailSharedPreferencesDataSource(context)
    }

    @Test
    fun `loadTrails initializes trails from SharedPreferences`() = runBlocking {
        val trails = listOf(Trail("1", "Trail 1", emptyList()))
        val json = gson.toJson(trails)

        `when`(sharedPreferences.getString(anyString(), anyString())).thenReturn(json)

        dataSource = TrailSharedPreferencesDataSource(context)

        val result = dataSource.getAllTrails().first()
        assertEquals(trails, result)
    }

    @Test
    fun `addTrail adds a new trail and saves to SharedPreferences`() = runBlocking {
        val trail = Trail("1", "Trail 1", emptyList())
        dataSource.addTrail(trail)

        verify(editor).putString(eq("trails"), anyString())
        verify(editor).apply()

        val result = dataSource.getAllTrails().first()
        assertEquals(listOf(trail), result)
    }

    @Test
    fun `editTrail updates an existing trail and saves to SharedPreferences`() = runBlocking {
        val trail = Trail("1", "Trail 1", emptyList())
        val updatedTrail = trail.copy(name = "Updated Trail")
        dataSource.addTrail(trail)
        dataSource.editTrail(updatedTrail)

        verify(editor, atLeastOnce()).putString(eq("trails"), anyString())
        verify(editor, atLeastOnce()).apply()

        val result = dataSource.getAllTrails().first()
        assertEquals(listOf(updatedTrail), result)
    }

    @Test
    fun `deleteTrail removes a trail by ID and saves to SharedPreferences`() = runBlocking {
        val trail = Trail("1", "Trail 1", emptyList())
        dataSource.addTrail(trail)
        dataSource.deleteTrail("1")

        verify(editor, atLeastOnce()).putString(eq("trails"), anyString())
        verify(editor, atLeastOnce()).apply()

        val result = dataSource.getAllTrails().first()
        assertEquals(emptyList<Trail>(), result)
    }

    @Test
    fun `addStation adds a station to a trail and saves to SharedPreferences`() = runBlocking {
        val trail = Trail("1", "Trail 1", emptyList())
        val station = Station("101", 52.0, 13.0, "Q", "A")
        dataSource.addTrail(trail)
        dataSource.addStation("1", station)

        val result = dataSource.getAllTrails().first()
        assertEquals(listOf(trail.copy(stations = listOf(station))), result)
    }

    @Test
    fun `deleteStation removes a station from a trail and saves to SharedPreferences`() =
        runBlocking {
            val station = Station("101", 52.0, 13.0, "Q", "A")
            val trail = Trail("1", "Trail 1", listOf(station))
            dataSource.addTrail(trail)
            dataSource.deleteStation("1", station)

            val result = dataSource.getAllTrails().first()
            assertEquals(listOf(trail.copy(stations = emptyList())), result)
        }

    @Test
    fun `getAllTrails returns the correct list of trails`() = runBlocking {
        val trail1 = Trail("1", "Trail 1", emptyList())
        val trail2 = Trail("2", "Trail 2", emptyList())
        dataSource.addTrail(trail1)
        dataSource.addTrail(trail2)

        val result = dataSource.getAllTrails().first()
        assertEquals(listOf(trail1, trail2), result)
    }

    @Test
    fun `editTrail does not modify trails if ID is not found`() = runBlocking {
        val trail1 = Trail("1", "Trail 1", emptyList())
        dataSource.addTrail(trail1)
        dataSource.editTrail(Trail("2", "Non-existent Trail", emptyList()))

        val result = dataSource.getAllTrails().first()
        assertEquals(listOf(trail1), result)
    }

    @Test
    fun `deleteTrail does not throw error if trail ID is not found`() = runBlocking {
        val trail = Trail("1", "Trail 1", emptyList())
        dataSource.addTrail(trail)
        dataSource.deleteTrail("2")

        val result = dataSource.getAllTrails().first()
        assertEquals(listOf(trail), result)
    }
}
