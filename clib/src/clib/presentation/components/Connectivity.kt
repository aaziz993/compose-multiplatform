package clib.presentation.components

import kotlinx.serialization.Serializable

@Serializable
public data class Connectivity(
    public val isConnectivityIndicator: Boolean = true,
    public val isConnectivitySnackbar: Boolean = false,
    public val isConnectivityAlert: Boolean = false,
    public val isAvatarConnectivityIndicator: Boolean = true,
)
