package ui.auth.profile.presentation

import androidx.compose.runtime.Composable
import ui.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun ProfileScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewProfileScreen(): Unit = ProfileScreen()
