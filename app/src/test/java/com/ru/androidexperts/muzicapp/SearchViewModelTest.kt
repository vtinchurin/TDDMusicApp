package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState
import org.junit.Before
import org.junit.Test

class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var order: Order
    private lateinit var repository: FakeSearchRepository
    private lateinit var observable: FakeSearchUiObservable
    private lateinit var runAsync: FakeRunAsync
    private lateinit var fragment: FakeFragment

    @Before
    fun setUp() {
        order = Order()
        repository = FakeSearchRepository.Base(order)
        observable = FakeSearchUiObservable.Base(order)
        runAsync = FakeRunAsync.Base()
        viewModel = SearchViewModel(
            repository = repository,
            observable = observable,
            runAsync = runAsync
        )
        fragment = FakeFragment.Base()

        repository.expectTrackList(
            SearchResult(
                term = "Q",
                tracks = listOf<Track>(
                    Track(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        trackUrl = "1",
                        state = PlayStopUiState.Play
                    ),
                    Track(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        trackUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
    }

    @Test
    fun initNoCacheTest() {
        repository.expectTrackList(SearchResult.Init)

        viewModel.init(firstRun = true)
        observable.checkPostUiState(SearchUiState.Progress)
        observable.checkPostUiStateCalledCount(1)
        repository.initCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.checkRegisterCalledCount(1)

        fragment.checkUiState(SearchUiState.Progress)
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(SearchUiState.Init)
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(SearchUiState.Init)
        fragment.checkUiStateCount(2)

        order.check(listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_INIT, OBSERVABLE_POST))
    }

    @Test
    fun initWithCacheTest() {
        viewModel.init(firstRun = true)

        observable.checkPostUiState(SearchUiState.Progress)
        observable.checkPostUiStateCalledCount(1)
        repository.initCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.checkRegisterCalledCount(1)

        fragment.checkUiState(SearchUiState.Progress)
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(
            SearchUiState.Success(
                tracks = listOf<TrackUi>(
                    TrackUi(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        state = PlayStopUiState.Play
                    ),
                    TrackUi(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(
            SearchUiState.Success(
                tracks = listOf<TrackUi>(
                    TrackUi(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        state = PlayStopUiState.Play
                    ),
                    TrackUi(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
        fragment.checkUiStateCount(2)

        order.check(listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_INIT, OBSERVABLE_POST))
    }

    @Test
    fun fetchTest() {
        viewModel.fetch(term = "Q")
        observable.checkPostUiState(SearchUiState.Progress)
        observable.checkPostUiStateCalledCount(1)
        repository.fetchCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(SearchUiState.Progress)
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(
            SearchUiState.Success(
                tracks = listOf<TrackUi>(
                    TrackUi(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        state = PlayStopUiState.Play
                    ),
                    TrackUi(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(
            SearchUiState.Success(
                tracks = listOf<TrackUi>(
                    TrackUi(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        state = PlayStopUiState.Play
                    ),
                    TrackUi(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
        fragment.checkUiStateCount(2)

        order.check(listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_INIT, OBSERVABLE_POST))
    }

    @Test
    fun recreateActivityTest() {
        repository.expectTrackList(isSuccess = false)

        viewModel.fetch(term = "Q")
        observable.checkPostUiState(SearchUiState.Progress)
        observable.checkPostUiStateCalledCount(1)
        repository.fetchCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(SearchUiState.Progress)
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(SearchUiState.ErrorRes())
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(SearchUiState.ErrorRes())
        fragment.checkUiStateCount(2)

        val failFetchOrderList =
            listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_INIT, OBSERVABLE_POST)
        order.check(failFetchOrderList)

        val newInstanceFragment = FakeFragment.Base()
        viewModel.init(firstRun = false)

        observable.checkPostUiStateCalledCount(2)
        repository.fetchCalledCount(1)

        viewModel.startUpdates(observer = newInstanceFragment)
        observable.registerCalledCount(2)
        newInstanceFragment.checkUiState(SearchUiState.Progress)
        newInstanceFragment.checkUiStateCount(1)

        val newInstanceFragmentOrderList = listOf(OBSERVABLE_POST)
        order.check(failFetchOrderList + newInstanceFragmentOrderList)

        repository.expectTrackList(isSuccess = true)
        viewModel.fetch(term = "Q")

        observable.checkPostUiState(SearchUiState.Progress)
        observable.checkPostUiStateCalledCount(3)
        repository.fetchCalledCount(2)

        newInstanceFragment.checkUiState(SearchUiState.Progress)
        newInstanceFragment.checkUiStateCount(3)

        runAsync.returnResult()
        observable.checkPostUiState(
            SearchUiState.Success(
                tracks = listOf<TrackUi>(
                    TrackUi(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        state = PlayStopUiState.Play
                    ),
                    TrackUi(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
        observable.checkPostUiStateCalledCount(3)
        newInstanceFragment.checkUiState(
            SearchUiState.Success(
                tracks = listOf<TrackUi>(
                    TrackUi(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        state = PlayStopUiState.Play
                    ),
                    TrackUi(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
        newInstanceFragment.checkUiStateCount(3)

        val fetchAfterRecreateOrderList =
            listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_INIT, OBSERVABLE_POST)
        order.check(failFetchOrderList + newInstanceFragmentOrderList + fetchAfterRecreateOrderList)
    }

    @Test
    fun togglePlayPauseTrackTest() {
        viewModel.togglePlayPause(trackId = 1L)

        observable.checkPostUiState(
            SearchUiState.Success(
                tracks = listOf<TrackUi>(
                    TrackUi(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        state = PlayStopUiState.Stop
                    ),
                    TrackUi(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
        observable.checkPostUiStateCalledCount(1)
        repository.togglePlayPauseCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(
            SearchUiState.Success(
                tracks = listOf<TrackUi>(
                    TrackUi(
                        id = 1L,
                        artistName = "Q",
                        trackName = "1",
                        artworkUrl = "1",
                        state = PlayStopUiState.Stop
                    ),
                    TrackUi(
                        id = 2L,
                        artistName = "Q",
                        trackName = "2",
                        artworkUrl = "2",
                        state = PlayStopUiState.Play
                    )
                )
            )
        )
        fragment.checkUiStateCount(1)

        order.check(listOf(REPOSITORY_TOGGLE, OBSERVABLE_POST))
    }
}