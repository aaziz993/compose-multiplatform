package data.type.serialization.serializer.typography

import androidx.compose.material3.Typography
import klib.data.type.serialization.serializers.primitive.PrimitiveStringSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor

public object TypographyJsonSerializer : PrimitiveStringSerializer<Typography>(Typography::class.simpleName!!, { "" }, { Typography() }) {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Typography", PrimitiveKind.STRING)
}

public typealias TypographyJson = @Serializable(with = TypographyJsonSerializer::class) Typography
