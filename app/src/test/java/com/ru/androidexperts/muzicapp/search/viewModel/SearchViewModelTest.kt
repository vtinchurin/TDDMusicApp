package com.ru.androidexperts.muzicapp.search.viewModel

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.SearchViewModel
import com.ru.androidexperts.muzicapp.search.presentation.adapter.SearchItem
import com.ru.androidexperts.muzicapp.search.presentation.mappers.PlayerMapper
import com.ru.androidexperts.muzicapp.search.presentation.mappers.UiMapper
import com.ru.androidexperts.muzicapp.search.presentation.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUiState
import com.ru.androidexperts.muzicapp.search.viewModel.fakes.FakeFragment
import com.ru.androidexperts.muzicapp.search.viewModel.fakes.FakeMusicPlayer
import com.ru.androidexperts.muzicapp.search.viewModel.fakes.FakeRunAsync
import com.ru.androidexperts.muzicapp.search.viewModel.fakes.FakeSearchRepository
import com.ru.androidexperts.muzicapp.search.viewModel.fakes.FakeUiObservable
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var order: Order
    private lateinit var repository: FakeSearchRepository
    private lateinit var observable: FakeUiObservable<SearchUiState>
    private lateinit var runAsync: FakeRunAsync
    private lateinit var player: FakeMusicPlayer
    private lateinit var fragment: FakeFragment

    @Before
    fun setUp() {
        order = Order.Base()
        repository = FakeSearchRepository.Base(order)
        observable = FakeUiObservable.Base(order)
        runAsync = FakeRunAsync.Base(order)
        player = FakeMusicPlayer.Base(order)
        fragment = FakeFragment.Base(order)
        viewModel = SearchViewModel(
            repository = repository,
            observable = observable,
            runAsync = runAsync,
            player = player,
            toUi = UiMapper.Base(),
            toPlayList = PlayerMapper.Base()
        )

        repository.expectTrackList(
            listOf<TrackModel>(
                TrackModel.Base(
                    id = 1L, authorName = "Q", trackTitle = "1", coverUrl = "1", sourceUrl = "1"
                ), TrackModel.Base(
                    id = 2L, authorName = "Q", trackTitle = "2", coverUrl = "2", sourceUrl = "2"
                )
            )
        )
    }

    @Test
    fun initNoCacheTest() {
        repository.expectTermCached(termCache = "")

        /* Action */
        viewModel.init(isFirstRun = true)

        /* Assertion */
        player.assertInitCalled()
        repository.assertLastTermCachedCalled()

        /* Action */
        viewModel.startUpdates(observer = fragment)

        /* Assertion */
        observable.assertUpdateCalled(expectedObserver = fragment) // register + post
        fragment.assertUpdateCalled(expectedState = SearchUiState.Initial())

        order.assertTraceSize(5)
    }

    @Test
    fun initWithCacheTest() {
        val initUiState = SearchUiState.Initial(inputText = "Q", SearchItem.ProgressUi)
        repository.expectTermCached(termCache = "Q")

        /* Action */
        viewModel.init(isFirstRun = true)

        /* Assertion */
        player.assertInitCalled()
        repository.assertLastTermCachedCalled()
        observable.assertUpdateUiCalled(expectedInput = "Q")
        observable.assertUpdateUiCalled(initUiState, isEmptyObserver = true)
        runAsync.assertHandleAsyncCalled()
        repository.assertLoadCalled(term = "Q")

        /* Action */
        viewModel.startUpdates(observer = fragment)//!!!

        /* Assertion */
        observable.assertUpdateCalled(fragment)
        fragment.assertUpdateCalled(expectedState = initUiState)

        /* Action */
        runAsync.returnResult()

        /* Assertion */
        player.assertUpdateCalled()
        observable.assertUpdateUiCalled(SEARCH_UI_STATE_SUCCESS_BASE)
        fragment.assertUpdateCalled(SEARCH_UI_STATE_SUCCESS_BASE)

        order.assertTraceSize(13)
    }

    @Test
    fun loadTest() {
        /* Action */
        viewModel.init(isFirstRun = true)

        /* Assertion */
        player.assertInitCalled()
        repository.assertLastTermCachedCalled()

        /* Action */
        viewModel.fetch(term = "Q")

        /* Assertion */
        observable.assertUpdateUiCalled(SearchUiState.Loading, isEmptyObserver = true)
        runAsync.assertHandleAsyncCalled()
        repository.assertLoadCalled("Q")

        /* Action */
        viewModel.startUpdates(observer = fragment)

        /* Assertion */
        observable.assertUpdateCalled(fragment)
        fragment.assertUpdateCalled(expectedState = SearchUiState.Loading)

        /* Action */
        runAsync.returnResult()

        /* Assertion */
        player.assertUpdateCalled()
        observable.assertUpdateUiCalled(SEARCH_UI_STATE_SUCCESS_BASE)
        fragment.assertUpdateCalled(SEARCH_UI_STATE_SUCCESS_BASE)

        order.assertTraceSize(12)
    }

    @Test
    fun noTrackLoadTest() {
        repository.expectTrackList(emptyList())

        /* Action */
        viewModel.fetch(term = "QQ")

        /* Assertion */
        observable.assertUpdateUiCalled(SearchUiState.Loading, isEmptyObserver = true)
        runAsync.assertHandleAsyncCalled()
        repository.assertLoadCalled(term = "QQ")

        /* Action */
        viewModel.startUpdates(observer = fragment)

        /* Assertion */
        observable.assertUpdateCalled(fragment)
        fragment.assertUpdateCalled(expectedState = SearchUiState.Loading)

        /* Action */
        runAsync.returnResult()

        /* Assertion */
        player.assertUpdateCalled()
        observable.assertUpdateUiCalled(SearchUiState.NoTracks)
        fragment.assertUpdateCalled(SearchUiState.NoTracks)

        order.assertTraceSize(10)

    }

    @Test
    fun `recreate Activity and retry test`() {
        initNoCacheTest()
        repository.expectError()

        /* Action */
        viewModel.fetch(term = "Q")

        /* Assertion */
        observable.assertUpdateUiCalled(expectedState = SearchUiState.Loading)
        fragment.assertUpdateCalled(expectedState = SearchUiState.Loading)
        runAsync.assertHandleAsyncCalled()
        repository.assertLoadCalled("Q")

        runAsync.returnResult()

        /* Assertion */
        player.assertUpdateCalled()
        observable.assertUpdateUiCalled(SEARCH_UI_STATE_ERROR_NO_INTERNET)
        fragment.assertUpdateCalled(SEARCH_UI_STATE_ERROR_NO_INTERNET)

        order.assertTraceSize(14)

        val newInstanceFragment = FakeFragment.Base(order)

        /* Action */
        viewModel.init(isFirstRun = false)

        /* Assertion */
        order.assertTraceSize(14)

        /* Action */
        viewModel.startUpdates(observer = newInstanceFragment)

        /* Assertion */
        observable.assertUpdateCalled(newInstanceFragment)
        newInstanceFragment.assertUpdateCalled(SEARCH_UI_STATE_ERROR_NO_INTERNET)

        repository.expectSuccess()

        /* Action */
        viewModel.retry()

        /* Assertion */
        repository.assertLastTermCachedCalled()
        observable.assertUpdateUiCalled(SearchUiState.Loading)
        fragment.assertUpdateCalled(SearchUiState.Loading)
        runAsync.assertHandleAsyncCalled()
        repository.assertLoadCalled("Q")

        runAsync.returnResult()

        /* Assertion */
        player.assertUpdateCalled()
        observable.assertUpdateUiCalled(SEARCH_UI_STATE_SUCCESS_BASE)
        fragment.assertUpdateCalled(SEARCH_UI_STATE_SUCCESS_BASE)

        order.assertTraceSize(27)

    }

    @Test
    fun playPauseTest() {
        loadTest()

        /* Action */
        viewModel.play(trackId = 1)

        player.assertPlayCalled(trackId = 1)
        observable.assertPlayCalled(trackId = 1)
        observable.assertUpdateUiCalled(SEARCH_STATE_PLAY_FIRST)
        fragment.assertUpdateCalled(SEARCH_STATE_PLAY_FIRST)

        /* Action */
        viewModel.pause()

        /* Assertion */
        player.assertPauseCalled()
        observable.assertStopCalled()
        observable.assertUpdateUiCalled(SEARCH_UI_STATE_SUCCESS_BASE)
        fragment.assertUpdateCalled(SEARCH_UI_STATE_SUCCESS_BASE)

        /* Action */
        viewModel.play(trackId = 2)

        /* Assertion */
        player.assertPlayCalled(trackId = 2)
        observable.assertPlayCalled(trackId = 2)
        observable.assertUpdateUiCalled(SEARCH_STATE_PLAY_SECOND)
        fragment.assertUpdateCalled(SEARCH_STATE_PLAY_SECOND)

        /* Action */
        viewModel.pause()

        /* Assertion */
        player.assertPauseCalled()
        observable.assertStopCalled()
        observable.assertUpdateUiCalled(SEARCH_UI_STATE_SUCCESS_BASE)
        fragment.assertUpdateCalled(SEARCH_UI_STATE_SUCCESS_BASE)

        order.assertTraceSize(32)
    }

    @Test
    fun `test with process death handle`() {
        loadTest()
        setUp()
        repository.expectTermCached("Q")

        /* Action */
        viewModel.init(isFirstRun = false)

        /* Assertion */
        player.assertInitCalled()
        repository.assertLastTermCachedCalled()
        observable.assertUpdateUiCalled(expectedInput = "Q")
        observable.assertUpdateUiCalled(
            SearchUiState.Initial("Q", SearchItem.ProgressUi), isEmptyObserver = true
        )
        runAsync.assertHandleAsyncCalled()
        repository.assertLoadCalled("Q")

        /* Action */
        runAsync.returnResult()

        /* Assertion */
        player.assertUpdateCalled()
        observable.assertUpdateUiCalled(
            SearchUiState.Initial(inputText = "Q", recyclerState = SUCCESS_TRACKS),
            isEmptyObserver = true
        )

        /* Action */
        viewModel.startUpdates(observer = fragment)

        /* Assertion */
        observable.assertUpdateCalled(fragment)
        fragment.assertUpdateCalled(
            SearchUiState.Initial(inputText = "Q", recyclerState = SUCCESS_TRACKS)
        )

        order.assertTraceSize(11)
    }

    companion object {
        private val SEARCH_UI_STATE_ERROR_NO_INTERNET = SearchUiState.Error(errorResId = -777)

        private val SUCCESS_TRACKS = listOf<SearchItem.Track>(
            SearchItem.TrackUi(
                trackId = 1L,
                authorName = "Q",
                trackTitle = "1",
                coverUrl = TrackImageUiState.Base("1"),
                isPlaying = PlayStopUiState.Stop
            ), SearchItem.TrackUi(
                trackId = 2L,
                authorName = "Q",
                trackTitle = "2",
                coverUrl = TrackImageUiState.Base("2"),
                isPlaying = PlayStopUiState.Stop
            )
        )
        private val SEARCH_UI_STATE_SUCCESS_BASE = SearchUiState.Success(SUCCESS_TRACKS)

        private val SEARCH_STATE_PLAY_FIRST = SearchUiState.Success(
            listOf<SearchItem.Track>(
                SearchItem.TrackUi(
                    trackId = 1L,
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = TrackImageUiState.Base("1", isPlaying = true),
                    isPlaying = PlayStopUiState.Play
                ), SearchItem.TrackUi(
                    trackId = 2L,
                    authorName = "Q",
                    trackTitle = "2",
                    coverUrl = TrackImageUiState.Base("2"),
                    isPlaying = PlayStopUiState.Stop
                )
            )
        )
        private val SEARCH_STATE_PLAY_SECOND = SearchUiState.Success(
            listOf<SearchItem.Track>(
                SearchItem.TrackUi(
                    trackId = 1L,
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = TrackImageUiState.Base("1"),
                    isPlaying = PlayStopUiState.Stop
                ), SearchItem.TrackUi(
                    trackId = 2L,
                    authorName = "Q",
                    trackTitle = "2",
                    coverUrl = TrackImageUiState.Base("2", isPlaying = true),
                    isPlaying = PlayStopUiState.Play
                )
            )
        )
    }
}