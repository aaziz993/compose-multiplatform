package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import be.digitalia.compose.htmlconverter.HtmlStyle
import clib.presentation.locale.LocalLocalization
import klib.data.location.locale.Locale
import klib.data.location.locale.Localization
import klib.data.location.locale.current
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.getStringArray
import org.jetbrains.compose.resources.getSystemResourceEnvironment

@Composable
public fun stringArrayResource(resource: StringArrayResource): List<String> {
    val localization = LocalLocalization.current

    return (if (localization.locale == Locale.current) localization.getStringArrayOrNull(resource.key) else null)
        ?: org.jetbrains.compose.resources.stringArrayResource(resource)
}

@Composable
public fun annotatedStringArrayResource(
    resource: StringArrayResource,
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): List<AnnotatedString> = stringArrayResource(resource).map { value ->
    value.toHtmlString(
        compactMode,
        style,
        linkInteractionListener,
    )
}

public suspend fun getAnnotatedStringArray(
    resource: StringArrayResource,
    environment: ResourceEnvironment = getSystemResourceEnvironment(),
    localization: Localization = Localization(),
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): List<AnnotatedString> =
    ((if (localization.locale == Locale.current) localization.getStringArrayOrNull(resource.key) else null)
        ?: getStringArray(environment, resource)).map { value ->
        value.toHtmlString(
            compactMode,
            style,
            linkInteractionListener,
        )
    }
