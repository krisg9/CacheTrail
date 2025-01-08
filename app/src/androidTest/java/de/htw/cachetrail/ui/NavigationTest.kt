package de.htw.cachetrail.ui

import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import de.htw.cachetrail.ui.screens.EditScreen
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testAddNewTrailDialogAppears() {
        composeTestRule.setContent {
            EditScreen(navController = rememberNavController())
        }

        composeTestRule.onNodeWithText("Add new Trail").assertExists().performClick()
        composeTestRule.onNodeWithText("Trail Name").performTextInput("Test Trail1")
        composeTestRule.onNodeWithText("Add Trail").assertIsEnabled()
        composeTestRule.onNodeWithText("Add Trail").performClick()
        composeTestRule.onNodeWithText("Test Trail1").assertExists()
    }
}
