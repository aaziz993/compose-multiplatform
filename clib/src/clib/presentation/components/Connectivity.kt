package clib.presentation.components

import kotlinx.serialization.Serializable

@Serializable
public data class Connectivity(
    public val isConnectivityAlert: Boolean = false,
    public val isConnectivitySnackbar: Boolean = false,
    public val isConnectivityIndicator: Boolean = true,
    public val isConnectivityIndicatorText: Boolean = true,
    public val isAvatarConnectivityIndicator: Boolean = true,
)
