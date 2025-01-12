package de.htw.cachetrail.integration

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.data.repository.ITrailsRepository
import de.htw.cachetrail.di.ServiceLocator
import de.htw.cachetrail.domain.CacheTrailService
import de.htw.cachetrail.domain.usecase.AddStationUseCase
import de.htw.cachetrail.domain.usecase.AddTrailUseCase
import de.htw.cachetrail.domain.usecase.DeleteStationUseCase
import de.htw.cachetrail.domain.usecase.DeleteTrailUseCase
import de.htw.cachetrail.domain.usecase.RetrieveTrailsUseCase
import de.htw.cachetrail.ui.viewmodel.Feedback
import de.htw.cachetrail.ui.viewmodel.MapScreenViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MapScreenViewModelIntegrationTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapScreenViewModel
    private lateinit var trailRepository: ITrailsRepository
    private lateinit var trailService: CacheTrailService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        trailRepository = ServiceLocator.getTrailRepository()
        trailService = CacheTrailService(
            RetrieveTrailsUseCase(trailRepository),
            AddStationUseCase(trailRepository),
            DeleteStationUseCase(trailRepository),
            AddTrailUseCase(trailRepository),
            DeleteTrailUseCase(trailRepository)
        )

        Dispatchers.setMain(testDispatcher)
        viewModel = MapScreenViewModel(trailService)
    }

    @Test
    fun simulateGameflow() = runTest {
        val trailId = "trail1"
        val station1 = Station(
            id = "station1",
            latitude = 52.0,
            longitude = 13.0,
            question = "Question 1?",
            answer = "Answer 1"
        )
        val station2 = Station(
            id = "station2",
            latitude = 52.1,
            longitude = 13.1,
            question = "Question 2?",
            answer = "Answer 2"
        )
        val trail = Trail(id = trailId, name = "Trail1", stations = listOf(station1, station2))
        trailRepository.addTrail(trail)

        viewModel.loadTrail(trailId)
        advanceUntilIdle()

        var gameState = viewModel.gameState.first()
        assertEquals(trailId, gameState.currentTrail?.id)
        assertEquals(0, gameState.currentStationIndex)
        assertEquals(null, gameState.feedback)
        assertEquals(false, gameState.isGameCompleted)

        viewModel.submitAnswer("Answer 1")
        advanceUntilIdle()

        gameState = viewModel.gameState.first()
        assertEquals(1, gameState.currentStationIndex)
        assertEquals(Feedback.COMPLETED, gameState.feedback)
        assertEquals(false, gameState.isGameCompleted)

        viewModel.submitAnswer("Answer 2")
        advanceUntilIdle()

        gameState = viewModel.gameState.first()
        assertEquals(2, gameState.currentStationIndex)
        assertEquals(Feedback.COMPLETED, gameState.feedback)
        assertEquals(true, gameState.isGameCompleted)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}