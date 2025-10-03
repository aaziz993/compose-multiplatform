package presentation.wallet.crypto

import androidx.compose.runtime.Composable
import presentation.navigation.presentation.Destination
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
