package klib.data.type.serialization.serializers.http

import io.ktor.http.*
import klib.data.type.serialization.serializers.primitive.PrimitiveIntSerializer
import kotlinx.serialization.Serializable

public object HttpStatusCodeSerializer :
    PrimitiveIntSerializer<HttpStatusCode>(
        "io.ktor.http.HttpStatusCode",
        HttpStatusCode::value,
        HttpStatusCode::fromValue,
    )

public typealias HttpStatusCodeSerial = @Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode
