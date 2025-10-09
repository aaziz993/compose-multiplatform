package ui.news

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Home
import ui.navigation.presentation.News
import ui.navigation.presentation.Services

@Composable
public fun NewsScreen(
    modifier: Modifier = Modifier,
    route: News = News,
    navigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewNewsScreen(): Unit = NewsScreen()
