package klib.data.type.serialization.properties

import net.pearx.kasechange.toCamelCase
import net.pearx.kasechange.toKebabCase
import net.pearx.kasechange.toPascalCase
import net.pearx.kasechange.toSnakeCase
import klib.data.type.serialization.properties.PropertiesNamingStrategy.Builtins.PascalCase
import kotlinx.serialization.descriptors.SerialDescriptor

public fun interface PropertiesNamingStrategy {
    public fun serialNameForProperties(descriptor: SerialDescriptor, index: Int): String

    public companion object Builtins {
        /**
         * A [PropertiesNamingStrategy] that converts property names to snake_case (lowercase words separated by underscores).
         */
        public val SnakeCase: PropertiesNamingStrategy = object : PropertiesNamingStrategy {
            override fun serialNameForProperties(descriptor: SerialDescriptor, index: Int): String =
                descriptor.getElementName(index).toSnakeCase()
        }

        /**
         * A [PropertiesNamingStrategy] that converts property names to kebab-case (lowercase words separated by dashes).
         */
        public val KebabCase: PropertiesNamingStrategy = object : PropertiesNamingStrategy {
            override fun serialNameForProperties(descriptor: SerialDescriptor, index: Int): String =
                descriptor.getElementName(index).toKebabCase()
        }

        /**
         * A [PropertiesNamingStrategy] that converts property names to PascalCase (capitalized words concatenated together).
         */
        public val PascalCase: PropertiesNamingStrategy = object : PropertiesNamingStrategy {
            override fun serialNameForProperties(descriptor: SerialDescriptor, index: Int): String =
                descriptor.getElementName(index).toPascalCase()
        }

        /**
         * A [PropertiesNamingStrategy] that converts property names to camelCase (like [PascalCase] but with the first letter lowercase).
         */
        public val CamelCase: PropertiesNamingStrategy = object : PropertiesNamingStrategy {
            override fun serialNameForProperties(descriptor: SerialDescriptor, index: Int): String =
                descriptor.getElementName(index).toCamelCase()
        }
    }
}
