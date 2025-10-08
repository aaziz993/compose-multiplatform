package ui.wallet.stock

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.NavRoute
import ui.navigation.presentation.Stock

@Composable
public fun StockScreen(
    route:Stock,
    navigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewStockScreen(): Unit = StockScreen(Stock)
