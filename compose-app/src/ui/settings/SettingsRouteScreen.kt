package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.outlined.DisplaySettings
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import clib.data.type.primitives.string.stringResource
import clib.presentation.config.RouteConfig
import clib.presentation.navigation.slideTransition
import com.alorma.compose.settings.ui.SettingsGroup
import compose_app.generated.resources.Res
import compose_app.generated.resources.enabled
import compose_app.generated.resources.label
import data.type.primitives.string.asStringResource
import klib.data.type.primitives.string.LINE_SEPARATOR
import presentation.components.settings.SettingsSwitch
import ui.navigation.presentation.NavSuiteSceneStrategy
import ui.navigation.presentation.NavSuiteTopAppBarSceneStrategy
import ui.navigation.presentation.SettingsRoute
import ui.navigation.presentation.TopAppBarNavSuiteSceneStrategy
import ui.navigation.presentation.TopAppBarSceneStrategy

@Composable
public fun SettingsRouteScreen(
    modifier: Modifier = Modifier,
    route: SettingsRoute = SettingsRoute,
    routes: Map<String, RouteConfig> = emptyMap(),
    onRouteChange: (String, RouteConfig) -> Unit = { _, _ -> },
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Top,
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    val metadataList = remember {
        listOf(
            slideTransition(),
            TopAppBarSceneStrategy.screen(),
            NavSuiteSceneStrategy.screen(),
            TopAppBarNavSuiteSceneStrategy.screen(),
            NavSuiteTopAppBarSceneStrategy.screen(),
        )
    }

    routes.forEach { (route, config) ->
        SettingsRouteConfig(
            route.asStringResource(),
            metadataList,
            config,
        ) { value ->
            onRouteChange(route, value)
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsRouteScreen(): Unit = SettingsRouteScreen()

@Composable
private fun SettingsRouteConfig(
    title: String,
    metadataList: List<Map<String, Any>>,
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
        trueIcon = Icons.Filled.Route,
        falseIcon = Icons.Outlined.Route,
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

    metadataList.forEach { metadata ->
        SettingsSwitch(
            title = metadata.keys.map { it.asStringResource() }.joinToString(String.LINE_SEPARATOR),
            metadata.keys.all { key -> key in value.metadata || key in value.additionalMetadata },
            trueIcon = Icons.Filled.DisplaySettings,
            falseIcon = Icons.Outlined.DisplaySettings,
        ) {
            onValueChange(
                value.copy(
                    metadata = if (it) value.metadata + metadata else value.metadata - metadata.keys,
                    additionalMetadata = if (it) value.additionalMetadata else value.additionalMetadata - metadata.keys,
                ),
            )
        }
    }
}
