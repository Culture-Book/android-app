package uk.co.culturebook.tests

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Test
import uk.co.culturebook.add_new.data.AddNewState
import uk.co.culturebook.add_new.data.TypeData
import uk.co.culturebook.add_new.info.composables.AddInfoRoute
import uk.co.culturebook.add_new.location.composables.LocationRoute
import uk.co.culturebook.add_new.submit.SubmitRoute
import uk.co.culturebook.add_new.title_type.composables.TitleAndTypeRoute
import uk.co.culturebook.base.BaseTest
import uk.co.culturebook.base.MockResponses
import uk.co.culturebook.base.mock
import uk.co.culturebook.base.returns
import uk.co.culturebook.base.withRole
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.models.cultural.ElementType
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R

class AddNewTests : BaseTest() {
    @Test
    fun testAddNew_location() {
        val authInterface = mock<AuthInterface>()
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getNearbyCultures returns MockResponses.Api.successfulCulturesResponse,
        )
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var selectLocation: String

        launchTest {
            LocationRoute(
                navigateBack = {},
                onDone = { c, _ ->
                    assertEquals(
                        MockResponses.Api.successfulCulturesResponse.data.cultures.first(), c
                    )
                },
            )

            selectLocation = stringResource(id = R.string.select_location)
        }

        with(composeTestRule) {
            onNodeWithText(selectLocation)
                .performClick()
            onNodeWithText(MockResponses.Api.successfulCulturesResponse.data.cultures.first().name)
                .performClick()
        }
    }

    @Test
    fun testAddNew_TitleAndType() {
        val authInterface = mock<AuthInterface>()
        val apiInterface = mock<ApiInterface>(
            ApiInterface::getDuplicateElement returns MockResponses.Api.noDuplicateElementsResponse,
        )
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var food: String
        lateinit var title: String
        lateinit var next: String

        launchTest {
            TitleAndTypeRoute(
                navigateBack = { },
                typeData = TypeData(),
                onElementAndTitleSelected = {
                    assertEquals(ElementType.Food, it.type)
                    assertEquals("title", it.name)
                }
            )

            food = stringResource(id = R.string.food)
            title = stringResource(id = R.string.name)
            next = stringResource(id = R.string.next)
        }

        with(composeTestRule) {
            onNodeWithText(food)
                .performClick()
            onNodeWithText(title)
                .performTextInput("title")
            onNodeWithText(next)
                .performClick()
        }
    }

    @Test
    fun testAddNew_AddInfo() {
        val authInterface = mock<AuthInterface>()
        val apiInterface = mock<ApiInterface>()
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var next: String

        launchTest {
            AddInfoRoute(
                navigateBack = { },
                navigate = {
                    if (it == Route.AddNew.Review.route) assert(true)
                    else assert(false)
                },
                addNewState = AddNewState(),
            )

            next = stringResource(id = R.string.next)
        }

        with(composeTestRule) {
            onNodeWithTag("LargeDynamicRoundedTextField")
                .performTextInput("information")
            onNodeWithText(next)
                .performClick()
        }
    }

    @Test
    fun testAddNew_Submit() {
        val authInterface = mock<AuthInterface>()
        val apiInterface = mock<ApiInterface>(
            ApiInterface::postElement returns MockResponses.Api.successfulElementResponse,
        )
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var submit: String
        val addNewState = AddNewState().apply {
            name = "title"
            type = ElementType.Food
            information = "information"
            culture = MockResponses.Api.successfulCulturesResponse.data.cultures.first()
            location = MockResponses.Api.successfulCulturesResponse.data.cultures.first().location
        }
        launchTest {
            SubmitRoute(
                navigateBack = { },
                onFinished = {
                    assert(true)
                },
                addNewState = addNewState,
            )

            submit = stringResource(id = R.string.submit_element)
        }

        with(composeTestRule) {
            onAllNodes(hasText(submit) and withRole(Role.Button))
                .onFirst()
                .performClick()
        }
    }
}