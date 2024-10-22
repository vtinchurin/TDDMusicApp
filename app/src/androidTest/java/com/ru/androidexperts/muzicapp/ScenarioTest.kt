package com.ru.androidexperts.muzicapp

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class ScenarioTest {

    @get:Rule
    val scenarioRule = ActivityScenarioRule(MainActivity::class.java)

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

        appPage.waitTillSuccessResponse()

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