package clib.presentation.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.ui.graphics.vector.ImageVector

public val String?.issuerIcon: ImageVector
    get() = when {
        this == null -> Icons.Default.Lock
        contains("amazon", ignoreCase = true) -> Icons.Default.Amazon
        contains("cloudflare", ignoreCase = true) -> Icons.Default.Cloudflare
        contains("dropbox", ignoreCase = true) -> Icons.Default.Dropbox
        contains("facebook", ignoreCase = true) -> Icons.Default.Facebook
        contains("github", ignoreCase = true) -> Icons.Default.Github
        contains("gitlab", ignoreCase = true) -> Icons.Default.Gitlab
        contains("google", ignoreCase = true) -> Icons.Default.Google
        contains("instagram", ignoreCase = true) -> Icons.Default.Instagram
        contains("microsoft", ignoreCase = true) -> Icons.Default.Microsoft
        contains("reddit", ignoreCase = true) -> Icons.Default.Reddit
        contains("twitter", ignoreCase = true) -> Icons.Default.Twitter
        else -> Icons.Default.Lock
    }
