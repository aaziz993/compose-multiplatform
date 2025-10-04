package ui.wallet.crypto

import androidx.compose.runtime.Composable
import clib.presentation.components.navigation.NavigationRoute
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Crypto
import ui.navigation.presentation.NavRoute

@Composable
public fun CryptoScreen(
    route: Crypto,
    navigateTo: (route: NavigationRoute<NavRoute, *>) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewCryptoScreen(): Unit = CryptoScreen(Crypto)
