package ui.wallet.crypto

import androidx.compose.runtime.Composable
import ui.navigation.presentation.Route
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun CryptoScreen(
    navigateTo: (route: Route) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewCryptoScreen(): Unit = CryptoScreen()
