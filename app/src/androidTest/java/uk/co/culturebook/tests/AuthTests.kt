package uk.co.culturebook.tests

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavController
import org.junit.Assert.assertEquals
import org.junit.Test
import uk.co.culturebook.auth.composables.LoginRoute
import uk.co.culturebook.base.BaseTest
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R

class AuthTests : BaseTest() {

    @Test
    fun testLogin() {
        lateinit var navController: NavController
        lateinit var emailString: String
        lateinit var passwordString: String
        lateinit var loginString: String

        launchTest { testNavController ->
            LoginRoute(navController = testNavController)

            navController = testNavController
            emailString = stringResource(R.string.email)
            passwordString = stringResource(R.string.password)
            loginString = stringResource(R.string.login)
        }

        with(composeTestRule) {
            onNodeWithText(emailString)
                .performTextInput("email@email.com")
            onNodeWithText(passwordString)
                .performTextInput("password")
            onNodeWithText(loginString)
                .performClick()

            runOnIdle {
                assertEquals(navController.currentDestination?.route, Route.Home.route)
            }
        }

    }
}