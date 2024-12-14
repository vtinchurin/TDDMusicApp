import com.ru.androidexperts.muzicapp.core.Order
import com.ru.androidexperts.muzicapp.R
import com.ru.androidexperts.muzicapp.core.adapter.GenericAdapter
import com.ru.androidexperts.muzicapp.core.adapter.RecyclerItem
import com.ru.androidexperts.muzicapp.core.uiObservable.UiObserver
import com.ru.androidexperts.muzicapp.search.presentation.SearchUiState
import com.ru.androidexperts.muzicapp.search.presentation.adapter.SearchItem
import com.ru.androidexperts.muzicapp.search.presentation.uiObservable.Playlist
import com.ru.androidexperts.muzicapp.search.presentation.view.UpdateText
import com.ru.androidexperts.muzicapp.search.presentation.view.play.PlayStopUiState
import com.ru.androidexperts.muzicapp.search.presentation.view.trackImage.TrackImageUiState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AbstractTest {

    private lateinit var observer: UiObserver<SearchUiState>
    private lateinit var input: FakeUpdateText
    private lateinit var adapter: FakeGenericAdapter
    private val observable = Playlist.Base()
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
    fun `initial - without cached data`() {
        observable.update(observer)
        input.assertText(emptyString())
        adapter.assertRecyclerList(listOf())
        order.check(listOf(UPDATE_RECYCLER, UPDATE_UI))
    }

    @Test
    fun `initial - observer was subscribed after result returned`() {
        observable.updateUi("123")
        observable.updateUi(SearchUiState.Loading)
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))
        observable.update(observer)
        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)
        order.check(listOf(SET_TEXT, UPDATE_RECYCLER, UPDATE_UI))
    }

    @Test
    fun `initial - observer was subscribed before result returned`() {
        observable.updateUi("123")
        observable.updateUi(SearchUiState.Loading)
        observable.update(observer)
        input.assertText("123")
        adapter.assertRecyclerList(listOf(SearchItem.ProgressUi))
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))
        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)
        order.check(
            listOf(
                SET_TEXT, UPDATE_RECYCLER, UPDATE_UI,
                UPDATE_RECYCLER, UPDATE_UI,
            )
        )
    }

    @Test
    fun `fetch - success result`() {
        observable.update(observer)
        input.assertText(emptyString())
        adapter.assertRecyclerList(listOf())
        input.update("123")
        observable.updateUi(SearchUiState.Loading)
        input.assertText("123")
        adapter.assertRecyclerList(listOf(SearchItem.ProgressUi))
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))
        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)
        order.check(
            listOf(
                UPDATE_RECYCLER, UPDATE_UI, SET_TEXT,
                UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI
            )
        )
    }

    @Test
    fun `success result and play first and stop`() {
        observable.updateUi(SearchUiState.Initial())
        observable.update(observer)
        input.assertText(emptyString())
        adapter.assertRecyclerList(listOf())
        input.update("123")
        observable.updateUi(SearchUiState.Loading)
        input.assertText("123")
        adapter.assertRecyclerList(listOf(SearchItem.ProgressUi))
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))
        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)

        observable.play(0)

        input.assertText("123")
        adapter.assertRecyclerList(
            listOf(
                SearchItem.TrackUi(
                    0,
                    TrackImageUiState.Base("1", isPlaying = true),
                    "1",
                    "123",
                    PlayStopUiState.Play
                ),
                SearchItem.TrackUi(1, TrackImageUiState.Base("2"), "2", "123", PlayStopUiState.Stop)
            )
        )

        observable.stop()

        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)

        order.check(
            listOf(
                UPDATE_RECYCLER, UPDATE_UI, SET_TEXT, UPDATE_RECYCLER,
                UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI,
                UPDATE_RECYCLER, UPDATE_UI
            )
        )
    }

    @Test
    fun `success result and play first and play next and stop`() {
        observable.update(observer)
        input.assertText(emptyString())
        adapter.assertRecyclerList(listOf())
        input.update("123")
        observable.updateUi(SearchUiState.Loading)
        input.assertText("123")
        adapter.assertRecyclerList(listOf(SearchItem.ProgressUi))
        observable.updateUi(SearchUiState.Success(SUCCESS_LIST))
        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)

        observable.play(0)

        input.assertText("123")
        adapter.assertRecyclerList(
            listOf(
                SearchItem.TrackUi(
                    0,
                    TrackImageUiState.Base("1", isPlaying = true),
                    "1",
                    "123",
                    PlayStopUiState.Play
                ),
                SearchItem.TrackUi(1, TrackImageUiState.Base("2"), "2", "123", PlayStopUiState.Stop)
            )
        )

        observable.play(1)

        input.assertText("123")
        adapter.assertRecyclerList(
            listOf(
                SearchItem.TrackUi(
                    0,
                    TrackImageUiState.Base("1"),
                    "1",
                    "123",
                    PlayStopUiState.Stop
                ),
                SearchItem.TrackUi(
                    1,
                    TrackImageUiState.Base("2", isPlaying = true),
                    "2",
                    "123",
                    PlayStopUiState.Play
                )
            )
        )

        observable.stop()

        input.assertText("123")
        adapter.assertRecyclerList(SUCCESS_LIST)

        order.check(
            listOf(
                UPDATE_RECYCLER, UPDATE_UI, SET_TEXT, UPDATE_RECYCLER,
                UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI,
                UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI
            )
        )
    }


    @Test
    fun `error no item message`() {
        observable.updateUi(SearchUiState.NoTracks)
        observable.update(observer)
        input.assertText(emptyString())
        adapter.assertRecyclerList(ERROR_NO_ITEM)
        order.check(listOf(UPDATE_RECYCLER, UPDATE_UI))
    }

    @Test
    fun `error internet connection message`() {
        observable.updateUi(SearchUiState.Initial("123"))
        observable.update(observer)
        observable.updateUi(SearchUiState.Error(R.string.no_internet_connection))
        input.assertText("123")
        adapter.assertRecyclerList(ERROR_MESSAGE)
        order.check(listOf(SET_TEXT, UPDATE_RECYCLER, UPDATE_UI, UPDATE_RECYCLER, UPDATE_UI))
    }

    companion object {
        private val SUCCESS_LIST = listOf(
            SearchItem.TrackUi(0, TrackImageUiState.Base("1"), "1", "123", PlayStopUiState.Stop),
            SearchItem.TrackUi(1, TrackImageUiState.Base("2"), "2", "123", PlayStopUiState.Stop)
        )
        private val ERROR_NO_ITEM = listOf(SearchItem.NoTracksUi)
        private val ERROR_MESSAGE = listOf(SearchItem.ErrorUi(R.string.no_internet_connection))
        private fun emptyString() = ""
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

        override fun update(textResId: Int) = Unit

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

private const val SET_TEXT = "UpdateText#update"
private const val UPDATE_RECYCLER = "GenericRecycler#update"
private const val UPDATE_UI = "Observer#updateUi"