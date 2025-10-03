package wallet.balance

import androidx.compose.runtime.Composable
import navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun BalanceScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewBalanceScreen(): Unit = BalanceScreen()
