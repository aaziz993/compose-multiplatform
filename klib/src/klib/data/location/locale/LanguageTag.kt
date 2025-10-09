package klib.data.location.locale

import klib.data.location.country.Country
import klib.data.type.collections.iterator.nextOrNull
import klib.data.type.primitives.string.isDigit
import klib.data.type.primitives.string.isLetter
import klib.data.type.primitives.string.isLetterOrDigit
import klib.data.type.primitives.string.uppercaseFirstChar
import kotlin.collections.iterator

/**
 * A BCP 47 language tag, for example `en`, `en-US` or `sl-IT-nedis`.
 *
 * References:
 * - [https://tools.ietf.org/html/bcp47]
 */
public data class LanguageTag(
    public val extensions: List<String>,
    public val extlangs: List<String>,
    public val language: String?,
    public val privateUse: String?,
    public val region: String?,
    public val script: String?,
    public val variants: List<String>,
) {

    public fun forPrivateUse(privateUse: String): LanguageTag = copy(language = null, privateUse = privateUse)

    public fun forLanguage(language: String): LanguageTag = copy(language = language, privateUse = null)

    public fun country(): Country = Country.forCode(region ?: "")

    override fun toString(): String = buildString {
        if (language != null) {
            append(language)

            extlangs.forEach { extLang ->
                append(SEPARATOR)
                append(extLang)
            }

            if (script != null) {
                append(SEPARATOR)
                append(script)
            }

            if (region != null) {
                append(SEPARATOR)
                append(region)
            }

            variants.forEach { variant ->
                append(SEPARATOR)
                append(variant)
            }

            extensions.forEach { extension ->
                append(SEPARATOR)
                append(extension)
            }
        }

        if (privateUse != null) {
            if (isNotEmpty())
                append(SEPARATOR)

            append(privateUse)
        }
    }

    public companion object {

        public const val PRIVATE_USE_PREFIX: Char = 'x'
        public const val SEPARATOR: Char = '-'
        public const val UNDETERMINED_PREFIX: String = "und"

        // https://www.iana.org/assignments/language-subtag-registry/language-subtag-registry
        // grandfathered = irregular           ; non-redundant tags registered
        //               / regular             ; during the RFC 3066 era
        //
        // irregular     = "en-GB-oed"         ; irregular tags do not match
        //               / "i-ami"             ; the 'langtag' production and
        //               / "i-bnn"             ; would not otherwise be
        //               / "i-default"         ; considered 'well-formed'
        //               / "i-enochian"        ; These tags are all valid,
        //               / "i-hak"             ; but most are deprecated
        //               / "i-klingon"         ; in favor of more modern
        //               / "i-lux"             ; subtags or subtag
        //               / "i-mingo"           ; combination
        //               / "i-navajo"
        //               / "i-pwn"
        //               / "i-tao"
        //               / "i-tay"
        //               / "i-tsu"
        //               / "sgn-BE-FR"
        //               / "sgn-BE-NL"
        //               / "sgn-CH-DE"
        //
        // regular       = "art-lojban"        ; these tags match the 'langtag'
        //               / "cel-gaulish"       ; production, but their subtags
        //               / "no-bok"            ; are not extended language
        //               / "no-nyn"            ; or variant subtags: their meaning
        //               / "zh-guoyu"          ; is defined by their registration
        //               / "zh-hakka"          ; and all of these are deprecated
        //               / "zh-min"            ; in favor of a more modern
        //               / "zh-min-nan"        ; subtag or sequence of subtags
        //               / "zh-xiang"
        private val grandfathered: Map<String, String> = hashMapOf(
            "art-lojban" to "jbo",
            "cel-gaulish" to "xtg-x-cel-gaulish",
            "en-gb-oed" to "en-GB-oxendict",
            "i-ami" to "ami",
            "i-bnn" to "bnn",
            "i-default" to "en-x-i-default",
            "i-enochian" to "und-x-i-enochian",
            "i-hak" to "hak",
            "i-klingon" to "tlh",
            "i-lux" to "lb",
            "i-mingo" to "see-x-i-mingo",
            "i-navajo" to "nv",
            "i-pwn" to "pwn",
            "i-tao" to "tao",
            "i-tay" to "tay",
            "i-tsu" to "tsu",
            "no-bok" to "nb",
            "no-nyn" to "nn",
            "sgn-be-fr" to "sfb",
            "sgn-be-nl" to "vgt",
            "sgn-ch-de" to "sgg",
            "zh-guoyu" to "cmn",
            "zh-hakka" to "hak",
            "zh-min" to "nan-x-zh-min",
            "zh-min-nan" to "nan",
            "zh-xiang" to "hsn",
        )

        public fun canonicalizeExtension(extension: String?): String? =
            extension?.ifEmpty { null }?.lowercase()

        public fun canonicalizeExtensionSingleton(singleton: String?): String? =
            singleton?.ifEmpty { null }?.lowercase()

        public fun canonicalizeExtensionSubtag(subtag: String?): String? =
            subtag?.ifEmpty { null }?.lowercase()

        public fun canonicalizeExtensions(extensions: List<String>): List<String> =
            extensions.ifEmpty { null }?.mapNotNull(::canonicalizeExtension)?.sorted().orEmpty()

        public fun canonicalizeExtLang(extLang: String?): String? =
            extLang?.ifEmpty { null }?.lowercase()

        public fun canonicalizeExtlangs(extlangs: List<String>): List<String> =
            extlangs.ifEmpty { null }?.mapNotNull(::canonicalizeExtLang).orEmpty()

        public fun canonicalizeLanguage(language: String?): String? =
            language?.ifEmpty { null }?.lowercase()

        public fun canonicalizePrivateUse(privateUse: String?): String? =
            privateUse?.ifEmpty { null }?.lowercase()

        public fun canonicalizePrivateUsePrefix(prefix: String?): String? =
            prefix?.ifEmpty { null }?.lowercase()

        public fun canonicalizePrivateUseSubtag(subtag: String?): String? =
            subtag?.ifEmpty { null }?.lowercase()

        public fun canonicalizeRegion(region: String?): String? =
            region?.ifEmpty { null }?.uppercase()

        public fun canonicalizeScript(script: String?): String? =
            script?.ifEmpty { null }?.uppercaseFirstChar()

        public fun canonicalizeVariant(variant: String?): String? =
            variant?.ifEmpty { null }

        public fun canonicalizeVariants(variants: List<String>): List<String> =
            variants.ifEmpty { null }?.mapNotNull(::canonicalizeVariant).orEmpty()

        public fun forLanguage(
            language: String,
            script: String? = null,
            region: String? = null,
            variants: List<String> = emptyList(),
            extlangs: List<String> = emptyList(),
            extensions: List<String> = emptyList(),
            privateUse: String? = null,
        ): LanguageTag {
            val canonicalLanguage = canonicalizeLanguage(language)
            val canonicalScript = canonicalizeScript(script)
            val canonicalRegion = canonicalizeRegion(region)
            val canonicalVariants = canonicalizeVariants(variants)
            val canonicalExtlangs = canonicalizeExtlangs(extlangs)
            val canonicalExtensions = canonicalizeExtensions(extensions)
            val canonicalPrivateUse = canonicalizePrivateUse(privateUse)

            require(canonicalLanguage != null && isLanguage(canonicalLanguage)) { "Invalid language: $language" }
            require(canonicalScript == null || isScript(canonicalScript)) { "Invalid script: $script" }
            require(canonicalRegion == null || isRegion(canonicalRegion)) { "Invalid region: $region" }

            for (variant in canonicalVariants)
                require(isVariant(variant)) { "Invalid variant: $variant" }
            for (extLang in canonicalExtlangs)
                require(isExtLang(extLang)) { "Invalid extLang: $extLang" }
            for (extension in canonicalExtensions)
                require(isExtension(extension)) { "Invalid extension: $extension" }

            require(canonicalPrivateUse == null || isPrivateUse(canonicalPrivateUse)) { "Invalid privateUse: $privateUse" }

            return LanguageTag(
                extensions = canonicalExtensions,
                extlangs = canonicalExtlangs,
                language = canonicalLanguage,
                privateUse = canonicalPrivateUse,
                region = canonicalRegion,
                script = canonicalScript,
                variants = canonicalVariants,
            )
        }

        public fun forLanguageOrNull(
            language: String,
            script: String? = null,
            region: String? = null,
            variants: List<String> = emptyList(),
            extlangs: List<String> = emptyList(),
            extensions: List<String> = emptyList(),
            privateUse: String? = null,
        ): LanguageTag? {
            val canonicalLanguage = canonicalizeLanguage(language)
            val canonicalScript = canonicalizeScript(script)
            val canonicalRegion = canonicalizeRegion(region)
            val canonicalVariants = canonicalizeVariants(variants)
            val canonicalExtlangs = canonicalizeExtlangs(extlangs)
            val canonicalExtensions = canonicalizeExtensions(extensions)
            val canonicalPrivateUse = canonicalizePrivateUse(privateUse)

            require(canonicalLanguage != null && isLanguage(canonicalLanguage)) { return null }
            require(canonicalScript == null || isScript(canonicalScript)) { return null }
            require(canonicalRegion == null || isRegion(canonicalRegion)) { return null }

            for (variant in canonicalVariants)
                require(isVariant(variant)) { return null }
            for (extLang in canonicalExtlangs)
                require(isExtLang(extLang)) { return null }
            for (extension in canonicalExtensions)
                require(isExtension(extension)) { return null }

            require(canonicalPrivateUse == null || isPrivateUse(canonicalPrivateUse)) { return null }

            return LanguageTag(
                extensions = canonicalExtensions,
                extlangs = canonicalExtlangs,
                language = canonicalLanguage,
                privateUse = canonicalPrivateUse,
                region = canonicalRegion,
                script = canonicalScript,
                variants = canonicalVariants,
            )
        }

        public fun forPrivateUse(
            privateUse: String,
        ): LanguageTag {
            val canonicalPrivateUse = canonicalizePrivateUse(privateUse)
            require(canonicalPrivateUse != null && isPrivateUse(privateUse)) { "Invalid privateUse: $privateUse" }

            return LanguageTag(
                extensions = emptyList(),
                extlangs = emptyList(),
                language = null,
                privateUse = canonicalPrivateUse,
                region = null,
                script = null,
                variants = emptyList(),
            )
        }

        public fun forPrivateUseOrNull(
            privateUse: String,
        ): LanguageTag? {
            val canonicalPrivateUse = canonicalizePrivateUse(privateUse)
            require(canonicalPrivateUse != null && isPrivateUse(privateUse)) { return null }

            return LanguageTag(
                extensions = emptyList(),
                extlangs = emptyList(),
                language = null,
                privateUse = canonicalPrivateUse,
                region = null,
                script = null,
                variants = emptyList(),
            )
        }

        // extension     = singleton 1*("-" (2*8alphanum))
        public fun isExtension(extension: String): Boolean {
            val tokens = extension.splitToSequence(SEPARATOR).iterator()

            if (!isExtensionSingleton(tokens.next()))
                return false

            if (!tokens.hasNext())
                return false

            for (token in tokens)
                if (!isExtensionSubtag(token))
                    return false

            return true
        }

        //                                     ; Single alphanumerics
        //                                     ; "x" reserved for private use
        // singleton     = DIGIT               ; 0 - 9
        //               / %x41-57             ; A - W
        //               / %x59-5A             ; Y - Z
        //               / %x61-77             ; a - w
        //               / %x79-7A             ; y - z
        public fun isExtensionSingleton(singleton: Char): Boolean =
            singleton.isLetterOrDigit() && !isPrivateUsePrefix(singleton)

        public fun isExtensionSingleton(singleton: String): Boolean =
            singleton.length == 1 && isExtensionSingleton(singleton[0])

        // extension     = singleton 1*("-" (2*8alphanum))
        public fun isExtensionSubtag(extension: String): Boolean =
            extension.length in 2..8 && extension.isLetterOrDigit()

        // extLang       = 3ALPHA              ; selected ISO 639 codes
        //                 *2("-" 3ALPHA)      ; permanently reserved
        public fun isExtLang(extLang: String): Boolean =
            extLang.length == 3 && extLang.isLetter()

        // language      = 2*3ALPHA            ; shortest ISO 639 code
        //                 ["-" extLang]       ; sometimes followed by
        //                                     ; extended language subtags
        //               / 4ALPHA              ; or reserved for future use
        //               / 5*8ALPHA            ; or registered language subtag
        public fun isLanguage(language: String): Boolean =
            language.length in 2..8 && language.isLetter()

        // privateUse    = "x" 1*("-" (1*8alphanum))
        public fun isPrivateUse(privateUse: String): Boolean {
            val tokens = privateUse.splitToSequence(SEPARATOR).iterator()

            if (!isPrivateUsePrefix(tokens.next()))
                return false

            if (!tokens.hasNext())
                return false

            for (token in tokens)
                if (!isPrivateUseSubtag(token))
                    return false

            return true
        }

        // privateUse    = "x" 1*("-" (1*8alphanum))
        public fun isPrivateUsePrefix(prefix: Char): Boolean =
            prefix.equals(PRIVATE_USE_PREFIX, ignoreCase = true)

        public fun isPrivateUsePrefix(prefix: String): Boolean =
            prefix.length == 1 && isPrivateUsePrefix(prefix[0])

        // privateUse    = "x" 1*("-" (1*8alphanum))
        public fun isPrivateUseSubtag(privateUse: String): Boolean =
            privateUse.length in 1..8 && privateUse.isLetterOrDigit()

        // region        = 2ALPHA              ; ISO 3166-1 code
        //               / 3DIGIT              ; UN M.49 code
        public fun isRegion(region: String): Boolean =
            when (region.length) {
                2 -> region.isLetter()
                3 -> region.isDigit()
                else -> false
            }

        // script        = 4ALPHA              ; ISO 15924 code
        public fun isScript(script: String): Boolean =
            script.length == 4 && script.isLetter()

        // variant       = 5*8alphanum         ; registered variants
        //               / (DIGIT 3alphanum)
        public fun isVariant(variant: String): Boolean =
            when (variant.length) {
                4 -> variant[0].isDigit() && variant[1].isLetterOrDigit() && variant[2].isLetterOrDigit() && variant[3].isLetterOrDigit()
                in 5..8 -> variant.isLetterOrDigit()
                else -> false
            }

        public fun parse(string: String): LanguageTag =
            parseOrNull(string) ?: error("Ill-formed BCP 47 language tag: $string")

        @Suppress("NAME_SHADOWING")
        public fun parseOrNull(string: String): LanguageTag? {
            val string = grandfathered[string.lowercase()] ?: string

            val tokens = string.splitToSequence(SEPARATOR).iterator()

            var extlangs: MutableList<String>? = null
            var extensions: MutableList<String>? = null
            var language: String? = null
            var privateUse: String? = null
            var region: String? = null
            var script: String? = null
            var variants: MutableList<String>? = null

            var token: String?
            token = tokens.next()
            if (isLanguage(token)) {
                language = canonicalizeLanguage(token)
                token = tokens.nextOrNull()

                if (token != null && isExtLang(token)) {
                    extlangs = mutableListOf()

                    do {
                        extlangs.add(canonicalizeExtLang(token)!!)

                        token = tokens.nextOrNull()
                    } while (token != null && extlangs.size < 3 && isExtLang(token))
                }

                if (token != null && isScript(token)) {
                    script = canonicalizeScript(token)
                    token = tokens.nextOrNull()
                }

                if (token != null && isRegion(token)) {
                    region = canonicalizeRegion(token)
                    token = tokens.nextOrNull()
                }

                if (token != null && isVariant(token)) {
                    variants = mutableListOf()

                    do {
                        variants.add(canonicalizeVariant(token)!!)

                        token = tokens.nextOrNull()
                    } while (token != null && isVariant(token))
                }

                if (token != null && isExtensionSingleton(token)) {
                    extensions = mutableListOf()

                    do {
                        val extensionBuilder = StringBuilder(canonicalizeExtensionSingleton(token)!!)

                        token = tokens.nextOrNull()

                        if (token != null && isExtensionSubtag(token)) {
                            do {
                                extensionBuilder.append(SEPARATOR)
                                extensionBuilder.append(canonicalizeExtensionSubtag(token))

                                token = tokens.nextOrNull()
                            } while (token != null && isExtensionSubtag(token))
                        }
                        else
                            return null // incomplete extension

                        extensions.add(extensionBuilder.toString())
                    } while (token != null && isExtensionSingleton(token))
                }
            }

            if (token != null && isPrivateUsePrefix(token)) {
                val privateuseBuilder = StringBuilder(canonicalizePrivateUsePrefix(token)!!)

                token = tokens.nextOrNull()

                if (token != null && isPrivateUseSubtag(token)) {
                    do {
                        privateuseBuilder.append(SEPARATOR)
                        privateuseBuilder.append(canonicalizePrivateUseSubtag(token))

                        token = tokens.nextOrNull()
                    } while (token != null && isPrivateUseSubtag(token))
                }
                else
                    return null // incomplete privateUse

                privateUse = privateuseBuilder.toString()
            }

            if (token != null)
                return null

            return LanguageTag(
                extensions = extensions.orEmpty(),
                extlangs = extlangs.orEmpty(),
                language = language,
                privateUse = privateUse,
                region = region,
                script = script,
                variants = variants.orEmpty(),
            )
        }
    }
}

public fun String.toLanguageTagOrNull(): LanguageTag? = LanguageTag.parseOrNull(this)

public fun String.toLanguageTag(): LanguageTag = LanguageTag.parse(this)
