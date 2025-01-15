package com.ru.androidexperts.muzicapp.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import com.ru.androidexperts.muzicapp.core.assertions.Enabled
import com.ru.androidexperts.muzicapp.core.assertions.Existence
import com.ru.androidexperts.muzicapp.core.assertions.Visibility
import com.ru.androidexperts.muzicapp.core.matchers.RecyclerViewMatcher
import com.ru.androidexperts.muzicapp.core.matchers.waitTillDisplayed
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matcher

/**
 *[Ui] is a parent interface, which contains methods for checking views
 */

interface Ui : Visibility, Existence, Enabled {

    fun check(viewAssert: ViewAssertion)

    /**
     * [Single] is a parent interface, for any View in Hierarchy
     */

    interface Single : Ui {

        fun baseMatchers(): List<Matcher<View>>
        fun perform(action: ViewAction): ViewInteraction
    }

    /**
     * [Group] is a parent interface, for any ViewGroup in Hierarchy
     */

    interface Group : Single {

        fun parentMatchers(): List<Matcher<View>>
        fun descendantMatchers(): List<Matcher<View>>
    }

    /**
     * [Ui.Item] is a Abstract implementation for any final View
     */

    abstract class Item(
        protected val id: Int,
        protected val classType: Class<out View>,
        protected val matchers: List<Matcher<View>> = emptyList(),
    ) : Single {

        protected open val interaction: ViewInteraction
            get() = onView(allOf(matchers + baseMatchers()))

        override fun baseMatchers(): List<Matcher<View>> = listOf(
            withId(id),
            isAssignableFrom(classType)
        )

        override fun perform(action: ViewAction): ViewInteraction =
            interaction.perform(action)

        override fun waitTillDisplayed(timeout: Long) {
            perform(waitTillDisplayed(id, timeout))
        }

        override fun check(viewAssert: ViewAssertion) {
            interaction.check(viewAssert)
        }

        override fun assertEnabled() {
            check(matches(isEnabled()))
        }

        override fun assertNotEnabled() {
            check(matches(isNotEnabled()))
        }

        override fun assertVisible() {
            check(matches(isDisplayed()))
        }

        override fun assertNotVisible() {
            check(matches(not(isDisplayed())))
        }

        override fun assertDoesNotExist() {
            check(doesNotExist())
        }
    }

    /**
     * [Ui.Container] is a Abstract implementation for any ViewGroup
     */

    abstract class Container(
        id: Int,
        classType: Class<out View>,
        matchers: List<Matcher<View>> = emptyList(),
    ) : Item(
        id = id,
        classType = classType,
        matchers = matchers
    ), Group {

        override fun parentMatchers(): List<Matcher<View>> = listOf(
            withParent(withId(id)),
            withParent(isAssignableFrom(classType))
        )

        override fun descendantMatchers(): List<Matcher<View>> = matchers.map {
            isDescendantOfA(it)
        }

        override fun perform(action: ViewAction): ViewInteraction =
            onView(isRoot()).perform(action)
    }

    /**
     * [Ui.Recycler] is a parent interface, for RecyclerView and Views inside
     */

    interface Recycler : Group {

        fun hasItemCount(itemCount: Int)
        fun hasNoItems()

        /**
            [Recycler.Item] is a Abstract implementation for any View
            in ViewGroup inside RecyclerView
         */

        abstract class Item(
            id: Int,
            classType: Class<out View>,
            private val recyclerViewMatcher: RecyclerViewMatcher,
            private val position: Int,
            matchers: List<Matcher<View>> = emptyList(),
        ) : Ui.Item(
            id = id,
            classType = classType,
            matchers = matchers
        ), Single {

            override val interaction: ViewInteraction
                get() = onView(
                    allOf(
                        matchers +
                                baseMatchers() +
                                isDescendantOfA(recyclerViewMatcher.atPosition(position))
                    )
                )
        }

        /**
            [Recycler.Container] is a Abstract implementation for any ViewGroup inside RecyclerView
         */

        abstract class Container(
            id: Int,
            classType: Class<out View>,
            private val position: Int,
            private val recyclerViewMatcher: RecyclerViewMatcher,
            matchers: List<Matcher<View>> = emptyList(),
        ) : Ui.Container(
            id = id,
            classType = classType,
            matchers = matchers
        ) {

            override val interaction: ViewInteraction
                get() = onView(
                    allOf(
                        matchers +
                                baseMatchers() +
                                recyclerViewMatcher.atPosition(position)
                    )
                )
        }

        /**
            [Recycler.Abstract] is a Abstract implementation for RecyclerView
         */

        abstract class Abstract(
            id: Int,
            matchers: List<Matcher<View>>,
        ) : Ui.Container(
            id = id,
            classType = RecyclerView::class.java,
            matchers = matchers
        ), Recycler {

            protected val recyclerViewMatcher: RecyclerViewMatcher
                get() = RecyclerViewMatcher.Base(id)

            override fun hasItemCount(itemCount: Int) {
                recyclerViewMatcher.hasItemCount(itemCount)
            }

            override fun hasNoItems() {
                recyclerViewMatcher.hasItemCount(0)
            }
        }
    }
}