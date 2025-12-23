package ui.auth.login.presentation.viewmodel

import com.sunildhiman90.kmauth.supabase.model.SupabaseOAuthProvider

public sealed interface LoginAction {
    public data class SetUsername(val value: String) : LoginAction
    public data class SetPassword(val value: String) : LoginAction
    public data class SetShowPassword(val value: Boolean) : LoginAction
    public data class SetRemember(val value: Boolean) : LoginAction
    public data object Login : LoginAction
    public data object LoginGoogle : LoginAction
    public data object LoginApple : LoginAction
    public data class LoginSupabase(val value: SupabaseOAuthProvider) : LoginAction
}
