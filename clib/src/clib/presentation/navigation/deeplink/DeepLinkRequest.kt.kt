package clib.presentation.navigation.deeplink

import androidx.navigation3.runtime.NavKey
import arrow.core.mapValuesNotNull
import io.ktor.http.Url
import io.ktor.util.toMap
import kotlinx.serialization.KSerializer

/**
 * Parse the requested Uri and store it in a easily readable format
 *
 * @param url the target deeplink uri to link to
 */
internal class DeepLinkRequest(
    val url: Url
) {

    /**
     * A list of path segments
     */
    val rawSegments: List<String> = url.rawSegments

    /**
     * A map of query name to query value
     */
    val queries: Map<String, String> = url.parameters.toMap().mapValuesNotNull { (_, value) -> value.firstOrNull() }

    // TODO add parsing for other Uri components, i.e. fragments, mimeType, action
}

public fun <T : NavKey> KSerializer<T>.deepLinkRoute(url: Url, urlPattern: Url): T? {
    val deepLinkPattern = DeepLinkPattern(this, urlPattern)

    /** Parse requested deeplink. */
    val request = DeepLinkRequest(url)

    /** Compared requested with supported deeplink to find match. */
    val match = DeepLinkMatcher(request, deepLinkPattern).match()

    /** If match is found, associate match to the correct key. */
    return match?.let {
        // Decode match result into a backstack key.
        KeyDecoder(match.args).decodeSerializableValue(match.serializer)
    }
}
