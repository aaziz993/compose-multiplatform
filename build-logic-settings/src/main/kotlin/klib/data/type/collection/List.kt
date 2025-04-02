package klib.data.type.collection

import klib.data.type.serialization.serializer.AnySerializer
import klib.data.type.serialization.serializer.OptionalAnySerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer

public object SerializableAnyList : KSerializer<List<Any>> by ListSerializer(AnySerializer)

public object SerializableOptionalAnyList : KSerializer<List<Any?>> by ListSerializer(OptionalAnySerializer)
