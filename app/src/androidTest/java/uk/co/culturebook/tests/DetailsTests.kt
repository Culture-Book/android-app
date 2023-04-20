package uk.co.culturebook.tests

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.text.EmojiCompat
import junit.framework.TestCase.fail
import org.junit.Test
import uk.co.culturebook.base.BaseTest
import uk.co.culturebook.base.MockResponses
import uk.co.culturebook.base.mock
import uk.co.culturebook.base.returns
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.details.DetailsScreenRoute
import uk.co.culturebook.nav.Route

class DetailsTests : BaseTest() {
    @Test
    fun testDetails_element() {
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElement returns MockResponses.Api.successfulElementResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
            ApiInterface::getElementComments returns MockResponses.Api.successfulCommentsResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        launchTest {
            DetailsScreenRoute(
                navigateBack = {},
                navigate = {
                    when {
                        it.contains(Route.Details.route) -> assert(true)
                        else -> fail("Unexpected route: $it")
                    }
                },
                uuid = MockResponses.Api.successfulElementResponse.data.id,
                isContribution = false
            )
        }

        with(composeTestRule) {
            onNodeWithText(MockResponses.Api.successfulElementResponse.data.name)
                .assertIsDisplayed()
        }
    }

    @Test
    fun testDetails_contribution() {
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getContribution returns MockResponses.Api.successfulContributionResponse,
            ApiInterface::getContributionsMedia returns MockResponses.Api.successfulMediaResponse,
            ApiInterface::getContributionComments returns MockResponses.Api.successfulCommentsResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var contributions: String

        launchTest {
            DetailsScreenRoute(
                navigateBack = {},
                navigate = {
                    when {
                        it.contains(Route.Details.route) -> assert(true)
                        else -> fail("Unexpected route: $it")
                    }
                },
                uuid = MockResponses.Api.successfulContributionResponse.data.id,
                isContribution = true
            )
            contributions = stringResource(id = uk.co.culturebook.ui.R.string.contributions)
        }

        with(composeTestRule) {
            onNodeWithText(MockResponses.Api.successfulContributionResponse.data.name)
                .assertIsDisplayed()
            onNodeWithText(contributions)
                .assertDoesNotExist()
        }
    }

    @Test
    fun testDetails_testComments() {
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElement returns MockResponses.Api.successfulElementResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
            ApiInterface::getElementComments returns MockResponses.Api.successfulCommentsResponse,
            ApiInterface::addElementComment returns MockResponses.Api.successfulCommentResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        launchTest {
            DetailsScreenRoute(
                navigateBack = {},
                navigate = {
                    when {
                        it.contains(Route.Details.route) -> assert(true)
                        else -> fail("Unexpected route: $it")
                    }
                },
                uuid = MockResponses.Api.successfulElementResponse.data.id,
                isContribution = false
            )
        }

        with(composeTestRule) {
            onNodeWithContentDescription("comments")
                .performClick()
            onAllNodesWithText(MockResponses.Api.successfulCommentsResponse.data.first().comment)
                .onFirst()
                .assertIsDisplayed()
        }
    }

    @Test
    fun testDetails_testAddComments() {
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElement returns MockResponses.Api.successfulElementResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
            ApiInterface::getElementComments returns MockResponses.Api.successfulCommentsResponse,
            ApiInterface::addElementComment returns MockResponses.Api.successfulCommentResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var addComment: String

        launchTest {
            DetailsScreenRoute(
                navigateBack = {},
                navigate = {
                    when {
                        it.contains(Route.Details.route) -> assert(true)
                        else -> fail("Unexpected route: $it")
                    }
                },
                uuid = MockResponses.Api.successfulElementResponse.data.id,
                isContribution = false
            )
            addComment = stringResource(id = uk.co.culturebook.ui.R.string.add_comment)
        }

        with(composeTestRule) {
            onNodeWithContentDescription("comments")
                .performClick()
            onAllNodesWithText(MockResponses.Api.successfulCommentsResponse.data.first().comment)
                .onFirst()
                .assertIsDisplayed()
            onAllNodesWithText(addComment)
                .onLast()
                .performTextInput("Test comment")
            onAllNodesWithContentDescription("send comment")
                .onFirst()
                .performClick()
        }
    }

    @Test
    fun testDetails_testAddReaction() {
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getElement returns MockResponses.Api.successfulElementResponse,
            ApiInterface::getElementsMedia returns MockResponses.Api.successfulMediaResponse,
            ApiInterface::getElementComments returns MockResponses.Api.successfulCommentsResponse,
            ApiInterface::toggleElementReaction returns MockResponses.Api.successfulReactionResponse,
        )
        val authInterface = mock<AuthInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        launchTest {
            DetailsScreenRoute(
                navigateBack = {},
                navigate = {
                    when {
                        it.contains(Route.Details.route) -> assert(true)
                        else -> fail("Unexpected route: $it")
                    }
                },
                uuid = MockResponses.Api.successfulElementResponse.data.id,
                isContribution = false
            )
        }

        testContext?.let { EmojiCompat.init(BundledEmojiCompatConfig(it)) }

        with(composeTestRule) {
            onNodeWithContentDescription("reactions")
                .performClick()
        }
    }
}