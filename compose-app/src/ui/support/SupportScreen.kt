package ui.support

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.navigation.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Support

@Composable
public fun SupportScreen(
    modifier: Modifier = Modifier,
    route: Support = Support,
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
) {
}

@Preview
@Composable
private fun PreviewSupportScreen(): Unit = SupportScreen()
