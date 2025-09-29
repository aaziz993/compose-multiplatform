package clib.ui.presentation.components.card.weather.model;

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

public enum class WeatherCondition(
    public val icon: ImageVector
) {
    Sunny(Icons.Default.WbSunny),
    Clear(Icons.Default.WbSunny),
    Cloudy(Icons.Default.Cloud),
    Overcast(Icons.Default.Cloud),
    Rain(Icons.Default.WaterDrop),
    Drizzle(Icons.Default.WaterDrop),
    Shower(Icons.Default.WaterDrop),
    Snow(Icons.Default.AcUnit),
    Sleet(Icons.Default.AcUnit),
    Unknown(Icons.Default.WbSunny);

    public companion object {
        public fun from(condition: String): WeatherCondition {
            val lower = condition.lowercase()
            return entries.firstOrNull { entry ->
                lower.contains(entry.name.lowercase())
            } ?: Unknown
        }
    }
}
