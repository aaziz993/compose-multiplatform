package clib.presentation.components.auth.stateholder

import klib.data.type.auth.model.Auth
import klib.data.type.auth.model.User

public sealed interface AuthAction {
    public data class SetAuth(val value: Auth) : AuthAction

    public data class SetProvider(val value: String?) : AuthAction

    public data class SetUser(val value: User?) : AuthAction
}
