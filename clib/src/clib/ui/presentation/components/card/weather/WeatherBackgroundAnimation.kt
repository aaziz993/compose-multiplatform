package clib.ui.presentation.components.card.weather

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.graphicsLayer
import clib.ui.presentation.components.card.weather.model.WeatherCondition

@Composable
public fun WeatherBackgroundAnimation(condition: WeatherCondition, modifier: Modifier = Modifier) {
    // speeds: slower for background cloud, faster for foreground cloud
    val infinite = rememberInfiniteTransition()

    val cloud1Progress = infinite.animateFloat(
        initialValue = -1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 55_000, easing = LinearEasing),
        ),
    )
    val cloud2Progress = infinite.animateFloat(
        initialValue = -1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 36_000, easing = LinearEasing),
        ),
    )

    // little vertical bob for foreground cloud
    val bob = infinite.animateFloat(
        initialValue = -2f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 2000
            },
        ),
    )

    // Use density to convert fraction -> px offset
    val density = LocalDensity.current

    // Draw two clouds as icons translated horizontally. Keep them faint (alpha).
    // The `graphicsLayer` translation is in px so convert from Dp/parent size approximation.
    // We don't know parent width here; approximate by mapping progress→ translation as percent.
    // That's fine for a decorative effect.
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        // background cloud (big, faint)
        val cloud1Size = 220.dp
        Icon(
            imageVector = Icons.Default.Cloud,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.06f),
            modifier = Modifier
                .size(cloud1Size)
                .graphicsLayer {
                    // -1..1.2 -> map to -w..+w
                    translationX = with(density) { (cloud1Progress.value * 600).toDp().toPx() }
                },
        )

        // foreground cloud
        Icon(
            imageVector = Icons.Default.Cloud,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.12f),
            modifier = Modifier
                .size(140.dp)
                .graphicsLayer {
                    translationX = with(density) { (cloud2Progress.value * 420).toDp().toPx() }
                    translationY = bob.value
                },
        )
    }

    // Rain / snow hint: small drops or snowflakes falling (very lightweight)
    if (condition in listOf(WeatherCondition.Rain, WeatherCondition.Drizzle, WeatherCondition.Shower)) {
        // draw a row of small water-drop icons with subtle alpha and slow vertical animation
        val dropAnim = rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(animation = tween(900, easing = LinearEasing)),
        )

        Row(
            Modifier
                .padding(top = 40.dp)
                .alpha(0.55f),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            repeat(6) {
                Icon(
                    imageVector = Icons.Default.WaterDrop,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.18f),
                    modifier = Modifier
                        .size(10.dp)
                        .graphicsLayer {
                            // simple up-down to simulate falling
                            translationY = (dropAnim.value * 18f * (it % 3 + 1))
                        },
                )
            }
        }
    }
    else if (condition == WeatherCondition.Snow) {
        // simple snowflakes (reusing AcUnit)
        Row(
            Modifier
                .padding(top = 30.dp)
                .alpha(0.45f),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            repeat(5) { i ->
                Icon(
                    imageVector = Icons.Default.AcUnit,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.16f),
                    modifier = Modifier
                        .size(12.dp)
                        .graphicsLayer {
                            translationY = ((i + 1) * 6).toFloat()
                        },
                )
            }
        }
    }
}
