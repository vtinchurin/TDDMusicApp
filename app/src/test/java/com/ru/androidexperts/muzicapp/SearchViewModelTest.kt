package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState
import org.junit.Before
import org.junit.Test

//TODO tests for play/stop track
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private lateinit var order: Order
    private lateinit var repository: FakeSearchRepository
    private lateinit var playlist: FakePlaylist
    private lateinit var observable: FakeSearchUiObservable
    private lateinit var runAsync: FakeRunAsync
    private lateinit var fragment: FakeFragment

    @Before
    fun setUp() {
        order = Order()
        repository = FakeSearchRepository.Base(order)
        playlist = FakePlaylist.Base()
        observable = FakeSearchUiObservable.Base(order)
        runAsync = FakeRunAsync.Base()
        viewModel = SearchViewModel(
            repository = repository,
            playlist = playlist,
            observable = observable,
            runAsync = runAsync
        )
        fragment = FakeFragment.Base()

        repository.expectTrackList(
            listOf<Track>(
                Track(
                    id = 1L,
                    authorName = "1",
                    trackTitle = "1",
                    coverUrl = "1",
                    trackUrl = "1"
                ),
                Track(
                    id = 2L,
                    authorName = "1",
                    trackTitle = "2",
                    coverUrl = "2",
                    trackUrl = "2"
                )
            )
        )
        repository.expectTermCache(termCache = "Q")
    }

    @Test
    fun initNoCacheTest() {
        repository.expectTermCache(termCache = "")

        val actualTermCache = viewModel.init(firstRun = true)
        repository.checkCacheTerm(actualTermCache)

        observable.checkPostUiState(SearchUiState.Loading)
        observable.checkPostUiStateCalledCount(1)
        repository.initCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.checkRegisterCalledCount(1)

        fragment.checkUiState(SearchUiState.Loading)
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(SearchUiState.Initial(term = ""))
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(SearchUiState.Initial(term = ""))
        fragment.checkUiStateCount(2)

        order.check(listOf(OBSERVABLE_POST, REPOSITORY_TERM, OBSERVABLE_POST))
    }

    @Test
    fun initWithCacheTest() {
        val actualTermCache = viewModel.init(firstRun = true)
        repository.checkCacheTerm(actualTermCache)

        observable.checkPostUiState(SearchUiState.Initial("Q"))
        observable.checkPostUiStateCalledCount(1)
        repository.initCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.checkRegisterCalledCount(1)

        fragment.checkUiState(SearchUiState.Initial("Q"))
        fragment.checkUiStateCount(1)

        observable.checkPostUiState(SearchUiState.Loading)
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(SearchUiState.Loading)
        fragment.checkUiStateCount(2)

        runAsync.returnResult()
        playlist.checkUpdateTracksUriList(TRACKS_URI_LIST)
        observable.checkPostUiState(SEARCH_STATE_BASE)
        observable.checkPostUiStateCalledCount(3)
        fragment.checkUiState(SEARCH_STATE_BASE)
        fragment.checkUiStateCount(3)

        order.check(
            listOf(
                REPOSITORY_TERM,
                OBSERVABLE_POST,
                OBSERVABLE_POST,
                RYNASYNC_HANDLE,
                REPOSITORY_LOAD,
                PLAYLIST_UPDATE,
                OBSERVABLE_POST
            )
        )
    }

    @Test
    fun loadTest() {
        viewModel.load(term = "Q")
        observable.checkPostUiState(SearchUiState.Loading)
        observable.checkPostUiStateCalledCount(1)
        repository.loadCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(SearchUiState.Loading)
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        playlist.checkUpdateTracksUriList(TRACKS_URI_LIST)
        observable.checkPostUiState(SEARCH_STATE_BASE)
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(SEARCH_STATE_BASE)
        fragment.checkUiStateCount(2)

        order.check(
            listOf(
                OBSERVABLE_POST,
                RYNASYNC_HANDLE,
                REPOSITORY_LOAD,
                PLAYLIST_UPDATE,
                OBSERVABLE_POST
            )
        )
    }

    @Test
    fun noTrackLoadTest() {
        repository.expectTrackList(listOf<Track>())

        viewModel.load(term = "QQ")
        observable.checkPostUiState(SearchUiState.Loading)
        observable.checkPostUiStateCalledCount(1)
        repository.loadCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(SearchUiState.Loading)
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(SearchUiState.NoTracks)
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(SearchUiState.NoTracks)
        fragment.checkUiStateCount(2)

        order.check(listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_LOAD, OBSERVABLE_POST))
    }

    @Test
    fun recreateActivityTest() {
        repository.expectError()

        viewModel.load(term = "Q")
        observable.checkPostUiState(SearchUiState.Loading)
        observable.checkPostUiStateCalledCount(1)
        repository.loadCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(SearchUiState.Loading)
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(SearchUiState.Error("No internet connection"))
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(SearchUiState.Error("No internet connection"))
        fragment.checkUiStateCount(2)

        val failLoadOrderList =
            listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_LOAD, OBSERVABLE_POST)
        order.check(failLoadOrderList)

        val newInstanceFragment = FakeFragment.Base()
        viewModel.init(firstRun = false)

        observable.checkPostUiStateCalledCount(2)
        repository.loadCalledCount(1)

        viewModel.startUpdates(observer = newInstanceFragment)
        observable.registerCalledCount(2)
        newInstanceFragment.checkUiState(SearchUiState.Loading)
        newInstanceFragment.checkUiStateCount(1)

        val newInstanceFragmentOrderList = listOf(OBSERVABLE_POST)
        order.check(failLoadOrderList + newInstanceFragmentOrderList)

        repository.expectSuccess()
        viewModel.load(term = "Q")

        observable.checkPostUiState(SearchUiState.Loading)
        observable.checkPostUiStateCalledCount(3)
        repository.loadCalledCount(2)

        newInstanceFragment.checkUiState(SearchUiState.Loading)
        newInstanceFragment.checkUiStateCount(3)

        runAsync.returnResult()
        playlist.checkUpdateTracksUriList(TRACKS_URI_LIST)
        observable.checkPostUiState(SEARCH_STATE_BASE)
        observable.checkPostUiStateCalledCount(3)
        newInstanceFragment.checkUiState(SEARCH_STATE_BASE)
        newInstanceFragment.checkUiStateCount(3)

        val loadAfterRecreateOrderList =
            listOf(
                OBSERVABLE_POST,
                RYNASYNC_HANDLE,
                REPOSITORY_LOAD,
                PLAYLIST_UPDATE,
                OBSERVABLE_POST
            )
        order.check(failLoadOrderList + newInstanceFragmentOrderList + loadAfterRecreateOrderList)
    }

    companion object {
        private val SEARCH_STATE_BASE = SearchUiState.Success(
            listOf<RecyclerItem>(
                TrackUi(
                    id = 1L,
                    sourceUrl = "1",
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = "1",
                    state = PlayStopUiState.Stop
                ),
                TrackUi(
                    id = 2L,
                    sourceUrl = "2",
                    authorName = "Q",
                    trackTitle = "2",
                    coverUrl = "2",
                    state = PlayStopUiState.Stop
                )
            )
        )
        private val SEARCH_STATE_PLAY_FIRST = SearchUiState.Success(
            listOf<RecyclerItem>(
                TrackUi(
                    id = 1L,
                    sourceUrl = "1",
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = "1",
                    state = PlayStopUiState.Play
                ),
                TrackUi(
                    id = 2L,
                    sourceUrl = "2",
                    authorName = "Q",
                    trackTitle = "2",
                    coverUrl = "2",
                    state = PlayStopUiState.Stop
                )
            )
        )
        private val TRACKS_URI_LIST = listOf("1", "2")
    }
}