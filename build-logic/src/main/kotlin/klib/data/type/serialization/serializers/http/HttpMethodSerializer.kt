package klib.data.type.serialization.serializers.http

import io.ktor.http.*
import klib.data.type.serialization.serializers.primitive.StringSerializer
import kotlinx.serialization.Serializable

public object HttpMethodSerializer :
    StringSerializer<HttpMethod>(
        "io.ktor.http.HttpMethod",
        HttpMethod::value,
        HttpMethod::parse,
    )

public typealias HttpMethodSerial = @Serializable(with = HttpMethodSerializer::class) HttpMethod
