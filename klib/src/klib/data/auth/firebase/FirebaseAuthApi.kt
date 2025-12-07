package klib.data.auth.firebase

import klib.data.auth.firebase.model.CreateAuthUriRequest
import klib.data.auth.firebase.model.CreateAuthUriResponse
import klib.data.auth.firebase.model.CreateRequest
import klib.data.auth.firebase.model.DeleteRequest
import klib.data.auth.firebase.model.LookupRequest
import klib.data.auth.firebase.model.LookupResponse
import klib.data.auth.firebase.model.SendOobResponse
import klib.data.auth.firebase.model.ResetPasswordRequest
import klib.data.auth.firebase.model.ResetPasswordResponse
import klib.data.auth.firebase.model.SendOobRequest
import klib.data.auth.firebase.model.SignInResponse
import klib.data.auth.firebase.model.SignInWithCustomTokenRequest
import klib.data.auth.firebase.model.SignInWithCustomTokenResponse
import klib.data.auth.firebase.model.SignInWithIdpRequest
import klib.data.auth.firebase.model.SignInWithIdpResponse
import klib.data.auth.firebase.model.SignRequest
import klib.data.auth.firebase.model.SignUpResponse
import klib.data.auth.firebase.model.TokenRequest
import klib.data.auth.firebase.model.TokenResponse
import klib.data.auth.firebase.model.UpdateRequest
import klib.data.auth.firebase.model.UpdateResponse
import klib.data.auth.firebase.model.UserRecord
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.POST

internal interface FirebaseAuthApi {

    @POST("accounts:signInWithCustomToken")
    suspend fun signInWithCustomToken(@Body request: SignInWithCustomTokenRequest): SignInWithCustomTokenResponse

    @POST("token")
    suspend fun getToken(@Body request: TokenRequest): TokenResponse

    @POST("accounts:signUp")
    suspend fun signUp(@Body request: SignRequest): SignUpResponse

    @POST("accounts:signInWithPassword")
    suspend fun signInWithPassword(@Body request: SignRequest): SignInResponse

    @POST("accounts:signInWithIdp")
    suspend fun signInWithIdp(@Body request: SignInWithIdpRequest): SignInWithIdpResponse

    @POST("accounts:lookup")
    suspend fun lookup(@Body request: LookupRequest): LookupResponse

    @POST("accounts")
    suspend fun create(@Body request: CreateRequest): UserRecord

    @POST("accounts:update")
    suspend fun update(@Body request: UpdateRequest): UpdateResponse

    @POST("accounts:delete")
    suspend fun delete(@Body request: DeleteRequest)

    @POST("accounts:createAuthUri")
    suspend fun createAuthUri(@Body request: CreateAuthUriRequest): CreateAuthUriResponse

    @POST("accounts:sendOobCode")
    suspend fun sendOobCode(@Body request: SendOobRequest): SendOobResponse

    @POST("accounts:resetPassword")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): ResetPasswordResponse

    companion object {

        val reservedClaims: List<String> = listOf(
            "amr", "at_hash", "aud", "auth_time", "azp", "cnf", "c_hash", "exp", "iat",
            "iss", "jti", "nbf", "nonce", "sub", "firebase",
        )
    }
}
