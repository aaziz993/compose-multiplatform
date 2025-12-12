package ui.wallet.balance

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.navigation.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Balance

@Composable
public fun BalanceScreen(
    modifier: Modifier = Modifier,
    route: Balance = Balance,
    onNavigationActions: (Array<NavigationAction>) -> Unit = {},
) {
}

@Preview
@Composable
private fun PreviewBalanceScreen(): Unit = BalanceScreen()
