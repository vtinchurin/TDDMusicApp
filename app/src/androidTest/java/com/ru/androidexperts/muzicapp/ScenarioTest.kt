package com.ru.androidexperts.muzicapp

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun close() {
        val sharedPreferences = ApplicationProvider
            .getApplicationContext<Context>()
            .getSharedPreferences("test", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
    /**
     * MG-01
     */
    @Test
    fun caseNumber1() {
        val appPage: AppPage = AppPage()

        appPage.assertInitialState()

        appPage.addUserInput(text = "NonExistentArtist")
        assertWithRecreate { appPage.assertProgressState() }

        appPage.waitTillError()

        assertWithRecreate { appPage.assertErrorState() }

        appPage.clickRetry()
        assertWithRecreate { appPage.assertProgressState() }

        appPage.waitTillNoTracksResponse()

        assertWithRecreate { appPage.assertEmptyState() }

        appPage.addUserInput(text = "ExistentArtist")
        assertWithRecreate { appPage.assertSuccessState() }

        appPage.clickFirstTrackPlayButton()
        assertWithRecreate { appPage.assertFirstTrackPlayState() }

        appPage.waitTillFirstTrackStopped()

        assertWithRecreate { appPage.assertSecondTrackPlayState() }

        appPage.waitTillSecondTrackStopped()

        assertWithRecreate { appPage.assertSuccessState() }
    }

    private fun assertWithRecreate(assertion: () -> Unit) {
        assertion.invoke()
        scenarioRule.scenario.recreate()
        assertion.invoke()
    }
}