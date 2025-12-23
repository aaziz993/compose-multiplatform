package ui.auth.login.presentation.viewmodel

import androidx.lifecycle.viewModelScope
import klib.data.type.collections.restartableflow.RestartableStateFlow
import clib.presentation.auth.AuthState
import clib.presentation.viewmodel.ViewModel
import com.sunildhiman90.kmauth.apple.AppleAuthManager
import com.sunildhiman90.kmauth.apple.KMAuthApple
import com.sunildhiman90.kmauth.google.GoogleAuthManager
import com.sunildhiman90.kmauth.google.KMAuthGoogle
import com.sunildhiman90.kmauth.supabase.KMAuthSupabase
import com.sunildhiman90.kmauth.supabase.SupabaseAuthManager
import com.sunildhiman90.kmauth.supabase.model.SupabaseAuthConfig
import com.sunildhiman90.kmauth.supabase.model.SupabaseDefaultAuthProvider
import com.sunildhiman90.kmauth.supabase.model.SupabaseOAuthProvider
import com.sunildhiman90.kmauth.supabase.model.toKMAuthUser
import klib.data.auth.model.Auth
import klib.data.auth.model.User
import klib.data.auth.model.toUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
public class LoginViewModel(
    private val authState: AuthState,
    // For Sign In With Google.
    private val googleAuthManager: GoogleAuthManager = KMAuthGoogle.googleAuthManager,
// For Sign In With Apple.
    private val appleAuthManager: AppleAuthManager = KMAuthApple.appleAuthManager,
// For Sign In With other providers such as Facebook, Github, Twitter etc.
// For these providers using supabase, for setting up the supabase for specific provider, refer to the supabase docs.
    private val supabaseAuthManager: SupabaseAuthManager = KMAuthSupabase.getAuthManager(),
) : ViewModel<LoginAction>() {

    public val state: RestartableStateFlow<LoginState>
        field = MutableStateFlow(LoginState()).onStartStateIn { it }

    override fun action(action: LoginAction): Unit = when (action) {
        is LoginAction.SetUsername -> setUsername(action.value)
        is LoginAction.SetPassword -> setPassword(action.value)
        is LoginAction.SetShowPassword -> setShowPassword(action.value)
        is LoginAction.SetRemember -> setRemember(action.value)
        is LoginAction.SetError -> setError(action.value)
        is LoginAction.Login -> login()
        is LoginAction.LoginGoogle -> loginGoogle()
        is LoginAction.LoginApple -> loginApple()
        is LoginAction.LoginSupabaseDefaultAuth -> loginSupabaseDefaultAuth(action.provider, action.config)
        is LoginAction.LoginSupabaseOAuth -> loginSupabaseOAuth(action.provider, action.config)
    }

    private fun setUsername(value: String) = state.update { it.copy(username = value, error = null) }

    private fun setPassword(value: String) = state.update { it.copy(password = value, error = null) }

    private fun setShowPassword(value: Boolean) = state.update { it.copy(showPassword = value, error = null) }

    private fun setRemember(value: Boolean) = state.update { it.copy(remember = value, error = null) }

    private fun setError(value: Throwable?) = state.update { it.copy(error = value) }

    private fun login() {
        viewModelScope.launch {
            if (state.value.password == "7890")
                authState.setUser(
                    User(
                        username = "jogn.doe@gmail.com",
                        firstName = "John",
                        lastName = "Doe",
                        roles = setOf("User"),
                    ),
                )
            else state.update { it.copy(error = Exception("Invalid username or password")) }
        }
    }

    private fun loginGoogle() {
        viewModelScope.launch {
            googleAuthManager.signIn()
                .onSuccess { user ->
                    user?.let { authState.value = Auth("google", it.toUser()) }
                }
                .onFailure { error ->
                    state.update { it.copy(error = error) }
                }
        }
    }

    private fun loginApple() {
        viewModelScope.launch {
            appleAuthManager.signIn()
                .onSuccess { user ->
                    user?.let { authState.value = Auth("apple", it.toUser()) }
                }
                .onFailure { error ->
                    state.update { it.copy(error = error) }
                }
        }
    }

    private fun loginSupabaseDefaultAuth(provider: SupabaseDefaultAuthProvider, config: SupabaseAuthConfig) {
        viewModelScope.launch {
            supabaseAuthManager.signInWith(provider, config)
                .onSuccess { user ->
                    user?.let { authState.value = Auth(provider.name, it.toKMAuthUser().toUser()) }
                }
                .onFailure { error ->
                    state.update { it.copy(error = error) }
                }
        }
    }

    private fun loginSupabaseOAuth(provider: SupabaseOAuthProvider, config: SupabaseAuthConfig) {
        viewModelScope.launch {
            supabaseAuthManager.signInWith(provider, config)
                .onSuccess { user ->
                    user?.let { authState.value = Auth(provider.name, it.toKMAuthUser().toUser()) }
                }
                .onFailure { error ->
                    state.update { it.copy(error = error) }
                }
        }
    }
}
