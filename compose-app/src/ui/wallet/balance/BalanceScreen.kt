package ui.wallet.balance

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.model.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Balance
import ui.navigation.presentation.NavRoute

@Composable
public fun BalanceScreen(
    route:Balance,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewBalanceScreen(): Unit = BalanceScreen(Balance)
