package clib.presentation.components.card.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import clib.presentation.components.card.glass.GlassCard
import clib.presentation.components.card.weather.model.HourlyWeather
import clib.presentation.components.card.weather.model.WeatherData

@Composable
public fun WeatherGlassOverlay(
    currentWeather: WeatherData,
    hourlyForecast: List<HourlyWeather>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        // Background with weather-appropriate gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = currentWeather.backgroundColors.map(clib.presentation.Color::toColor),
                    ),
                ),
        )

        // Animated background elements (clouds, rain, etc.)
        WeatherBackgroundAnimation(currentWeather.condition)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            item {
                // Location and current temp
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = currentWeather.location,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                        )

                        Text(
                            text = "${currentWeather.temperature}°",
                            style = MaterialTheme.typography.displayLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Light,
                        )

                        Text(
                            text = currentWeather.condition.name,
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White.copy(alpha = 0.8f),
                        )

                        Text(
                            text = "H:${currentWeather.high}° L:${currentWeather.low}°",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f),
                        )
                    }
                }
            }

            item {
                // Hourly forecast
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column {
                        Text(
                            text = "HOURLY FORECAST",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.padding(bottom = 12.dp),
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                        ) {
                            items(hourlyForecast) { hour ->
                                HourlyWeatherItem(hour)
                            }
                        }
                    }
                }
            }

            item {
                // Weather details grid
                GlassCard(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.height(200.dp),
                    ) {
                        item {
                            WeatherDetailItem(
                                icon = Icons.Default.Visibility,
                                label = "VISIBILITY",
                                value = currentWeather.visibility,
                            )
                        }
                        item {
                            WeatherDetailItem(
                                icon = Icons.Default.Air,
                                label = "WIND",
                                value = "${currentWeather.windSpeed} mph",
                            )
                        }
                        item {
                            WeatherDetailItem(
                                icon = Icons.Default.WaterDrop,
                                label = "HUMIDITY",
                                value = "${currentWeather.humidity}%",
                            )
                        }
                        item {
                            WeatherDetailItem(
                                icon = Icons.Default.Thermostat,
                                label = "FEELS LIKE",
                                value = "${currentWeather.feelsLike}°",
                            )
                        }
                    }
                }
            }
        }
    }
}


