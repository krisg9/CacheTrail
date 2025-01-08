package de.htw.cachetrail.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.data.repository.ITrailsRepository
import de.htw.cachetrail.data.repository.TrailRepository
import de.htw.cachetrail.domain.CacheTrailService
import de.htw.cachetrail.domain.usecase.AddStationUseCase
import de.htw.cachetrail.domain.usecase.AddTrailUseCase
import de.htw.cachetrail.domain.usecase.DeleteStationUseCase
import de.htw.cachetrail.domain.usecase.DeleteTrailUseCase
import de.htw.cachetrail.domain.usecase.RetrieveTrailsUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.clearAllCaches
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.verify


@OptIn(ExperimentalCoroutinesApi::class)
class EditTrailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: EditTrailsViewModel
    private lateinit var trailRepository: ITrailsRepository
    private lateinit var service: CacheTrailService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        trailRepository = mock(TrailRepository::class.java)
        service = CacheTrailService(
            RetrieveTrailsUseCase(trailRepository),
            AddStationUseCase(trailRepository),
            DeleteStationUseCase(trailRepository),
            AddTrailUseCase(trailRepository),
            DeleteTrailUseCase(trailRepository)
        )

        Dispatchers.setMain(testDispatcher)
        viewModel = EditTrailsViewModel(service)
    }

    @Test
    fun `addStation calls repository addStation`() = runTest {
        val trailId = "trail1"
        val station = Station(
            id = "station1",
            latitude = 52.0,
            longitude = 13.0,
            question = "Q",
            answer = "A"
        )

        viewModel.addStation(trailId, station)
        advanceUntilIdle()

        verify(trailRepository).addStation(trailId, station)
    }

    @Test
    fun `deleteStation calls repository deleteStation`() = runTest {
        val trailId = "trail1"
        val station = Station(
            id = "station1",
            latitude = 52.0,
            longitude = 13.0,
            question = "Q",
            answer = "A"
        )
        val trails = listOf(
            Trail(id = trailId, name = "Trail1", stations = listOf(station))
        )

        `when`(trailRepository.getAllTrails()).thenReturn(flowOf(trails))

        viewModel.deleteStation(station)
        advanceUntilIdle()

        verify(trailRepository).deleteStation(trailId, station)
    }

    @Test
    fun `addTrail calls repository addTrail`() {
        val newTrail = Trail(id = "trail1", name = "Trail1", stations = emptyList())

        viewModel.addTrail(newTrail)

        verify(trailRepository).addTrail(newTrail)
    }

    @Test
    fun `getTrailById returns correct trail`() = runTest {
        val trailId = "trail1"
        val trail = Trail(id = trailId, name = "Trail1", stations = emptyList())
        val trails = listOf(trail)

        `when`(trailRepository.getAllTrails()).thenReturn(flowOf(trails))

        val result = viewModel.getTrailById(trailId).first()

        assertEquals(trail, result)
    }

    @Test
    fun `deleteTrail calls repository deleteTrail`() {
        val trailId = "trail1"

        viewModel.deleteTrail(trailId)

        verify(trailRepository).deleteTrail(trailId)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllCaches()
    }
}