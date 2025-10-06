package klib.data.location.country

import klib.data.type.primitives.string.isLatinLetter
import kotlin.jvm.JvmInline
import kotlinx.serialization.*

/**
 * An ISO 3166-1 alpha-2 country code, for example `US` or `DE`.
 *
 * References:
 * - [https://www.iso.org/iso-3166-country-codes.html]
 * - [https://www.iso.org/obp/ui/]
 */
@JvmInline
@Serializable
public value class Alpha3Letter(public val value: String) {

    init {
        require(isValidFormat(value)) {
            "Invalid ISO alpha-3 code format: $value"
        }
    }

    override fun toString(): String = value

    public companion object Companion {

        public fun parse(value: String): Alpha3Letter =
            parseOrNull(value) ?: error("Invalid ISO 3166-1 alpha-3 country code format: $value")

        public fun parseOrNull(value: String): Alpha3Letter? =
            value.takeIf(this::isValidFormat)?.let { Alpha3Letter(it.uppercase()) }

        private fun isValidFormat(value: String) = value.length == 3 && value.isLatinLetter()
    }
}
