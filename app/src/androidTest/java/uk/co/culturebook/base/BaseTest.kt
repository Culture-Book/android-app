package uk.co.culturebook.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.navigation.NavController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import org.junit.Before
import org.junit.Rule
import uk.co.culturebook.data.remote.interfaces.ApiInterface
import uk.co.culturebook.data.remote.interfaces.AuthInterface
import uk.co.culturebook.data.remote.retrofit.ClientFactory
import uk.co.culturebook.ui.theme.AppTheme

abstract class BaseTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Before
    fun setup() {
        ClientFactory.getApiInstance(MockTestApi()).createService(ApiInterface::class.java)
        ClientFactory.getAuthInstance(MockTestAuth()).createService(AuthInterface::class.java)
    }

    fun launchTest(block: @Composable (NavController) -> Unit) =
        composeTestRule.setContent {
            AppTheme {
                val context = LocalContext.current
                val navController = remember {
                    TestNavHostController(context).apply {
                        navigatorProvider.addNavigator(ComposeNavigator())
                    }
                }
                block(navController)
            }
        }
}