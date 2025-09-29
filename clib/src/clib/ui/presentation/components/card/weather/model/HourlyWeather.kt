package clib.ui.presentation.components.card.weather.model

import kotlinx.serialization.Serializable

@Serializable
public data class HourlyWeather(
    val time: String,
    val temperature: Int,
    val condition: WeatherCondition,
    val icon: String
)
