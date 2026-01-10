package klib.data.type.serialization.serializers.http

import io.ktor.http.HttpStatusCode
import klib.data.type.serialization.serializers.primitive.IntSerializer
import kotlinx.serialization.Serializable

public object HttpStatusCodeSerializer :
    IntSerializer<HttpStatusCode>(
        "io.ktor.http.HttpStatusCode",
        HttpStatusCode::value,
        HttpStatusCode::fromValue,
    )

public typealias HttpStatusCodeSerial = @Serializable(with = HttpStatusCodeSerializer::class) HttpStatusCode
