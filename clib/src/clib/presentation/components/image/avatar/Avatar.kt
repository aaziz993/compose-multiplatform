package clib.presentation.components.image.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import clib.presentation.components.image.AdvancedImage
import clib.presentation.components.image.avatar.model.AvatarView
import klib.data.auth.model.User
import kotlin.math.absoluteValue

@Suppress("ComposeModifierReused")
@Composable
public fun Avatar(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    imageSource: Any? = null,
    contentDescription: String = "",
    view: AvatarView = AvatarView(),
    content: (@Composable BoxScope.() -> Unit)? = null
): Unit = Box {
    if (imageSource == null) InitialsAvatar(
        firstName,
        lastName,
        modifier,
    )
    else AdvancedImage(
        imageSource,
        contentDescription,
        modifier,
    )
    content?.invoke(this)
}

@Composable
internal fun InitialsAvatar(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
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
    Box(
        modifier = modifier
            .background(color = color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = (firstName.take(1) + lastName.take(1)).uppercase(),
        )
    }
}

@Composable
public fun Avatar(
    user: User,
    modifier: Modifier = Modifier,
    contentDescription: String = "",
    view: AvatarView = AvatarView(),
    content: (@Composable BoxScope.() -> Unit)? = null
): Unit = Avatar(
    user.firstName.orEmpty(),
    user.lastName.orEmpty(),
    modifier,
    user.image,
    contentDescription,
    view,
    content,
)
