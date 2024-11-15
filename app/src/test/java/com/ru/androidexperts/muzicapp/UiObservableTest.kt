package com.ru.androidexperts.muzicapp

import com.ru.androidexperts.muzicapp.view.UpdateText
import com.ru.androidexperts.muzicapp.view.play.PlayStopUiState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ObservableTest {

    private lateinit var observer: UiObserver<SearchUiState>
    private lateinit var input: FakeUpdateText
    private lateinit var adapter: FakeGenericAdapter
    private val observable = UiObservable.Playlist.Base()
    private lateinit var order: Order

    @Before
    fun setup() {
        order = Order()
        input = FakeUpdateText.Base(order)
        adapter = FakeGenericAdapter.Base(order)
        observer = UiObserver {
            it.show(input, adapter)
            order.add(UPDATE_UI)
        }
    }

    @Test
    fun `initial without cached data`() {
        observable.update(observer)
        input.assertText("")
        adapter.assertRecyclerList(listOf())
        order.check(listOf(UPDATE_UI))
    }

    @Test
    fun `initial with cached word`() {
        observable.updateUi(SearchUiState.Initial("123"))
        observable.update(observer)
        input.assertText("123")
        adapter.assertRecyclerList(listOf())
        order.check(listOf(SET_TEXT, UPDATE_UI))
    }

    @Test
    fun `loading after initial`() {
        observable.updateUi(SearchUiState.Initial("123"))
        observable.update(observer)
        input.assertText("123")
        adapter.assertRecyclerList(listOf())
        observable.updateUi(SearchUiState.Loading)
        input.assertText("123")
        adapter.assertRecyclerList(listOf(RecyclerItem.ProgressUi))
        order.check(
            listOf(
                SET_TEXT, UPDATE_UI,
                UPDATE_RECYCLER, UPDATE_UI,
            )
        )
    }

    @Test
    fun `success result`() {
        observable.updateUi(SearchUiState.Initial(""))
        observable.update(observer)
        input.assertText("")
        adapter.assertRecyclerList(listOf())
        input.update("123")
        observable.updateUi(SearchUiState.Loading)
        input.assertText("123")
        adapter.assertRecyclerList(listOf(RecyclerItem.ProgressUi))
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))
        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)
        order.check(
            listOf(
                UPDATE_UI, SET_TEXT,
                UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI
            )
        )
    }

    @Test
    fun `success result and play first and stop`() {
        observable.updateUi(SearchUiState.Initial(""))
        observable.update(observer)
        input.assertText("")
        adapter.assertRecyclerList(listOf())
        input.update("123")
        observable.updateUi(SearchUiState.Loading)
        input.assertText("123")
        adapter.assertRecyclerList(listOf(RecyclerItem.ProgressUi))
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))
        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)

        observable.play(0)

        input.assertText("123")
        adapter.assertRecyclerList(
            listOf(
                RecyclerItem.TrackUi(0, "1", "1", "123", PlayStopUiState.Play),
                RecyclerItem.TrackUi(1, "2", "2", "123", PlayStopUiState.Stop)
            )
        )

        observable.stop()

        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)

        order.check(
            listOf(
                UPDATE_UI, SET_TEXT, UPDATE_RECYCLER,
                UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI,
                UPDATE_RECYCLER, UPDATE_UI
            )
        )
    }

    @Test
    fun `success result and play first and play next and stop`() {
        observable.updateUi(SearchUiState.Initial(""))
        observable.update(observer)
        input.assertText("")
        adapter.assertRecyclerList(listOf())
        input.update("123")
        observable.updateUi(SearchUiState.Loading)
        input.assertText("123")
        adapter.assertRecyclerList(listOf(RecyclerItem.ProgressUi))
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))
        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)

        observable.play(0)

        input.assertText("123")
        adapter.assertRecyclerList(
            listOf(
                RecyclerItem.TrackUi(0, "1", "1", "123", PlayStopUiState.Play),
                RecyclerItem.TrackUi(1, "2", "2", "123", PlayStopUiState.Stop)
            )
        )

        observable.play(1)

        input.assertText("123")
        adapter.assertRecyclerList(
            listOf(
                RecyclerItem.TrackUi(0, "1", "1", "123", PlayStopUiState.Stop),
                RecyclerItem.TrackUi(1, "2", "2", "123", PlayStopUiState.Play)
            )
        )

        observable.stop()

        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)

        order.check(
            listOf(
                UPDATE_UI, SET_TEXT, UPDATE_RECYCLER,
                UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI,
                UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI
            )
        )
    }


    @Test
    fun `error no item message`() {
        observable.updateUi(SearchUiState.NoTracks)
        observable.update(observer)
        input.assertText("")
        adapter.assertRecyclerList(ERROR_NO_ITEM)
        order.check(listOf(UPDATE_RECYCLER, UPDATE_UI))
    }

    @Test
    fun `error internet connection message`() {
        observable.updateUi(SearchUiState.Initial("123"))
        observable.update(observer)
        observable.updateUi(SearchUiState.Error("No Internet"))
        input.assertText("123")
        adapter.assertRecyclerList(ERROR_MESSAGE)
        order.check(listOf(SET_TEXT, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI))
    }

    companion object {
        private val SUCCESS_LIST = listOf(
            RecyclerItem.TrackUi(0, "1", "1", "123", PlayStopUiState.Stop),
            RecyclerItem.TrackUi(1, "2", "2", "123", PlayStopUiState.Stop)
        )
        private val ERROR_NO_ITEM = listOf(RecyclerItem.NoTracksUi)
        private val ERROR_MESSAGE = listOf(RecyclerItem.ErrorUi("No Internet"))
    }

}

interface FakeUpdateText : UpdateText {

    fun assertText(expectedText: String)

    class Base(private val order: Order) : FakeUpdateText {

        private var text = ""

        override fun assertText(expectedText: String) {
            assertEquals(expectedText, text)
        }

        override fun update(newText: String) {
            text = newText
            order.add(SET_TEXT)
        }

    }
}

interface FakeGenericAdapter : GenericAdapter {

    fun assertRecyclerList(expectedList: List<RecyclerItem>)

    class Base(private val order: Order) : FakeGenericAdapter {

        private var data = mutableListOf<RecyclerItem>()

        override fun assertRecyclerList(expectedList: List<RecyclerItem>) {
            assertEquals(expectedList, data)
        }

        override fun update(newList: List<RecyclerItem>) {
            data.clear()
            data.addAll(newList)
            order.add(UPDATE_RECYCLER)
        }
    }
}

class Order {

    private val commands = mutableListOf<String>()

    fun add(command: String) {
        commands.add(command)
    }

    fun check(commands: List<String>) {
        assertEquals(this.commands, commands)
    }

}

private const val SET_TEXT = "UpdateText#update"
private const val UPDATE_RECYCLER = "GenericRecycler#update"
private const val UPDATE_UI = "Observer#updateUi"