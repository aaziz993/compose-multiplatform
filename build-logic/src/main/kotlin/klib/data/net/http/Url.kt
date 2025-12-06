package klib.data.net.http

import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.http.encodedPath
import io.ktor.http.parseUrl
import io.ktor.http.path
import io.ktor.util.toMap
import klib.data.net.http.annotation.UrlParam
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.addSuffixIfNotEmpty
import klib.data.type.primitives.string.surround
import klib.data.type.serialization.coders.tree.deserialize
import klib.data.type.serialization.getElementDescriptor
import klib.data.type.serialization.hasElementAnnotation
import klib.data.type.serialization.isElementOptional
import kotlin.IllegalArgumentException
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.elementNames
import kotlinx.serialization.serializer

public val Regex.Companion.HTTP_PATTERN: String
    get() = "^https?://.*"

public val Regex.Companion.HTTP: Regex
    get() = HTTP_PATTERN.toRegex()

public val Regex.Companion.GITHUB_HTTP_PATTERN: String
    get() = """https?://(www\.)?github\.com/.*"""

public val Regex.Companion.GITHUB_HTTP: Regex
    get() = GITHUB_HTTP_PATTERN.toRegex()

/**
 * Help differentiate if a path segment is an argument or a static value.
 */
public val Regex.Companion.PATH_PARAM_REGEX: String
    get() = "\\{(.+?)\\}"

public val Regex.Companion.PATH_PARAM: Regex
    get() = PATH_PARAM_REGEX.toRegex()

public fun String.isHttpUrl(): Boolean = matches(Regex.HTTP)

public fun String.isGithubUrl(): Boolean = matches(Regex.GITHUB_HTTP)

public fun Url.Companion.parseOrNull(urlString: String): Url? = parseUrl(urlString)

public fun Url.Companion.parse(urlString: String): Url =
    parseOrNull(urlString) ?: throw IllegalArgumentException("Invalid URL: $this")

public fun String.toUrlOrNull(): Url? = Url.parseOrNull(this)

public fun String.toUrl(): Url = Url.parse(this)

public fun String.isValidHttpUrl(): Boolean =
    toUrlOrNull()?.let { it.protocol == URLProtocol.HTTP || it.protocol == URLProtocol.HTTPS } == true

public fun String.encodeHttpUrl(): String = URLBuilder().apply { encodedPath = this@encodeHttpUrl }.buildString()

public fun String.decodeHttpUrl(): String = URLBuilder().apply { path(this@decodeHttpUrl) }.buildString()

/**
 * Match a [Url] to a [urlPattern].
 *
 * @param urlPattern The pattern to match against.
 * @returns a [Map] of parameters if this matches the pattern, returns null otherwise.
 */
public fun Url.matchParameters(urlPattern: Url): Map<String, Any>? {
    if (
        protocol.name != urlPattern.protocol.name ||
        rawSegments.size != urlPattern.rawSegments.size
    ) return null

    // exact match (url does not contain any arguments).
    if (this == urlPattern) return mapOf()

    val args = mutableMapOf<String, Any>()

    // match the path
    rawSegments
        .asSequence()
        // zip to compare the two objects side by side, order matters here so we
        // need to make sure the compared segments are at the same position within the url
        .zip(urlPattern.rawSegments.asSequence())
        .forEach { (requestedSegment, candidateSegment) ->
            // if the potential match expects a path arg for this segment, try to parse the.
            // requested segment into the expected type.
            Regex.PATH_PARAM.find(candidateSegment)?.let { result ->
                val segmentParamName = result.groups[1]!!.value
                args[segmentParamName] = requestedSegment
            } ?: run {
                // if it's path arg is not the expected type, its not a match
                if (requestedSegment != candidateSegment) return null
            }
        }

    val urlPatternParameterNames = urlPattern.parameters.names().map { name ->
        name.removeSurrounding("{", "}")
    }

    args += parameters.toMap().filter { (paramName, _) -> paramName in urlPatternParameterNames }

    // provide the map of arg names to arg values.
    return args
}

/**
 * Generates a url pattern [Url] for this serializer type.
 *
 * Required, non-null, non-list properties are treated as path parameters,
 * while optional, nullable, or list properties are treated as query parameters.
 *
 * Example:
 * ```
 * data class User(val id: Int, val name: String?)
 * User.serializer().urlPattern() // "/User/{id}?{name}"
 * ```
 *
 * @return [Url] representing the url pattern for this type.
 */
public fun <T : Any> KSerializer<T>.urlPattern(basePath: String = ""): Url {
    val (pathParams, queryParams) = descriptor.elementNames.partition { name ->
        val elementDescriptor = descriptor.getElementDescriptor(name)!!
        descriptor.hasElementAnnotation<UrlParam>(name) == false &&
            descriptor.isElementOptional(name) == false &&
            !elementDescriptor.isNullable &&
            elementDescriptor.kind != StructureKind.LIST
    }

    return Url(
        "${basePath.addSuffixIfNotEmpty("/")}${descriptor.serialName}${
            pathParams.joinToString("/") { param -> param.surround("{", "}") }
                .addPrefixIfNotEmpty("/")
        }${
            queryParams.joinToString("&") { param -> param.surround("{", "}") }
                .addPrefixIfNotEmpty("?")
        }",
    )
}

/**
 * Generates a url pattern [Url] for the specified type [T].
 *
 * This is a reified inline version of [KSerializer.urlPattern].
 *
 * Example:
 * ```
 * urlPattern<User>() // "/User/{id}?{name}"
 * ```
 *
 * @return [Url] representing the url pattern for type [T].
 */
public inline fun <reified T : Any> urlPattern(basePath: String = ""): Url =
    T::class.serializer().urlPattern(basePath)

/**
 * Attempts to decode the current [Url] into a strongly-typed route object.
 *
 * This function:
 * 1. Matches the current url against the provided [urlPattern].
 * 2. Extracts all path and query arguments defined in the pattern (e.g. `/user/{id}` or `?name={name}`).
 * 3. Deserializes the extracted argument map into an instance of type [T] using the given [kSerializer].
 *
 * @param kSerializer The serializer used to construct an instance of [T] from the extracted arguments.
 * @param urlPattern A pattern describing how to interpret the current url.
 * @return An instance of [T] if the url matches the pattern, or `null` if it does not.
 *
 * @see matchParameters for the argument-extraction logic.
 */
public fun <T : Any> Url.toRoute(kSerializer: KSerializer<T>, urlPattern: Url): T? =
    matchParameters(urlPattern)?.let { result ->
        kSerializer.deserialize(
            result.mapValues { (paramName, value) ->
                if (kSerializer.descriptor.getElementDescriptor(paramName)!!.kind == StructureKind.LIST) {
                    if (value !is List<*>) return@mapValues listOf(value)
                }
                else {
                    if (value is List<*>) return@mapValues value.single()
                }

                value
            },
        )
    }

/**
 * Inline + reified convenience overload of [toRoute] that automatically picks the correct
 * serializer for the target type [T].
 *
 * This is the idiomatic entry point for callers:
 *
 * ```kotlin
 * val route: UserRoute? = url.toRoute(pattern)
 * ```
 *
 * It behaves exactly like [toRoute] with an explicitly provided serializer:
 * - Matches the url against [urlPattern].
 * - Extracts arguments from path/query placeholders.
 * - Deserializes them into an instance of [T].
 *
 * @param urlPattern A pattern describing the url structure for type [T].
 * @return An instance of [T], or `null` if the url does not match the pattern.
 */
public inline fun <reified T : Any> Url.toRoute(urlPattern: Url): T? =
    toRoute(T::class.serializer(), urlPattern)

