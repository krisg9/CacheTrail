package de.htw.cachetrail.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isDialog
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import de.htw.cachetrail.ui.nav.AppNavHost
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAddNewTrailDialogAppears() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            AppNavHost(navController)
        }

        composeTestRule.onNodeWithText("Edit trails").assertExists().performClick()
        composeTestRule.onNodeWithText("Add new Trail").assertExists().performClick()

        composeTestRule.onNodeWithText("Trail Name").performTextInput("Test Trail1")
        composeTestRule.onNodeWithText("Add Trail").assertIsEnabled().performClick()
        composeTestRule.onNodeWithText("Test Trail1").assertExists().performClick()

        composeTestRule.onNodeWithText("Add new Station").assertExists().performClick()
        composeTestRule.onNodeWithText("Question").performTextInput("Test Station1")
        composeTestRule.onNodeWithText("Answer").performTextInput("Test Answer1")
        composeTestRule.onNodeWithText("Pick Location").performClick()
        composeTestRule.onNodeWithText("Add Marker").assertIsNotEnabled()

        Thread.sleep(5000)
        composeTestRule.onNodeWithText("MapWindow").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("MapWindow").assertExists().performClick()
    }
}
