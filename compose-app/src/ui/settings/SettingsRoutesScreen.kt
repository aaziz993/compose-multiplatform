package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.type.primitives.string.stringResource
import clib.presentation.config.RouteConfig
import com.alorma.compose.settings.ui.SettingsGroup
import compose_app.generated.resources.Res
import compose_app.generated.resources.enabled
import compose_app.generated.resources.label
import data.type.primitives.string.asStringResource
import presentation.components.settings.SettingsSwitch
import ui.navigation.presentation.SettingsRoutes

@Composable
public fun SettingsRoutesScreen(
    modifier: Modifier = Modifier,
    route: SettingsRoutes = SettingsRoutes,
    defaultRoutes: Map<String, RouteConfig> = emptyMap(),
    routes: Map<String, RouteConfig> = defaultRoutes,
    onRouteChange: (String, RouteConfig) -> Unit = { _, _ -> },
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    routes.forEach { (route, config) ->
        SettingsRouteConfig(
            route.asStringResource(),
            config,
        ) { value ->
            onRouteChange(route, value)
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsRoutesScreen(): Unit = SettingsRoutesScreen()

@Composable
private fun SettingsRouteConfig(
    title: String,
    value: RouteConfig,
    onValueChange: (RouteConfig) -> Unit = {},
) = SettingsGroup(
    modifier = Modifier,
    enabled = true,
    title = { Text(title) },
    contentPadding = PaddingValues(16.dp),
) {
    SettingsSwitch(
        title = stringResource(Res.string.enabled),
        value = value.enabled,
        trueIcon = Icons.Filled.Check,
        falseIcon = Icons.Outlined.Check,
    ) {
        onValueChange(value.copy(enabled = it))
    }

    SettingsSwitch(
        title = stringResource(Res.string.label),
        value = value.alwaysShowLabel,
        trueIcon = Icons.AutoMirrored.Filled.Label,
        falseIcon = Icons.AutoMirrored.Outlined.Label,
    ) {
        onValueChange(value.copy(alwaysShowLabel = it))
    }
}
