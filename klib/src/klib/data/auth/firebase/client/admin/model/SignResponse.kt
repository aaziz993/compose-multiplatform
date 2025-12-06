package klib.data.auth.firebase.client.admin.model

import klib.data.auth.client.bearer.model.BearerToken

public interface SignResponse : BearerToken {

    public val email: String

    public val expiresIn: String
    public val localId: String
}
