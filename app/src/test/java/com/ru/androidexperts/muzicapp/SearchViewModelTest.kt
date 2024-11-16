package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.presentation.SearchViewModel
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState
import org.junit.Before
import org.junit.Test

//TODO tests for play/stop track
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
        order = Order()
        repository = FakeSearchRepository.Base(order)
        observable = object : FakeUiObservable.Abstract<SearchUiState>(order)
        runAsync = FakeRunAsync.Base(order)
        player = FakeMusicPlayer.Base(order)
        viewModel = SearchViewModel(
            repository = repository,
            observable = observable,
            runAsync = runAsync,
            player = player
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
    }

    @Test
    fun initNoCacheTest() {
        repository.expectTermCached(termCache = "")

        viewModel.init(firstRun = true)

        observable.assertPostUiStateCalledCount(0)
        repository.assertLoadCalledCount(0)

        viewModel.startUpdates(observer = fragment)

        observable.assertRegisterCalledCount(1)
        fragment.assertCurrentUiStates(listOf())

        order.check(listOf(OBSERVABLE_REGISTER))
    }

    @Test
    fun initWithCacheTest() {
        repository.expectTermCached("Q")
        viewModel.init(firstRun = true)

        observable.assertPostUiStates(listOf(SearchUiState.Load("Q")))
        observable.assertPostUiStateCalledCount(2)

        viewModel.startUpdates(observer = fragment)

        observable.assertRegisterCalledCount(1)
        fragment.assertCurrentUiStates(listOf(SearchUiState.Load("Q")))

        runAsync.returnResult()

        player.assertUpdateTracksUriList(TRACKS_URI_LIST)
        observable.assertPostUiStates(SEARCH_UI_STATE_SUCCESS_BASE)
        observable.assertPostUiStateCalledCount(3)
        fragment.assertCurrentUiStates(
            listOf(
                SearchUiState.Load("Q"),
                SEARCH_UI_STATE_SUCCESS_BASE
            )
        )

        order.check(
            listOf(
                REPOSITORY_TERM,
                OBSERVABLE_POST,
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                OBSERVABLE_REGISTER,
                PLAYER_UPDATE,
                OBSERVABLE_POST
            )
        )
    }

    @Test
    fun loadTest() {
        viewModel.load(term = "Q")
        observable.assertPostUiStates(listOf(SearchUiState.Load("Q")))
        observable.assertPostUiStateCalledCount(1)
        repository.assertLoadCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        observable.assertRegisterCalledCount(1)
        fragment.assertCurrentUiStates(listOf(SearchUiState.Load("Q")))

        runAsync.returnResult()
        player.assertUpdateTracksUriList(TRACKS_URI_LIST)
        observable.assertPostUiStates(listOf(SearchUiState.Load("Q"), SEARCH_UI_STATE_SUCCESS_BASE))
        observable.assertPostUiStateCalledCount(2)
        fragment.assertCurrentUiStates(
            listOf(
                SearchUiState.Load("Q"),
                SEARCH_UI_STATE_SUCCESS_BASE
            )
        )

        order.check(
            listOf(
                OBSERVABLE_POST,
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                PLAYER_UPDATE,
                OBSERVABLE_POST
            )
        )
    }

    @Test
    fun noTrackLoadTest() {
        repository.expectTrackList(listOf<Track>())

        viewModel.load(term = "QQ")

        observable.assertPostUiStates(listOf(SearchUiState.Load("QQ")))
        observable.assertPostUiStateCalledCount(1)
        repository.assertLoadCalledCount(1)

        viewModel.startUpdates(observer = fragment)

        observable.assertRegisterCalledCount(1)
        fragment.assertCurrentUiStates(listOf(SearchUiState.Load("QQ")))

        runAsync.returnResult()

        observable.assertPostUiStates(
            listOf(
                SearchUiState.Load("QQ"),
                SearchUiState.NoTracks("QQ")
            )
        )
        observable.assertPostUiStateCalledCount(2)
        fragment.assertCurrentUiStates(SearchUiState.NoTracks("QQ"))

        order.check(
            listOf(
                OBSERVABLE_POST,
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                OBSERVABLE_POST
            )
        )
    }

    @Test
    fun recreateActivityTest() {
        repository.expectError()

        viewModel.load(term = "Q")

        observable.assertPostUiStates(listOf(SearchUiState.Load("Q")))
        observable.assertPostUiStateCalledCount(1)
        repository.assertLoadCalledCount(1)

        runAsync.returnResult()
        viewModel.startUpdates(observer = fragment)

        observable.assertPostUiStates(
            listOf(
                SearchUiState.Load("Q"),
                SearchUiState.Error("No internet connection")
            )
        )
        observable.assertPostUiStateCalledCount(2)
        fragment.assertCurrentUiStates(listOf(SearchUiState.Error("No internet connection")))

        val failLoadOrderList =
            listOf(
                OBSERVABLE_POST,
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                OBSERVABLE_POST,
                OBSERVABLE_REGISTER
            )
        order.check(failLoadOrderList)

        val newInstanceFragment = FakeFragment.Base()

        viewModel.init(firstRun = false)

        observable.assertPostUiStateCalledCount(2)
        repository.assertLoadCalledCount(1)

        viewModel.startUpdates(observer = newInstanceFragment)
        observable.assertRegisterCalledCount(2)
        newInstanceFragment.assertCurrentUiStates(listOf(SearchUiState.Error("No internet connection")))

        val newInstanceFragmentOrderList = listOf(OBSERVABLE_REGISTER)
        order.check(failLoadOrderList + newInstanceFragmentOrderList)

        repository.expectSuccess()
        viewModel.load(term = "Q")

        observable.assertPostUiStates(
            listOf(
                SearchUiState.Load("Q"),
                SearchUiState.Error("No internet connection"),
                SearchUiState.Load("Q")
            )
        )
        observable.assertPostUiStateCalledCount(3)
        repository.assertLoadCalledCount(2)

        newInstanceFragment.checkUiState(
            listOf(
                SearchUiState.Error("No internet connection"),
                SearchUiState.Load("Q")
            )
        )

        runAsync.returnResult()

        player.assertUpdateTracksUriList(TRACKS_URI_LIST)
        observable.assertPostUiStates(
            listOf(
                SearchUiState.Load("Q"),
                SearchUiState.Error("No internet connection"),
                SearchUiState.Load("Q"),
                SEARCH_UI_STATE_SUCCESS_BASE
            )
        )
        observable.assertPostUiStateCalledCount(4)
        newInstanceFragment.assertCurrentUiStates(
            listOf(
                SearchUiState.Error("No internet connection"),
                SearchUiState.Load("Q"),
                SEARCH_UI_STATE_SUCCESS_BASE
            )
        )

        val loadAfterRecreateOrderList =
            listOf(
                OBSERVABLE_POST,
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                PLAYER_UPDATE,
                OBSERVABLE_POST
            )
        order.check(failLoadOrderList + newInstanceFragmentOrderList + loadAfterRecreateOrderList)
    }

    companion object {
        private val SEARCH_UI_STATE_SUCCESS_BASE = SearchUiState.Success(
            listOf<RecyclerItem>(
                RecyclerItem.TrackUi(
                    id = 1L,
                    sourceUrl = "1",
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = "1",
                    state = PlayStopUiState.Stop
                ),
                RecyclerItem.TrackUi(
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
                RecyclerItem.TrackUi(
                    id = 1L,
                    sourceUrl = "1",
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = "1",
                    state = PlayStopUiState.Play
                ),
                RecyclerItem.TrackUi(
                    id = 2L,
                    sourceUrl = "2",
                    authorName = "Q",
                    trackTitle = "2",
                    coverUrl = "2",
                    state = PlayStopUiState.Stop
                )
            )
        )
        private val TRACKS_URI_LIST = listOf(
            Pair(1L, "1"),
            Pair(2L, "2")
        )
    }
}