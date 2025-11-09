package ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import clib.presentation.components.navigation.stateholder.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Home

@Composable
public fun HomeScreen(
    modifier: Modifier = Modifier,
    route: Home = Home,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        val list = (0..75).map { it.toString() }
        items(count = list.size) {
            Text(
                text = list[it],
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            )
        }
    }
}

@Preview
@Composable
public fun PreviewHomeScreen(): Unit = HomeScreen()
