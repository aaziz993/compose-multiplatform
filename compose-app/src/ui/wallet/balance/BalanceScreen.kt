package ui.wallet.balance

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Balance
import ui.navigation.presentation.NavRoute

@Composable
public fun BalanceScreen(
    modifier: Modifier = Modifier,
    route: Balance = Balance,
    navigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewBalanceScreen(): Unit = BalanceScreen()
