package uk.co.culturebook.tests

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Test
import uk.co.culturebook.auth.composables.LoginRoute
import uk.co.culturebook.auth.composables.RegistrationRoute
import uk.co.culturebook.base.BaseTest
import uk.co.culturebook.base.MockResponses
import uk.co.culturebook.base.mock
import uk.co.culturebook.base.returns
import uk.co.culturebook.base.withRole
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.nav.Route
import uk.co.culturebook.ui.R

class AuthTests : BaseTest() {

    @Test
    fun testSuccessfulLogin() {
        val apiInterface = mock<ApiInterface>()
        val authInterface = mock<AuthInterface>(
            AuthInterface::login returns MockResponses.Auth.successfulLoginResponse,
            AuthInterface::getPublicOauthKey returns MockResponses.Auth.successfulPublicKeyResponse
        )
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var emailString: String
        lateinit var passwordString: String
        lateinit var loginString: String

        launchTest {
            LoginRoute(
                navigate = {
                    when (it) {
                        Route.Registration.route -> assertEquals(Route.Registration.route, it)
                        Route.Forgot.route -> assertEquals(Route.Forgot.route, it)
                        else -> fail("Unexpected route: $it")
                    }
                },
                navigateTop = {
                    assertEquals(Route.Home.route, it)
                })

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
        }
    }

    @Test
    fun testFailedLogin() {
        val apiInterface = mock<ApiInterface>()
        val authInterface = mock<AuthInterface>(
            AuthInterface::login returns MockResponses.Auth.failedLoginResponse,
            AuthInterface::getPublicOauthKey returns MockResponses.Auth.successfulPublicKeyResponse
        )
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var emailString: String
        lateinit var passwordString: String
        lateinit var loginString: String
        lateinit var errorString: String

        launchTest {
            LoginRoute(
                navigate = {
                    when (it) {
                        Route.Registration.route -> assertEquals(Route.Registration.route, it)
                        Route.Forgot.route -> assertEquals(Route.Forgot.route, it)
                        else -> fail("Unexpected route: $it")
                    }
                },
                navigateTop = { fail("Failed login attempts shouldn't navigate anywhere") })

            emailString = stringResource(R.string.email)
            passwordString = stringResource(R.string.password)
            loginString = stringResource(R.string.login)
            errorString = stringResource(R.string.generic_sorry)
        }

        with(composeTestRule) {
            onNodeWithText(emailString)
                .performTextInput("email@email.com")
            onNodeWithText(passwordString)
                .performTextInput("password")
            onNodeWithText(loginString)
                .performClick()

            onNodeWithText(errorString)
                .assertIsDisplayed()
        }
    }

    @Test
    fun testRegistration() {
        val apiInterface = mock<ApiInterface>()
        val authInterface = mock<AuthInterface>(
            AuthInterface::register returns MockResponses.Auth.successfulLoginResponse,
            AuthInterface::getPublicOauthKey returns MockResponses.Auth.successfulPublicKeyResponse
        )
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var emailString: String
        lateinit var passwordString: String
        lateinit var confirmPasswordString: String
        lateinit var registerString: String

        launchTest {
            RegistrationRoute(
                navigate = {
                    when (it) {
                        Route.WebView.route -> assertEquals(Route.WebView.route, it)
                        else -> fail("Unexpected route: $it")
                    }
                },
                navigateTop = {
                    assertEquals(Route.Home.route, it)
                })

            emailString = stringResource(R.string.email)
            passwordString = stringResource(R.string.password)
            confirmPasswordString = stringResource(R.string.confirm_password)
            registerString = stringResource(R.string.register)
        }

        with(composeTestRule) {
            onNodeWithText(emailString)
                .performTextInput("email@email.com")
            onNodeWithText(passwordString)
                .performTextInput("password123")
            onNodeWithText(confirmPasswordString)
                .performTextInput("password123")
            val switches = onAllNodes(withRole(Role.Switch) and hasClickAction())
            switches[0].performClick()
            switches[1].performClick()
            onAllNodes(withRole(Role.Button) and hasClickAction() and hasText(registerString))
                .onFirst()
                .performClick()
        }

    }

    @Test
    fun testFailedRegistration() {
        val apiInterface = mock<ApiInterface>()
        val authInterface = mock<AuthInterface>(
            AuthInterface::register returns MockResponses.Auth.failedLoginResponse,
            AuthInterface::getPublicOauthKey returns MockResponses.Auth.successfulPublicKeyResponse
        )
        Singletons.setMockInterfaces(authInterface, apiInterface)

        lateinit var emailString: String
        lateinit var passwordString: String
        lateinit var confirmPasswordString: String
        lateinit var errorString: String
        lateinit var registerString: String

        launchTest {
            RegistrationRoute(
                navigate = {
                    when (it) {
                        Route.WebView.route -> assertEquals(Route.WebView.route, it)
                        else -> fail("Unexpected route: $it")
                    }
                },
                navigateTop = { fail("Failed registration attempts shouldn't navigate anywhere") })

            emailString = stringResource(R.string.email)
            passwordString = stringResource(R.string.password)
            confirmPasswordString = stringResource(R.string.confirm_password)
            errorString = stringResource(R.string.generic_sorry)
            registerString = stringResource(R.string.register)
        }

        with(composeTestRule) {
            onNodeWithText(emailString)
                .performTextInput("email@email.com")
            onNodeWithText(passwordString)
                .performTextInput("password123")
            onNodeWithText(confirmPasswordString)
                .performTextInput("password123")
            val switches = onAllNodes(withRole(Role.Switch) and hasClickAction())
            switches[0].performClick()
            switches[1].performClick()
            onAllNodes(withRole(Role.Button) and hasClickAction() and hasText(registerString))
                .onLast()
                .performClick()

            onNodeWithText(errorString)
                .assertIsDisplayed()
        }
    }
}