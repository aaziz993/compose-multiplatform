package presentation.auth.profile.presentation

import androidx.compose.runtime.Composable
import presentation.navigation.presentation.Destination
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
