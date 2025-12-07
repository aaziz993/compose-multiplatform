package klib.data.auth.firebase.model

import klib.data.auth.client.model.BearerToken

public interface SignResponse : BearerToken {

    public val email: String

    public val expiresIn: String
    public val localId: String
}
