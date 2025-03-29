package gradle.serialization.serializer

import kotlin.reflect.KClass
import kotlinx.serialization.json.JsonTransformingSerializer

public abstract class JsonBaseValueTransformingContentPolymorphicSerializer<T : Any>(
    baseClass: KClass<T>,
    classDiscriminator: String = "type",
) : JsonTransformingSerializer<T>(JsonContentPolymorphicSerializer(baseClass, classDiscriminator))
