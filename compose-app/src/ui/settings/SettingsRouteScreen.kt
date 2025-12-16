package ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import com.alorma.compose.settings.ui.SettingsCheckbox
import com.alorma.compose.settings.ui.SettingsGroup
import compose_app.generated.resources.Res
import compose_app.generated.resources.enabled
import compose_app.generated.resources.label
import data.type.primitives.string.asStringResource
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
    onRoutesChange: (Map<String, RouteConfig>) -> Unit = { },
): Unit = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.Center,
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
            onRoutesChange(routes + (route to value))
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
        trueIcon = Icons.Default.RadioButtonChecked,
        falseIcon = Icons.Default.RadioButtonUnchecked,
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

    value.metadata?.let { valueMetadata ->
        metadataList.forEach { metadata ->
            SettingsCheckbox(
                metadata.keys.all { key -> key in valueMetadata },
                { Text(metadata.keys.map { it.asStringResource() }.joinToString("\n")) },
            ) {
                onValueChange(
                    value.copy(
                        metadata = if (it) valueMetadata + metadata
                        else valueMetadata.filterKeys { key -> key !in metadata },
                    ),
                )
            }
        }
    }
}
