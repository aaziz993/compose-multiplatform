package ui.wallet.stock

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.NavRoute
import ui.navigation.presentation.Stock

@Composable
public fun StockScreen(
    route:Stock,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewStockScreen(): Unit = StockScreen(Stock)
