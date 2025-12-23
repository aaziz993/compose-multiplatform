package ui.services

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import clib.data.type.primitives.string.stringResource
import clib.presentation.navigation.NavigationAction
import compose_app.generated.resources.Res
import compose_app.generated.resources.services
import ui.navigation.presentation.Services

@Composable
public fun ServicesScreen(
    modifier: Modifier = Modifier,
    route: Services = Services,
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
) {
    Column {
        Text(stringResource(Res.string.services))
    }
}

@Preview
@Composable
private fun PreviewServicesScreen(): Unit = ServicesScreen()
