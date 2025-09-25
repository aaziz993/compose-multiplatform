package klib.data.type.serialization.properties

import klib.data.type.serialization.coders.model.TreeDecoderConfiguration
import klib.data.type.serialization.coders.model.TreeEncoderConfiguration
import klib.data.type.serialization.getAnnotation
import klib.data.type.serialization.hasAnnotation
import klib.data.type.serialization.properties.annotations.PropertiesClassDiscriminator
import klib.data.type.serialization.properties.annotations.PropertiesIgnoreUnknownKeys
import klib.data.type.tuples.to

/**
 * Configuration of the current [Properties] instance available through [Properties.configuration]
 *
 * Can be used for debug purposes and for custom Properties-specific serializers
 * via [PropertiesEncoder] and [PropertiesDecoder].
 *
 * Standalone configuration object is meaningless and can nor be used outside the
 * [Properties], neither new [Properties] instance can be created from it.
 *
 */
public class PropertiesConfiguration(
    public var encodeDefaults: Boolean = false,
    public var explicitNulls: Boolean = true,
    public var ignoreUnknownKeys: Boolean = false,
    public var emptyStringIsNull: Boolean = true,
    public var classDiscriminator: String = "type",
    public var keySeparator: String = ".",
    public var escUnicode: Boolean = true,
    public var namingStrategy: PropertiesNamingStrategy? = null,
    public var decodeEnumsCaseInsensitive: Boolean = false,
)

internal val PropertiesConfiguration.asTreeEncoderConfiguration: TreeEncoderConfiguration
    get() = TreeEncoderConfiguration(
        encodeDefaults,
        classDiscriminator = { descriptor ->
            descriptor.getAnnotation<PropertiesClassDiscriminator>()?.discriminator
                ?: classDiscriminator
        },
        transformProperty = namingStrategy?.let { namingStrategy ->
            { descriptor, index, value ->
                if (value == null && !explicitNulls) null
                else namingStrategy.serialNameForProperties(descriptor, index) to value
            }
        } ?: { descriptor, index, value -> descriptor.getElementName(index) to value },
    )

internal val PropertiesConfiguration.asTreeDecoderConfiguration: TreeDecoderConfiguration
    get() = TreeDecoderConfiguration(
        decodeEnumsCaseInsensitive,
        { descriptor ->
            descriptor.hasAnnotation<PropertiesIgnoreUnknownKeys>() || ignoreUnknownKeys
        },
        classDiscriminator = { descriptor ->
            descriptor.getAnnotation<PropertiesClassDiscriminator>()?.discriminator
                ?: classDiscriminator
        },
        transformProperty = namingStrategy?.let { namingStrategy ->
            { descriptor, index, map ->
                namingStrategy.serialNameForProperties(descriptor, index) to map::get
            }
        } ?: { descriptor, index, map ->
            descriptor.getElementName(index) to map::get
        },
    ).apply {
        if (emptyStringIsNull)
            decodeNotNullMark = { value -> value?.toString()?.isNotEmpty() == true }
    }
