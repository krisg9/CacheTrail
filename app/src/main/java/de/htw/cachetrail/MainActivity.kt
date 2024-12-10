package de.htw.cachetrail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import de.htw.cachetrail.ui.nav.AppNavHost
import de.htw.cachetrail.ui.theme.CacheTrailTheme
import org.osmdroid.config.Configuration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CacheTrailTheme {
                Configuration.getInstance()
                    .load(
                        this,
                        applicationContext.getSharedPreferences("osm_pref", MODE_PRIVATE)
                    )
                Column(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
                    .windowInsetsPadding(
                        WindowInsets.systemBars
                        .exclude(WindowInsets.displayCutout))
                ) {
                    AppNavHost(navController = rememberNavController())
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview() {
    AppNavHost(navController = rememberNavController())
}