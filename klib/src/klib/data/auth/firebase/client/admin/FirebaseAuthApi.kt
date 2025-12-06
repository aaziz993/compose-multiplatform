package klib.data.auth.firebase.client.admin

import klib.data.auth.firebase.client.admin.model.CreateAuthUriRequest
import klib.data.auth.firebase.client.admin.model.CreateAuthUriResponse
import klib.data.auth.firebase.client.admin.model.CreateRequest
import klib.data.auth.firebase.client.admin.model.DeleteRequest
import klib.data.auth.firebase.client.admin.model.LookupRequest
import klib.data.auth.firebase.client.admin.model.LookupResponse
import klib.data.auth.firebase.client.admin.model.SendOobResponse
import klib.data.auth.firebase.client.admin.model.ResetPasswordRequest
import klib.data.auth.firebase.client.admin.model.ResetPasswordResponse
import klib.data.auth.firebase.client.admin.model.SendOobRequest
import klib.data.auth.firebase.client.admin.model.SignInResponse
import klib.data.auth.firebase.client.admin.model.SignInWithCustomTokenRequest
import klib.data.auth.firebase.client.admin.model.SignInWithCustomTokenResponse
import klib.data.auth.firebase.client.admin.model.SignInWithIdpRequest
import klib.data.auth.firebase.client.admin.model.SignInWithIdpResponse
import klib.data.auth.firebase.client.admin.model.SignRequest
import klib.data.auth.firebase.client.admin.model.SignUpResponse
import klib.data.auth.firebase.client.admin.model.TokenRequest
import klib.data.auth.firebase.client.admin.model.TokenResponse
import klib.data.auth.firebase.client.admin.model.UpdateRequest
import klib.data.auth.firebase.client.admin.model.UpdateResponse
import klib.data.auth.firebase.client.admin.model.UserRecord
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
