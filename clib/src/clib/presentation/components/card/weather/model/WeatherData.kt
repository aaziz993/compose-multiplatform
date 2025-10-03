package clib.presentation.components.card.weather.model

import clib.presentation.Color
import kotlinx.serialization.Serializable

@Serializable
public data class WeatherData(
    val location: String,
    val temperature: Int,
    val condition: WeatherCondition,
    val high: Int,
    val low: Int,
    val visibility: String,
    val windSpeed: String,
    val humidity: Int,
    val feelsLike: Int,
    val backgroundColors: List<Color>
)
