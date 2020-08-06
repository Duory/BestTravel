package makov.besttravel.search.ui.search

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import makov.besttravel.R
import makov.besttravel.search.domain.SuggestionsInteractor
import makov.besttravel.search.domain.model.Airport
import makov.besttravel.whenever
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AirportSearchPresenterTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var presenter: AirportSearchPresenter

    @Mock
    lateinit var interactor: SuggestionsInteractor

    @Mock
    lateinit var airportSearchView: AirportSearchView

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        presenter = AirportSearchPresenter(interactor).apply {
            attachView(airportSearchView)
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun searchForText_shouldShowResult() = testDispatcher.runBlockingTest {
        whenever(interactor.getCitySuggestions(searchString)).thenReturn(airports)
        presenter.searchForText(searchString)
        advanceUntilIdle()
        inOrder(airportSearchView).run {
            verify(airportSearchView).showProgress(true)
            verify(airportSearchView).showSuggestions(airports)
            verify(airportSearchView).showProgress(false)
        }
        verify(airportSearchView, never()).showError(errorRes)
    }

    @Test
    fun searchForText_shouldShowError() = testDispatcher.runBlockingTest {
        whenever(interactor.getCitySuggestions(searchString)).thenThrow(RuntimeException())
        presenter.searchForText(searchString)
        advanceUntilIdle()
        inOrder(airportSearchView).run {
            verify(airportSearchView).showProgress(true)
            verify(airportSearchView).showError(errorRes)
            verify(airportSearchView).showProgress(false)
        }
        verify(airportSearchView, never()).showSuggestions(airports)
    }

    @Test
    fun searchForText_shouldNotShowErrorOnCancellation() = testDispatcher.runBlockingTest {
        whenever(interactor.getCitySuggestions(searchString)).thenThrow(CancellationException())
        presenter.searchForText(searchString)
        advanceUntilIdle()
        inOrder(airportSearchView).run {
            verify(airportSearchView).showProgress(true)
            verify(airportSearchView).showProgress(false)
        }
        verify(airportSearchView, never()).showError(errorRes)
        verify(airportSearchView, never()).showSuggestions(airports)
    }

    @Test
    fun searchForText_shouldShowResultWhenRunMultipleTimes() = testDispatcher.runBlockingTest {
        whenever(interactor.getCitySuggestions(searchString)).thenReturn(airports)
        presenter.searchForText(searchString)
        presenter.searchForText(searchString)
        presenter.searchForText(searchString)
        advanceUntilIdle()
        inOrder(airportSearchView).run {
            verify(airportSearchView, atLeastOnce()).showProgress(true)
            verify(airportSearchView, atLeastOnce()).showProgress(false)
        }
        verify(airportSearchView).showSuggestions(airports)
        verify(airportSearchView, never()).showError(errorRes)
    }

    private val searchString = "test"
    private val airports = listOf(Airport("City Name", "TTT", 0.0, 0.0))
    private val errorRes = R.string.error_default_msg
}