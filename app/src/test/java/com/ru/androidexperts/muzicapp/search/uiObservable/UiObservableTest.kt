package com.ru.androidexperts.muzicapp.search.uiObservable

import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.adapter.SearchItem
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist
import com.ru.androidexperts.muzicapp.search.presentation.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUiState
import com.ru.androidexperts.muzicapp.search.uiObservable.fakes.FakeGenericAdapter
import com.ru.androidexperts.muzicapp.search.uiObservable.fakes.FakeObserver
import com.ru.androidexperts.muzicapp.search.uiObservable.fakes.FakeObserver.Companion.UPDATE_UI
import com.ru.androidexperts.muzicapp.search.uiObservable.fakes.FakeUpdateText
import org.junit.Before
import org.junit.Test

class UiObservableTest {

    private lateinit var order: Order
    private lateinit var observer: FakeObserver
    private lateinit var input: FakeUpdateText
    private lateinit var adapter: FakeGenericAdapter
    private val observable = Playlist.Base()

    @Before
    fun setup() {
        order = Order.Base()
        input = FakeUpdateText.Base(order)
        adapter = FakeGenericAdapter.Base(order)
        observer = object : FakeObserver {

            override fun assertUpdateCalled(expectedState: SearchUiState) {
                order.assert(UPDATE_UI, expectedState)
            }

            override fun updateUi(data: SearchUiState) {
                order.add(UPDATE_UI, listOf(data))
                data.show(input = input, adapter = adapter)
            }
        }
    }

    @Test
    fun `initial - without cached data`() {
        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Initial())
        adapter.assertUpdateCalled(emptyList())
        order.assertTraceSize(2)
    }

    @Test
    fun `initial - observer was subscribed after result returned`() {
        observable.updateUi("123")
        observable.updateUi(SearchUiState.Loading)
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Initial("123", SUCCESS_LIST))
        input.assertUpdateCalled("123")
        adapter.assertUpdateCalled(SUCCESS_LIST)
        order.assertTraceSize(3)
    }

    @Test
    fun `with unregister observer`() {
        `initial - observer was subscribed after result returned`()

        observable.update(UiObserver.Empty())

        order.assertTraceSize(3)

        observable.updateUi(SearchUiState.Loading)
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        order.assertTraceSize(3)

        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(5)
    }

    @Test
    fun `initial - observer was subscribed before result returned`() {
        observable.updateUi("123")
        observable.updateUi(SearchUiState.Loading)

        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Initial("123", SearchItem.ProgressUi))
        input.assertUpdateCalled("123")
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(5)
    }

    @Test
    fun `fetch - success result`() {
        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Initial())
        adapter.assertUpdateCalled(emptyList())

        input.update("123")
        observable.updateUi(SearchUiState.Loading)

        input.assertUpdateCalled("123")
        observer.assertUpdateCalled(SearchUiState.Loading)
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(7)

    }

    @Test
    fun `success result and play first and stop`() {
        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Initial())
        adapter.assertUpdateCalled(emptyList())

        input.update("123")
        observable.updateUi(SearchUiState.Loading)

        input.assertUpdateCalled("123")
        observer.assertUpdateCalled(SearchUiState.Loading)
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        observable.play(0)

        observer.assertUpdateCalled(
            SearchUiState.Success(
                listOf(
                    SearchItem.TrackUi(
                        0,
                        TrackImageUiState.Base("1", isPlaying = true),
                        "1",
                        "123",
                        PlayStopUiState.Play
                    ), SearchItem.TrackUi(
                        1, TrackImageUiState.Base("2"), "2", "123", PlayStopUiState.Stop
                    )
                )
            )
        )
        adapter.assertUpdateCalled(
            listOf(
                SearchItem.TrackUi(
                    0,
                    TrackImageUiState.Base("1", isPlaying = true),
                    "1",
                    "123",
                    PlayStopUiState.Play
                ), SearchItem.TrackUi(
                    1, TrackImageUiState.Base("2"), "2", "123", PlayStopUiState.Stop
                )
            )
        )

        observable.stop()
        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(11)
    }

    @Test
    fun `success result and play first and play next and stop`() {

        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Initial())
        adapter.assertUpdateCalled(emptyList())
        order.assertTraceSize(2)

        input.update("123")
        observable.updateUi(SearchUiState.Loading)

        input.assertUpdateCalled("123")
        observer.assertUpdateCalled(SearchUiState.Loading)
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        observable.play(0)

        observer.assertUpdateCalled(
            SearchUiState.Success(
                listOf(
                    SearchItem.TrackUi(
                        0,
                        TrackImageUiState.Base("1", isPlaying = true),
                        "1",
                        "123",
                        PlayStopUiState.Play
                    ), SearchItem.TrackUi(
                        1, TrackImageUiState.Base("2"), "2", "123", PlayStopUiState.Stop
                    )
                )
            )
        )
        adapter.assertUpdateCalled(
            listOf(
                SearchItem.TrackUi(
                    0,
                    TrackImageUiState.Base("1", isPlaying = true),
                    "1",
                    "123",
                    PlayStopUiState.Play
                ), SearchItem.TrackUi(
                    1, TrackImageUiState.Base("2"), "2", "123", PlayStopUiState.Stop
                )
            )
        )

        observable.play(1)

        observer.assertUpdateCalled(
            SearchUiState.Success(
                listOf(
                    SearchItem.TrackUi(
                        0, TrackImageUiState.Base("1"), "1", "123", PlayStopUiState.Stop
                    ), SearchItem.TrackUi(
                        1,
                        TrackImageUiState.Base("2", isPlaying = true),
                        "2",
                        "123",
                        PlayStopUiState.Play
                    )
                )
            )
        )
        adapter.assertUpdateCalled(
            listOf(
                SearchItem.TrackUi(
                    0, TrackImageUiState.Base("1"), "1", "123", PlayStopUiState.Stop
                ), SearchItem.TrackUi(
                    1,
                    TrackImageUiState.Base("2", isPlaying = true),
                    "2",
                    "123",
                    PlayStopUiState.Play
                )
            )
        )

        observable.stop()

        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(13)
    }

    @Test
    fun `error no item message`() {
        observable.updateUi(SearchUiState.NoTracks)
        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.NoTracks)
        adapter.assertUpdateCalled(SearchItem.NoTracksUi)
        order.assertTraceSize(2)
    }

    @Test
    fun `error internet connection message`() {
        observable.updateUi(SearchUiState.Initial("123", SearchItem.ProgressUi))
        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Initial("123", SearchItem.ProgressUi))
        input.assertUpdateCalled("123")
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        observable.updateUi(SearchUiState.Error(errorResId = -777))

        observer.assertUpdateCalled(SearchUiState.Error(errorResId = -777))
        adapter.assertUpdateCalled(SearchItem.ErrorUi(resId = -777))
        order.assertTraceSize(5)
    }

    companion object {
        private val SUCCESS_LIST = listOf(
            SearchItem.TrackUi(0, TrackImageUiState.Base("1"), "1", "123", PlayStopUiState.Stop),
            SearchItem.TrackUi(1, TrackImageUiState.Base("2"), "2", "123", PlayStopUiState.Stop)
        )
    }
}

