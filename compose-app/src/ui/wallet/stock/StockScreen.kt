package ui.wallet.stock

import androidx.compose.runtime.Composable
import ui.navigation.presentation.Route
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun StockScreen(
    navigateTo: (route: Route) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewStockScreen(): Unit = StockScreen()
