package clib.presentation.quickaccess

import kotlinx.serialization.Serializable

@Serializable
public data class QuickAccess(
    override val isConnectivityIndicator: Boolean = true,
    override val isSupport: Boolean = true,
    override val isTheme: Boolean = true,
    override val isLocale: Boolean = true,
    override val isAvatar: Boolean = true,
    override val isAvatarConnectivityIndicator: Boolean = true,
) : BaseQuickAccess
