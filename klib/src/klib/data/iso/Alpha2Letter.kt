package klib.data.iso

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
public value class Alpha2Letter(private val value: String) {

    init {
        require(isValidFormat(value)) {
            "Invalid ISO alpha-2 code format: $value"
        }
    }

    override fun toString(): String = value

    public companion object Companion {

        public fun parse(value: String): Alpha2Letter =
            parseOrNull(value) ?: error("Invalid ISO 3166-1 alpha-2 country code format: $value")

        public fun parseOrNull(value: String): Alpha2Letter? =
            value.takeIf(this::isValidFormat)?.let { Alpha2Letter(value = it.uppercase()) }

        private fun isValidFormat(value: String) = value.length == 2 && value.isLatinLetter()
    }
}
