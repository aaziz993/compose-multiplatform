package ui.services

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import clib.presentation.navigation.NavigationAction
import androidx.compose.ui.tooling.preview.Preview
import clib.presentation.icons.Amazon
import clib.presentation.icons.Cloudflare
import clib.presentation.icons.Dropbox
import clib.presentation.icons.Facebook
import clib.presentation.icons.Github
import clib.presentation.icons.Gitlab
import clib.presentation.icons.Google
import clib.presentation.icons.Instagram
import clib.presentation.icons.Microsoft
import clib.presentation.icons.OneDrive
import clib.presentation.icons.Otp
import clib.presentation.icons.Reddit
import clib.presentation.icons.Twitter
import ui.navigation.presentation.Services

@Composable
public fun ServicesScreen(
    modifier: Modifier = Modifier,
    route: Services = Services,
    onNavigationAction: (NavigationAction) -> Unit = {},
) {
    Row {
        Icon(Icons.Default.Amazon, "Amazon")
        Icon(Icons.Default.Cloudflare, "Cloudflare")
        Icon(Icons.Default.Dropbox, "Dropbox")
        Icon(Icons.Default.Facebook, "Facebook")
        Icon(Icons.Default.Github, "Github")
        Icon(Icons.Default.Gitlab, "Gitlab")
        Icon(Icons.Default.Google, "Google")
        Icon(Icons.Default.Instagram, "Instagram")
        Icon(Icons.Default.Microsoft, "Microsoft")
        Icon(Icons.Default.OneDrive, "OneDrive")
        Icon(Icons.Default.Otp, "Otp")
        Icon(Icons.Default.Reddit, "Reddit")
        Icon(Icons.Default.Twitter, "Twitter")
    }
}

@Preview
@Composable
private fun PreviewServicesScreen(): Unit = ServicesScreen()
