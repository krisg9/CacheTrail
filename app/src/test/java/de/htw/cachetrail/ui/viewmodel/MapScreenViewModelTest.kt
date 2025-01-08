package de.htw.cachetrail.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import de.htw.cachetrail.data.model.Station
import de.htw.cachetrail.data.model.Trail
import de.htw.cachetrail.data.repository.ITrailsRepository
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
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.reset

@OptIn(ExperimentalCoroutinesApi::class)
class MapScreenViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MapScreenViewModel
    private lateinit var trailRepository: ITrailsRepository
    private lateinit var trailService: CacheTrailService
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        trailRepository = mock(ITrailsRepository::class.java)
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
    fun `test game state after loading trail`() = runTest {
        val trailId = "trail1"
        val station = Station(
            id = "station1",
            latitude = 52.0,
            longitude = 13.0,
            question = "Question?",
            answer = "Answer"
        )
        val trail = Trail(id = trailId, name = "Trail1", stations = listOf(station))

        `when`(trailRepository.getAllTrails()).thenReturn(flowOf(listOf(trail)))

        viewModel.loadTrail(trailId)
        advanceUntilIdle()

        val gameState = viewModel.gameState.first()
        assertEquals(trailId, gameState.currentTrail?.id)
        assertEquals(0, gameState.currentStationIndex)
        assertEquals(null, gameState.feedback)
        assertEquals(false, gameState.isGameCompleted)
    }

    @Test
    fun `test submit correct answer`() = runTest {
        val trailId = "trail1"
        val station = Station(
            id = "station1",
            latitude = 52.0,
            longitude = 13.0,
            question = "Question?",
            answer = "Answer"
        )
        val trail = Trail(id = trailId, name = "Trail1", stations = listOf(station))

        `when`(trailRepository.getAllTrails()).thenReturn(flowOf(listOf(trail)))

        viewModel.loadTrail(trailId)
        advanceUntilIdle()

        viewModel.submitAnswer("Answer")

        val gameState = viewModel.gameState.first()
        assertEquals(1, gameState.currentStationIndex)
        assertEquals(Feedback.COMPLETED, gameState.feedback)
        assertEquals(true, gameState.isGameCompleted)
    }

    @Test
    fun `test submit incorrect answer`() = runTest {
        val trailId = "trail1"
        val station = Station(
            id = "station1",
            latitude = 52.0,
            longitude = 13.0,
            question = "Question?",
            answer = "Answer"
        )
        val trail = Trail(id = trailId, name = "Trail1", stations = listOf(station))

        `when`(trailRepository.getAllTrails()).thenReturn(flowOf(listOf(trail)))

        viewModel.loadTrail(trailId)
        advanceUntilIdle()

        viewModel.submitAnswer("WrongAnswer")
        advanceUntilIdle()

        val gameState = viewModel.gameState.first()
        assertEquals(0, gameState.currentStationIndex)
        assertEquals(Feedback.INCORRECT, gameState.feedback)
    }

    @Test
    fun `test game reset`() = runTest {
        val trailId = "trail1"
        val station = Station(
            id = "station1",
            latitude = 52.0,
            longitude = 13.0,
            question = "Question?",
            answer = "Answer"
        )
        val trail = Trail(id = trailId, name = "Trail1", stations = listOf(station))

        `when`(trailRepository.getAllTrails()).thenReturn(flowOf(listOf(trail)))

        viewModel.loadTrail(trailId)
        advanceUntilIdle()
        viewModel.submitAnswer("Answer")
        advanceUntilIdle()

        val newTrailId = "newTrail"
        val newStation = Station(
            id = "station2",
            latitude = 52.1,
            longitude = 13.1,
            question = "New Question?",
            answer = "New Answer"
        )
        val newTrail = Trail(id = newTrailId, name = "New Trail", stations = listOf(newStation))

        `when`(trailRepository.getAllTrails()).thenReturn(flowOf(listOf(newTrail)))

        viewModel.loadTrail(newTrailId)
        advanceUntilIdle()

        val gameState = viewModel.gameState.first()
        assertEquals(newTrailId, gameState.currentTrail?.id)
        assertEquals(0, gameState.currentStationIndex)
        assertEquals(null, gameState.feedback)
        assertEquals(false, gameState.isGameCompleted)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        reset(trailRepository)
    }
}