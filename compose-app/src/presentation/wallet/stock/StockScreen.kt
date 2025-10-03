package presentation.wallet.stock

import androidx.compose.runtime.Composable
import presentation.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun StockScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewStockScreen(): Unit = StockScreen()
