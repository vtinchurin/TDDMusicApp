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

        observable.checkPostUiState(listOf<UiElement>(UiElement.Progress))
        observable.checkPostUiStateCalledCount(1)
        repository.initCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.checkRegisterCalledCount(1)

        fragment.checkUiState(listOf<UiElement>(UiElement.Progress))
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(listOf<UiElement.TrackUi>())
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(listOf<UiElement.TrackUi>())
        fragment.checkUiStateCount(2)

        order.check(listOf(OBSERVABLE_POST, REPOSITORY_TERM, OBSERVABLE_POST))
    }

    @Test
    fun initWithCacheTest() {
        val actualTermCache = viewModel.init(firstRun = true)
        repository.checkCacheTerm(actualTermCache)

        observable.checkPostUiState(listOf<UiElement>(UiElement.Progress))
        observable.checkPostUiStateCalledCount(1)
        repository.initCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.checkRegisterCalledCount(1)

        fragment.checkUiState(listOf<UiElement>(UiElement.Progress))
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(TRACK_LIST_BASE)
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(TRACK_LIST_BASE)
        fragment.checkUiStateCount(2)

        order.check(
            listOf(
                OBSERVABLE_POST,
                REPOSITORY_TERM,
                RYNASYNC_HANDLE,
                REPOSITORY_LOAD,
                OBSERVABLE_POST
            )
        )
    }

    @Test
    fun loadTest() {
        viewModel.load(term = "Q")
        observable.checkPostUiState(listOf<UiElement>(UiElement.Progress))
        observable.checkPostUiStateCalledCount(1)
        repository.loadCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(listOf<UiElement>(UiElement.Progress))
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(TRACK_LIST_BASE)
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(TRACK_LIST_BASE)
        fragment.checkUiStateCount(2)

        order.check(listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_LOAD, OBSERVABLE_POST))
    }

    @Test
    fun noTrackLoadTest() {
        repository.expectTrackList(listOf<UiElement>())

        viewModel.load(term = "QQ")
        observable.checkPostUiState(listOf<UiElement>(UiElement.Progress))
        observable.checkPostUiStateCalledCount(1)
        repository.loadCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(listOf<UiElement>(UiElement.Progress))
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(listOf<UiElement>(UiElement.NoTrack))
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(listOf<UiElement>(UiElement.NoTrack))
        fragment.checkUiStateCount(2)

        order.check(listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_LOAD, OBSERVABLE_POST))
    }

    @Test
    fun recreateActivityTest() {
        repository.expectError()

        viewModel.load(term = "Q")
        observable.checkPostUiState(listOf<UiElement>(UiElement.Progress))
        observable.checkPostUiStateCalledCount(1)
        repository.loadCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(listOf<UiElement>(UiElement.Progress))
        fragment.checkUiStateCount(1)

        runAsync.returnResult()
        observable.checkPostUiState(listOf<UiElement>(UiElement.Error("No internet connection")))
        observable.checkPostUiStateCalledCount(2)
        fragment.checkUiState(listOf<UiElement>(UiElement.Error("No internet connection")))
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
        newInstanceFragment.checkUiState(listOf<UiElement>(UiElement.Progress))
        newInstanceFragment.checkUiStateCount(1)

        val newInstanceFragmentOrderList = listOf(OBSERVABLE_POST)
        order.check(failLoadOrderList + newInstanceFragmentOrderList)

        repository.expectSuccess()
        viewModel.load(term = "Q")

        observable.checkPostUiState(listOf<UiElement>(UiElement.Progress))
        observable.checkPostUiStateCalledCount(3)
        repository.loadCalledCount(2)

        newInstanceFragment.checkUiState(listOf<UiElement>(UiElement.Progress))
        newInstanceFragment.checkUiStateCount(3)

        runAsync.returnResult()
        observable.checkPostUiState(TRACK_LIST_BASE)
        observable.checkPostUiStateCalledCount(3)
        newInstanceFragment.checkUiState(TRACK_LIST_BASE)
        newInstanceFragment.checkUiStateCount(3)

        val loadAfterRecreateOrderList =
            listOf(OBSERVABLE_POST, RYNASYNC_HANDLE, REPOSITORY_LOAD, OBSERVABLE_POST)
        order.check(failLoadOrderList + newInstanceFragmentOrderList + loadAfterRecreateOrderList)
    }

    @Test
    fun togglePlayPauseTrackTest() {
        viewModel.togglePlayPause(trackId = 1L)

        observable.checkPostUiState(TRACK_LIST_PLAY_FIRST)
        observable.checkPostUiStateCalledCount(1)
        repository.togglePlayPauseCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.registerCalledCount(1)
        fragment.checkUiState(TRACK_LIST_PLAY_FIRST)
        fragment.checkUiStateCount(1)

        order.check(listOf(REPOSITORY_TOGGLE, OBSERVABLE_POST))
    }

    companion object {
        private val TRACK_LIST_BASE = listOf<UiElement>(
            TrackUi(
                id = 1L,
                authorName = "Q",
                trackTitle = "1",
                coverUrl = "1",
                state = PlayStopUiState.Stop
            ),
            TrackUi(
                id = 2L,
                authorName = "Q",
                trackTitle = "2",
                coverUrl = "2",
                state = PlayStopUiState.Stop
            )
        )
        private val TRACK_LIST_PLAY_FIRST = listOf<UiElement>(
            TrackUi(
                id = 1L,
                authorName = "Q",
                trackTitle = "1",
                coverUrl = "1",
                state = PlayStopUiState.Play
            ),
            TrackUi(
                id = 2L,
                authorName = "Q",
                trackTitle = "2",
                coverUrl = "2",
                state = PlayStopUiState.Stop
            )
        )
    }
}