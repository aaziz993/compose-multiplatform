package ui.wallet.crypto

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.components.navigation.model.NavigationRoute
import clib.presentation.components.navigation.viewmodel.NavigationAction
import org.jetbrains.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Crypto
import ui.navigation.presentation.NavRoute

@Composable
public fun CryptoScreen(
    modifier: Modifier = Modifier,
    route: Crypto = Crypto,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
public fun PreviewCryptoScreen(): Unit = CryptoScreen()
