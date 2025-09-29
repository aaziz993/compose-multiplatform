package clib.ui.presentation.components.card.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import clib.ui.presentation.components.card.glass.GlassCard
import clib.ui.presentation.components.card.weather.model.HourlyWeather

@Composable
public fun HourlyWeatherItem(
    hour: HourlyWeather,
    modifier: Modifier = Modifier,
    size: Dp = 86.dp
) {
    // pick icon based on condition string (fallback to sunny)
    val icon = remember(hour.condition) { hour.condition.icon }

    GlassCard(modifier = modifier.size(size)) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(8.dp),
        ) {
            Text(
                text = hour.time,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.85f),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Icon(
                imageVector = icon,
                contentDescription = hour.condition.name,
                tint = Color.White.copy(alpha = 0.95f),
                modifier = Modifier.size(28.dp),
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "${hour.temperature}°",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
