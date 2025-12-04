package clib.presentation.components.image.avatar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import clib.presentation.components.image.AdvancedImage
import clib.presentation.components.image.avatar.model.AvatarLocalization
import klib.data.auth.model.User
import kotlin.math.absoluteValue

@Composable
public fun Avatar(
    firstName: String,
    lastName: String,
    modifier: Modifier = Modifier,
    imageSource: Any? = null,
    contentDescription: String = "",
    localization: AvatarLocalization = AvatarLocalization(),
    content: (@Composable BoxScope.() -> Unit)? = null
): Unit =
    Box {
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
    localization: AvatarLocalization = AvatarLocalization(),
    content: (@Composable BoxScope.() -> Unit)? = null
): Unit = Avatar(
    user.firstName.orEmpty(),
    user.lastName.orEmpty(),
    modifier,
    user.image,
    contentDescription,
    localization,
    content,
)

@Composable
private fun AvatarActions(
    onSingOut: () -> Unit
) {
    // Menu customization parameters
    val numItems by parameter(4) // Values between 1-4
    val menuWidth by parameter(180f)
    val useHighlightedBackground by parameter(false)

    // State to control menu visibility
    var showMenu by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.padding(16.dp),
    ) {
        Column(
            modifier = Modifier
                .width(250.dp)
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                "DropdownMenu Demo",
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Container with fixed height that holds both the button and menu
            Box(
                modifier = Modifier.height(300.dp),
                contentAlignment = Alignment.TopStart,
            ) {
                // Menu button
                IconButton(
                    onClick = { showMenu = !showMenu },
                ) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Menu",
                    )
                }

                // Custom menu implementation using ElevatedCard
                if (showMenu) {
                    ElevatedCard(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .width(menuWidth.dp),
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(
                            defaultElevation = 8.dp, // Increased elevation for better visibility in dark theme
                            pressedElevation = 12.dp,
                        ),
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = if (useHighlightedBackground)
                                MaterialTheme.colorScheme.surfaceContainerLow
                            else
                                MaterialTheme.colorScheme.surfaceContainerHigh, // Using surfaceContainerHigh for better contrast in dark theme
                        ),
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp),
                        ) {
                            // Menu items - show based on numItems parameter (values 1-4)
                            if (numItems >= 1) {
                                DropdownMenuItem(
                                    text = { Text("Create New Item", color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = { showMenu = false },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Add,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                        )
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                    ),
                                )
                            }

                            if (numItems >= 2) {
                                DropdownMenuItem(
                                    text = { Text("Edit Profile", color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = { showMenu = false },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Person,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                        )
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                    ),
                                )
                            }

                            if (numItems >= 3) {
                                DropdownMenuItem(
                                    text = { Text("Add to Favorites", color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = { showMenu = false },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Favorite,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                        )
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                    ),
                                )
                            }

                            if (numItems >= 4) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                                DropdownMenuItem(
                                    text = { Text("Share with Friends", color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = { showMenu = false },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Share,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurface,
                                        )
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = MaterialTheme.colorScheme.onSurface,
                                        leadingIconColor = MaterialTheme.colorScheme.onSurface,
                                        trailingIconColor = MaterialTheme.colorScheme.onSurface,
                                    ),
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Click the Menu button to open the dropdown. " +
                    "Valid values for the numItems parameter " +
                    "in the current implementation range from 0 to 4.",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}
