package clib.presentation.connectivity.model

import kotlinx.serialization.Serializable

@Serializable
public data class Connectivity(
    public val isConnectivityAlert: Boolean = false,
    public val isConnectivitySnackbar: Boolean = false,
    public val isConnectivityIndicator: Boolean = true,
    public val isConnectivityIndicatorText: Boolean = false,
    public val isAvatarConnectivityIndicator: Boolean = true,
)
