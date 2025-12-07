package ui.wallet.crypto

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.navigation.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import ui.navigation.presentation.Crypto

@Composable
public fun CryptoScreen(
    modifier: Modifier = Modifier,
    route: Crypto = Crypto,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
}

@Preview
@Composable
private fun PreviewCryptoScreen(): Unit = CryptoScreen()
