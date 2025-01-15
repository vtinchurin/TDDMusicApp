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
        /* Action */
        observable.update(observer)

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Initial())
        adapter.assertUpdateCalled(emptyList())
        order.assertTraceSize(2)
    }

    @Test
    fun `initial - observer was subscribed after result returned`() {
        observable.updateUi("123")
        observable.updateUi(SearchUiState.Loading)
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        /* Action */
        observable.update(observer)

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Initial("123", SUCCESS_LIST))
        input.assertUpdateCalled("123")
        adapter.assertUpdateCalled(SUCCESS_LIST)
        order.assertTraceSize(3)
    }

    @Test
    fun `with unregister observer`() {
        `initial - observer was subscribed after result returned`()

        /* Action */
        observable.update(UiObserver.Empty())

        /* Assertion */
        order.assertTraceSize(3)

        /* Action */
        observable.updateUi(SearchUiState.Loading)
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        /* Assertion */
        order.assertTraceSize(3)

        /* Action */
        observable.update(observer)

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(5)
    }

    @Test
    fun `initial - observer was subscribed before result returned`() {
        observable.updateUi("123")
        observable.updateUi(SearchUiState.Loading)

        /* Action */
        observable.update(observer)

        observer.assertUpdateCalled(SearchUiState.Initial("123", SearchItem.ProgressUi))
        input.assertUpdateCalled("123")
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        /* Action */
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(5)
    }

    @Test
    fun `fetch - success result`() {
        /* Action */
        observable.update(observer)

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Initial())
        adapter.assertUpdateCalled(emptyList())

        /* Action */
        input.update("123")
        observable.updateUi(SearchUiState.Loading)

        /* Assertion */
        input.assertUpdateCalled("123")
        observer.assertUpdateCalled(SearchUiState.Loading)
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        /* Action */
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(7)

    }

    @Test
    fun `success result and play first and stop`() {
        /* Action */
        observable.update(observer)

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Initial())
        adapter.assertUpdateCalled(emptyList())

        /* Action */
        input.update("123")
        observable.updateUi(SearchUiState.Loading)

        /* Assertion */
        input.assertUpdateCalled("123")
        observer.assertUpdateCalled(SearchUiState.Loading)
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        /* Action */
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        /* Action */
        observable.play(0)

        /* Assertion */
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

        /* Action */
        observable.stop()

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(11)
    }

    @Test
    fun `success result and play first and play next and stop`() {
        /* Action */
        observable.update(observer)

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Initial())
        adapter.assertUpdateCalled(emptyList())
        order.assertTraceSize(2)

        /* Action */
        input.update("123")
        observable.updateUi(SearchUiState.Loading)

        /* Assertion */
        input.assertUpdateCalled("123")
        observer.assertUpdateCalled(SearchUiState.Loading)
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        /* Action */
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        /* Action */
        observable.play(0)

        /* Assertion */
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

        /* Action */
        observable.play(1)

        /* Assertion */
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

        /* Action */
        observable.stop()

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Success(SUCCESS_LIST))
        adapter.assertUpdateCalled(SUCCESS_LIST)

        order.assertTraceSize(13)
    }

    @Test
    fun `error no item message`() {
        /* Action */
        observable.updateUi(SearchUiState.NoTracks)
        observable.update(observer)

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.NoTracks)
        adapter.assertUpdateCalled(SearchItem.NoTracksUi)
        order.assertTraceSize(2)
    }

    @Test
    fun `error internet connection message`() {
        /* Action */
        observable.updateUi(SearchUiState.Initial("123", SearchItem.ProgressUi))
        observable.update(observer)

        /* Assertion */
        observer.assertUpdateCalled(SearchUiState.Initial("123", SearchItem.ProgressUi))
        input.assertUpdateCalled("123")
        adapter.assertUpdateCalled(SearchItem.ProgressUi)

        /* Action */
        observable.updateUi(SearchUiState.Error(errorResId = -777))

        /* Assertion */
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

