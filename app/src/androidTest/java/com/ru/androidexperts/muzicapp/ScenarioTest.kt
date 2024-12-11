package com.ru.androidexperts.muzicapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @After
    fun close() {
        val sharedPreferences = ApplicationProvider
            .getApplicationContext<Context>()
            .getSharedPreferences("test", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    @Test
    fun caseNumber1() {
        val searchPage = SearchPage()

        assertWithRecreate { searchPage.assertInitialState() }

        searchPage.addUserInput(text = "NonExistentArtist")
        assertWithRecreate { searchPage.assertProgressState() }

        searchPage.waitTillError()
        assertWithRecreate { searchPage.assertErrorState() }

        searchPage.clickRetry()
        assertWithRecreate { searchPage.assertProgressState() }

        searchPage.waitTillNoTracksResponse()
        assertWithRecreate { searchPage.assertEmptyState() }

        searchPage.addUserInput(text = "ExistentArtist")
        searchPage.waitTillSuccessResponse()
        assertWithRecreate { searchPage.assertSuccessState() }

        searchPage.clickFirstTrackPlayButton()
        assertWithRecreate { searchPage.assertFirstTrackPlayState() }

        searchPage.waitTillFirstTrackStopped()
        assertWithRecreate { searchPage.assertSecondTrackPlayState() }

        searchPage.waitTillSecondTrackStopped()
        assertWithRecreate { searchPage.assertSuccessState() }
    }

    private fun assertWithRecreate(assertion: () -> Unit) {
        assertion.invoke()
        scenarioRule.scenario.recreate()
        assertion.invoke()
    }
}