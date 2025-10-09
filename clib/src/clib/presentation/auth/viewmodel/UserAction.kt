package clib.presentation.auth.viewmodel

import klib.data.type.auth.User

public sealed interface UserAction {
    public data class SetUser(val value: User?) : UserAction
}
