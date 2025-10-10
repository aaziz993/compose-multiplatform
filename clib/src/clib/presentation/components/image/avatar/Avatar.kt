package clib.presentation.components.image.avatar

import clib.presentation.components.image.AdvancedImage
import clib.presentation.components.image.avatar.model.AvatarLocalization
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import klib.data.type.auth.model.User
import kotlin.math.absoluteValue

@Composable
public fun Avatar(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    imageSource: Any? = null,
    contentDescription: String = "",
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    localization: AvatarLocalization = AvatarLocalization(),
    action: (@Composable BoxScope.() -> Unit)? = null
): Unit =
    Box {
        if (imageSource == null) InitialsAvatar(
            firstName,
            lastName,
            textStyle,
            modifier,
        )
        else AdvancedImage(
            imageSource,
            contentDescription,
            modifier,
        )
        action?.invoke(this)
    }

@Composable
internal fun InitialsAvatar(
    firstName: String,
    lastName: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        val color =
            remember(firstName, lastName) {
                val name =
                    listOf(firstName, lastName)
                        .joinToString(separator = "")
                        .uppercase()
                Color(
                    (name.fold(0) { acc, char -> char.code + acc } / (name.length * 1000)).absoluteValue.toFloat(),
                    0.5f,
                    0.4f,
                )
            }
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(SolidColor(color))
        }
        Text(
            text = (firstName.take(1) + lastName.take(1)).uppercase(),
            style = textStyle,
            color = Color.White,
        )
    }
}

@Composable
public fun Avatart(
    user: User,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    textStyle: TextStyle = MaterialTheme.typography.titleSmall,
    localization: AvatarLocalization = AvatarLocalization(),
    action: (@Composable BoxScope.() -> Unit)? = null
): Unit = Avatar(
    user.firstName.orEmpty(),
    user.lastName.orEmpty(),
    modifier,
    user.image,
    contentDescription,
    textStyle,
    localization,
    action,
)

