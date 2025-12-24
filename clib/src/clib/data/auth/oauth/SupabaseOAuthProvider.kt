package clib.data.auth.oauth

import androidx.compose.material.icons.Icons
import clib.presentation.icons.Keycloak
import clib.presentation.icons.X
import com.sunildhiman90.kmauth.supabase.model.SupabaseOAuthProvider
import compose.icons.SimpleIcons
import compose.icons.simpleicons.Apple
import compose.icons.simpleicons.Bitbucket
import compose.icons.simpleicons.Discord
import compose.icons.simpleicons.Facebook
import compose.icons.simpleicons.Github
import compose.icons.simpleicons.Gitlab
import compose.icons.simpleicons.Google
import compose.icons.simpleicons.Linkedin
import compose.icons.simpleicons.Microsoftazure
import compose.icons.simpleicons.Slack
import compose.icons.simpleicons.Spotify
import compose.icons.simpleicons.Twitch

public fun SupabaseOAuthProvider.imageVector() = when (this) {
    SupabaseOAuthProvider.GITHUB -> SimpleIcons.Github
    SupabaseOAuthProvider.GITLAB -> SimpleIcons.Gitlab
    SupabaseOAuthProvider.BITBUCKET -> SimpleIcons.Bitbucket
    SupabaseOAuthProvider.TWITTER -> Icons.Default.X
    SupabaseOAuthProvider.DISCORD -> SimpleIcons.Discord
    SupabaseOAuthProvider.SLACK -> SimpleIcons.Slack
    SupabaseOAuthProvider.SPOTIFY -> SimpleIcons.Spotify
    SupabaseOAuthProvider.TWITCH -> SimpleIcons.Twitch
    SupabaseOAuthProvider.LINKEDIN -> SimpleIcons.Linkedin
    SupabaseOAuthProvider.KEYCLOAK -> Icons.Default.Keycloak
    SupabaseOAuthProvider.GOOGLE -> SimpleIcons.Google
    SupabaseOAuthProvider.FACEBOOK -> SimpleIcons.Facebook
    SupabaseOAuthProvider.AZURE -> SimpleIcons.Microsoftazure
    SupabaseOAuthProvider.APPLE -> SimpleIcons.Apple
}
