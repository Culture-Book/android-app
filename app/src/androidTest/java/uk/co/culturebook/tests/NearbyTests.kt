package uk.co.culturebook.tests

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import junit.framework.TestCase.fail
import org.junit.Test
import uk.co.culturebook.base.BaseTest
import uk.co.culturebook.base.MockResponses
import uk.co.culturebook.base.mock
import uk.co.culturebook.base.returns
import uk.co.culturebook.base.withRole
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.explore.ExploreRoute
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R

class NearbyTests : BaseTest() {

    @Test
    fun testNearby_getElements() {
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElements returns MockResponses.Api.successfulElementsResponse,
            ApiInterface::getUser returns MockResponses.Api.successfulUserResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        launchTest {
            ExploreRoute(navigate = {
                when {
                    it.contains(Route.Details.route) -> assert(true)
                    else -> fail("Unexpected route: $it")
                }
            })
        }

        with(composeTestRule) {
            onNodeWithText(MockResponses.Api.successfulElementsResponse.data.first().name)
                .assertIsDisplayed()
                .performClick()
        }
    }

    @Test
    fun testNearby_goToAddNew() {
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElements returns MockResponses.Api.successfulElementsResponse,
            ApiInterface::getUser returns MockResponses.Api.successfulUserResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        launchTest {
            ExploreRoute(navigate = {
                when {
                    it.contains(Route.Details.route) -> assert(true)
                    it.contains(Route.AddNew.Location.route) -> assert(true)
                    else -> fail("Unexpected route: $it")
                }
            })
        }

        with(composeTestRule) {
            onNodeWithContentDescription("Add new element")
                .assertIsDisplayed()
                .performClick()
        }
    }

    @Test
    fun testNearby_showFilters() {
        lateinit var filterTitle: String
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElements returns MockResponses.Api.successfulElementsResponse,
            ApiInterface::getUser returns MockResponses.Api.successfulUserResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        launchTest {
            filterTitle = stringResource(R.string.type_filter)
            ExploreRoute(navigate = {
                when {
                    it.contains(Route.Details.route) -> assert(true)
                    it.contains(Route.AddNew.Location.route) -> assert(true)
                    else -> fail("Unexpected route: $it")
                }
            })
        }

        with(composeTestRule) {
            onNodeWithContentDescription("filters")
                .assertIsDisplayed()
                .performClick()

            onAllNodesWithText(filterTitle)
                .onFirst()
                .assertIsDisplayed()
        }
    }

    @Test
    fun testNearby_getContributions() {
        lateinit var contributionText: String
        lateinit var doneText: String

        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElements returns MockResponses.Api.successfulElementsResponse,
            ApiInterface::getContributions returns MockResponses.Api.successfulContributionsResponse,
            ApiInterface::getUser returns MockResponses.Api.successfulUserResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
            ApiInterface::getContributionsMedia returns MockResponses.Api.successfulMediaResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        launchTest {
            contributionText = stringResource(R.string.contributions)
            doneText = stringResource(R.string.done)

            ExploreRoute(navigate = {
                when {
                    it.contains(Route.Details.route) -> assert(true)
                    it.contains(Route.AddNew.Location.route) -> assert(true)
                    else -> fail("Unexpected route: $it")
                }
            })
        }

        with(composeTestRule) {
            onNodeWithContentDescription("filters")
                .assertIsDisplayed()
                .performClick()

            onAllNodesWithText(contributionText)
                .onFirst()
                .assertIsDisplayed()
                .performClick()

            onAllNodes(withRole(Role.Button) and hasText(doneText))
                .onFirst()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(MockResponses.Api.successfulContributionsResponse.data.first().name)
                .assertIsDisplayed()
                .performClick()
        }
    }

    @Test
    fun testNearby_getCultures() {
        lateinit var cultureText: String
        lateinit var doneText: String

        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElements returns MockResponses.Api.successfulElementsResponse,
            ApiInterface::getCultures returns MockResponses.Api.successfulCultures,
            ApiInterface::getUser returns MockResponses.Api.successfulUserResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        launchTest {
            cultureText = stringResource(R.string.cultures)
            doneText = stringResource(R.string.done)

            ExploreRoute(navigate = {
                when {
                    it.contains(Route.Details.route) -> assert(true)
                    it.contains(Route.AddNew.Location.route) -> assert(true)
                    else -> fail("Unexpected route: $it")
                }
            })
        }

        with(composeTestRule) {
            onNodeWithContentDescription("filters")
                .assertIsDisplayed()
                .performClick()

            onAllNodesWithText(cultureText)
                .onFirst()
                .assertIsDisplayed()
                .performClick()

            onAllNodes(withRole(Role.Button) and hasText(doneText))
                .onFirst()
                .assertIsDisplayed()
                .performClick()

            onNodeWithText(MockResponses.Api.successfulCultures.data.first().name)
                .assertIsDisplayed()
                .performClick()
        }
    }

}