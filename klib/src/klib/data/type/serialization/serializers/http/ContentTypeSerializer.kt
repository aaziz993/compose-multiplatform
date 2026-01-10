package klib.data.type.serialization.serializers.http

import io.ktor.http.ContentType
import klib.data.type.serialization.serializers.primitive.StringSerializer
import kotlinx.serialization.Serializable

public object ContentTypeSerializer :
    StringSerializer<ContentType>(
        "io.ktor.http.ContentType",
        ContentType::toString,
        ContentType::parse,
    )

public typealias ContentTypeSerial = @Serializable(with = ContentTypeSerializer::class) ContentType
