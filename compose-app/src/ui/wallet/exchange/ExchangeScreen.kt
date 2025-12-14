package ui.wallet.exchange

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.navigation.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Exchange

@Composable
public fun ExchangeScreen(
    modifier: Modifier = Modifier,
    route: Exchange = Exchange,
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
) {
}

@Preview
@Composable
private fun PreviewStockScreen(): Unit = ExchangeScreen()
