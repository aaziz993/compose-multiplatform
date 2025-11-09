package ui.wallet.stock

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.stateholder.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Stock

@Composable
public fun StockScreen(
    modifier: Modifier = Modifier,
    route: Stock = Stock,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewStockScreen(): Unit = StockScreen()
