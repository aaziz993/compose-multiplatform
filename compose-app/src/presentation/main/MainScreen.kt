package presentation.main

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clib.presentation.components.card.weather.WeatherGlassOverlay
import clib.presentation.components.card.weather.model.WeatherCondition
import clib.presentation.components.card.weather.model.WeatherData
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.navigation.presentation.Destination

@Composable
public fun MainScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
    Surface(Modifier.width(600.dp).height(480.dp)) {
        WeatherGlassOverlay(
            WeatherData(
                "Dushanbe",
                35,
                WeatherCondition.Sunny,
                0,
                100,
                "good",
                "1m/s",
                36,
                40,
                listOf(Color.Cyan, Color.Yellow).map { clib.presentation.Color(it.value) },
            ),
            emptyList(),
        )
    }
}

@Preview
@Composable
public fun PreviewMainScreen(): Unit = MainScreen()
