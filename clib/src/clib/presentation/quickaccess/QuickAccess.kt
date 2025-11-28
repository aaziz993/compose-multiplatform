package clib.presentation.quickaccess

import kotlinx.serialization.Serializable

@Serializable
public data class QuickAccess(
    override val isSupport: Boolean = true,
    override val isTheme: Boolean = true,
    override val isLocale: Boolean = true,
    override val isAvatar: Boolean = true,
) : BaseQuickAccess
