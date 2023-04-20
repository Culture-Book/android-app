package uk.co.culturebook.base

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.After
import org.junit.Rule
import uk.co.culturebook.data.Singletons
import uk.co.culturebook.data.sharedPreferences
import uk.co.culturebook.ui.theme.AppTheme

abstract class BaseTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    var testContext: Context? = null

    @After
    fun tearDown() {
        testContext?.sharedPreferences?.edit()?.clear()?.commit()
        Singletons.resetInterfaces()

        // Don't forget to set this to null, otherwise it will leak the context
        testContext = null
    }

    fun launchTest(block: @Composable () -> Unit) =
        composeTestRule.setContent {
            testContext = LocalContext.current
            AppTheme {
                block()
            }
        }
}