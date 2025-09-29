package ui.wallet.crypto

import androidx.compose.runtime.Composable
import ui.navigation.presentation.Destination
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
public fun CryptoScreen(
    navigateTo: (route: Destination) -> Unit = {},
    navigateBack: () -> Unit = {}
) {
}

@Preview
@Composable
public fun PreviewCryptoScreen(): Unit = CryptoScreen()
