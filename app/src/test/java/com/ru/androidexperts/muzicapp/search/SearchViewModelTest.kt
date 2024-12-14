package com.ru.androidexperts.muzicapp.search

import com.ru.androidexperts.muzicapp.search.fakes.FakeFragment
import com.ru.androidexperts.muzicapp.search.fakes.FakeMusicPlayer
import com.ru.androidexperts.muzicapp.search.fakes.FakeRunAsync
import com.ru.androidexperts.muzicapp.search.fakes.FakeSearchRepository
import com.ru.androidexperts.muzicapp.search.fakes.FakeUiObservable
import com.ru.androidexperts.muzicapp.search.fakes.OBSERVABLE_POST
import com.ru.androidexperts.muzicapp.search.fakes.OBSERVABLE_REGISTER
import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.search.fakes.PLAYER_INIT
import com.ru.androidexperts.muzicapp.search.fakes.PLAYER_UPDATE
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.search.fakes.REPOSITORY_LOAD
import com.ru.androidexperts.muzicapp.search.fakes.REPOSITORY_TERM
import com.ru.androidexperts.muzicapp.search.fakes.RUN_ASYNC_HANDLE
import com.ru.androidexperts.muzicapp.search.fakes.RUN_ASYNC_RETURN_RESULT
import com.ru.androidexperts.muzicapp.search.domain.model.TrackModel
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.SearchViewModel
import com.ru.androidexperts.muzicapp.search.presentation.adapter.SearchItem
import com.ru.androidexperts.muzicapp.search.presentation.mappers.PlayerMapper
import com.ru.androidexperts.muzicapp.search.presentation.mappers.UiMapper
import com.ru.androidexperts.muzicapp.search.presentation.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUiState
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
        order = Order()
        repository = FakeSearchRepository.Base(order)
        observable = FakeUiObservable.Base(order)
        runAsync = FakeRunAsync.Base(order)
        player = FakeMusicPlayer.Base(order)
        viewModel = SearchViewModel(
            repository = repository,
            observable = observable,
            runAsync = runAsync,
            player = player,
            toUi = UiMapper.Base(),
            toPlayList = PlayerMapper.Base()
        )
        fragment = FakeFragment.Base()

        repository.expectTrackList(
            listOf<TrackModel>(
                TrackModel.Base(
                    id = 1L,
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = "1",
                    sourceUrl = "1"
                ),
                TrackModel.Base(
                    id = 2L,
                    authorName = "Q",
                    trackTitle = "2",
                    coverUrl = "2",
                    sourceUrl = "2"
                )
            )
        )
    }

    @Test
    fun initNoCacheTest() {
        repository.expectTermCached(termCache = "")

        viewModel.init(isFirstRun = true)

        observable.assertCurrentUiState(SearchUiState.Initial())
        repository.assertLoadCalledCount(0)

        viewModel.startUpdates(observer = fragment)

        observable.assertCurrentUiState(SearchUiState.Initial())
        fragment.assertCurrentUiState(SearchUiState.Initial())

        observable.assertUiStatesHistory(
            listOf(SearchUiState.Initial())
        )
        order.check(
            listOf(
                PLAYER_INIT,
                REPOSITORY_TERM,
                OBSERVABLE_REGISTER,
                OBSERVABLE_POST
            )
        )
    }

    @Test
    fun initWithCacheTest() {
        repository.expectTermCached(termCache = "Q")
        viewModel.init(isFirstRun = true)

        val initUiState = SearchUiState.Initial(inputText = "Q", SearchItem.ProgressUi)

        observable.assertCurrentUiState(initUiState)
        repository.assertLoadCalledCount(1)

        viewModel.startUpdates(observer = fragment)

        observable.assertCurrentUiState(initUiState)
        fragment.assertCurrentUiState(initUiState)

        runAsync.returnResult()

        player.assertUpdateTracksUriList(TRACKS_URI_LIST)
        observable.assertCurrentUiState(SEARCH_UI_STATE_SUCCESS_BASE)
        fragment.assertCurrentUiState(SEARCH_UI_STATE_SUCCESS_BASE)

        observable.assertUiStatesHistory(
            listOf(
                SearchUiState.Initial(),
                initUiState,
                SEARCH_UI_STATE_SUCCESS_BASE
            )
        )
        order.check(
            listOf(
                PLAYER_INIT,
                REPOSITORY_TERM,
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                OBSERVABLE_REGISTER,
                OBSERVABLE_POST,
                RUN_ASYNC_RETURN_RESULT,
                PLAYER_UPDATE,
                OBSERVABLE_POST,
            )
        )
    }

    @Test
    fun loadTest() {
        viewModel.init(isFirstRun = true)
        viewModel.fetch(term = "Q")
        observable.assertCurrentUiState(SearchUiState.Loading)
        repository.assertLoadCalledCount(1)

        viewModel.startUpdates(observer = fragment)
        fragment.assertCurrentUiState(SearchUiState.Loading)

        runAsync.returnResult()
        player.assertUpdateTracksUriList(TRACKS_URI_LIST)
        observable.assertCurrentUiState(SEARCH_UI_STATE_SUCCESS_BASE)
        fragment.assertCurrentUiState(SEARCH_UI_STATE_SUCCESS_BASE)

        observable.assertUiStatesHistory(
            listOf(
                SearchUiState.Initial(),
                SearchUiState.Loading,
                SEARCH_UI_STATE_SUCCESS_BASE
            )
        )
        order.check(
            listOf(
                PLAYER_INIT,
                REPOSITORY_TERM,
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                OBSERVABLE_REGISTER,
                OBSERVABLE_POST,
                RUN_ASYNC_RETURN_RESULT,
                PLAYER_UPDATE,
                OBSERVABLE_POST,
            )
        )
    }

    @Test
    fun noTrackLoadTest() {
        repository.expectTrackList(listOf<TrackModel>())

        viewModel.fetch(term = "QQ")

        observable.assertCurrentUiState(SearchUiState.Loading)
        repository.assertLoadCalledCount(1)

        viewModel.startUpdates(observer = fragment)

        fragment.assertCurrentUiState(SearchUiState.Loading)

        runAsync.returnResult()

        observable.assertCurrentUiState(SearchUiState.NoTracks)
        fragment.assertCurrentUiState(SearchUiState.NoTracks)

        observable.assertUiStatesHistory(
            listOf(
                SearchUiState.Initial(),
                SearchUiState.Loading,
                SearchUiState.NoTracks
            )
        )
        order.check(
            listOf(
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                OBSERVABLE_REGISTER,
                OBSERVABLE_POST,
                RUN_ASYNC_RETURN_RESULT,
                PLAYER_UPDATE,
                OBSERVABLE_POST,
            )
        )
    }

    @Test
    fun recreateActivityTest() {
        initNoCacheTest()

        repository.expectError()

        viewModel.fetch(term = "Q")

        observable.assertCurrentUiState(SearchUiState.Loading)
        repository.assertLoadCalledCount(1)

        runAsync.returnResult()

        observable.assertCurrentUiState(SEARCH_UI_STATE_ERROR_NO_INTERNET)
        fragment.assertCurrentUiState(SEARCH_UI_STATE_ERROR_NO_INTERNET)

        val failLoadOrderList = listOf(
            PLAYER_INIT,
            REPOSITORY_TERM,
            OBSERVABLE_REGISTER,
            OBSERVABLE_POST,
            // viewModel#fetch(term: String)
            OBSERVABLE_POST,
            RUN_ASYNC_HANDLE,
            REPOSITORY_LOAD,
            RUN_ASYNC_RETURN_RESULT,
            PLAYER_UPDATE,
            OBSERVABLE_POST,
        )
        order.check(failLoadOrderList)

        val newInstanceFragment = FakeFragment.Base()

        viewModel.init(isFirstRun = false)

        repository.assertLoadCalledCount(1)

        viewModel.startUpdates(observer = newInstanceFragment)
        newInstanceFragment.assertCurrentUiState(SEARCH_UI_STATE_ERROR_NO_INTERNET)

        val newInstanceFragmentOrderList = listOf(
            OBSERVABLE_REGISTER,
            OBSERVABLE_POST
        )
        order.check(failLoadOrderList + newInstanceFragmentOrderList)

        repository.expectSuccess()
        viewModel.fetch(term = "Q")

        observable.assertCurrentUiState(SearchUiState.Loading)
        repository.assertLoadCalledCount(2)

        newInstanceFragment.assertCurrentUiState(
            SearchUiState.Loading
        )

        runAsync.returnResult()

        player.assertUpdateTracksUriList(TRACKS_URI_LIST)
        observable.assertCurrentUiState(SEARCH_UI_STATE_SUCCESS_BASE)
        newInstanceFragment.assertCurrentUiState(SEARCH_UI_STATE_SUCCESS_BASE)

        observable.assertUiStatesHistory(
            listOf(
                SearchUiState.Initial(),
                SearchUiState.Loading,
                SEARCH_UI_STATE_ERROR_NO_INTERNET,
                SearchUiState.Loading,
                SEARCH_UI_STATE_SUCCESS_BASE
            )
        )
        val loadAfterRecreateOrderList =
            listOf(
                OBSERVABLE_POST,
                RUN_ASYNC_HANDLE,
                REPOSITORY_LOAD,
                RUN_ASYNC_RETURN_RESULT,
                PLAYER_UPDATE,
                OBSERVABLE_POST,
            )
        order.check(failLoadOrderList + newInstanceFragmentOrderList + loadAfterRecreateOrderList)
    }

    @Test
    fun playPauseTest() {
        loadTest()

        viewModel.play(trackId = 1)
        observable.assertCurrentUiState(SEARCH_STATE_PLAY_FIRST)

        viewModel.pause()
        observable.assertCurrentUiState(SEARCH_UI_STATE_SUCCESS_BASE)

        viewModel.play(trackId = 2)
        observable.assertCurrentUiState(SEARCH_STATE_PLAY_SECOND)

        viewModel.pause()
        observable.assertCurrentUiState(SEARCH_UI_STATE_SUCCESS_BASE)
    }

    companion object {
        private val SEARCH_UI_STATE_ERROR_NO_INTERNET =
            SearchUiState.Error(R.string.no_internet_connection)
        private val SEARCH_UI_STATE_SUCCESS_BASE = SearchUiState.Success(
            listOf<SearchItem.Track>(
                SearchItem.TrackUi(
                    trackId = 1L,
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = TrackImageUiState.Base("1"),
                    isPlaying = PlayStopUiState.Stop
                ),
                SearchItem.TrackUi(
                    trackId = 2L,
                    authorName = "Q",
                    trackTitle = "2",
                    coverUrl = TrackImageUiState.Base("2"),
                    isPlaying = PlayStopUiState.Stop
                )
            )
        )
        private val SEARCH_STATE_PLAY_FIRST = SearchUiState.Success(
            listOf<SearchItem.Track>(
                SearchItem.TrackUi(
                    trackId = 1L,
                    authorName = "Q",
                    trackTitle = "1",
                    coverUrl = TrackImageUiState.Base("1", isPlaying = true),
                    isPlaying = PlayStopUiState.Play
                ),
                SearchItem.TrackUi(
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
                ),
                SearchItem.TrackUi(
                    trackId = 2L,
                    authorName = "Q",
                    trackTitle = "2",
                    coverUrl = TrackImageUiState.Base("2", isPlaying = true),
                    isPlaying = PlayStopUiState.Play
                )
            )
        )
        private val TRACKS_URI_LIST = listOf(
            Pair(1L, "1"),
            Pair(2L, "2")
        )
    }
}