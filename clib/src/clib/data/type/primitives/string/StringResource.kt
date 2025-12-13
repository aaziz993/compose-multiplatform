@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import be.digitalia.compose.htmlconverter.HtmlStyle
import clib.presentation.locale.LocalLocalization
import klib.data.location.locale.Locale
import klib.data.location.locale.Localization
import klib.data.location.locale.current
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.replaceWithArgs

@Composable
public fun stringResource(resource: StringResource): String {
    val localization = LocalLocalization.current

    return (if (localization.locale == Locale.current) localization.getStringOrNull(resource.key) else null)
        ?: org.jetbrains.compose.resources.stringResource(resource)
}

@Composable
public fun stringResource(resource: StringResource, vararg formatArgs: Any): String {
    val localization = LocalLocalization.current

    return (if (localization.locale == Locale.current) localization.getStringOrNull(resource.key) else null)
        ?.replaceWithArgs(*formatArgs)
        ?: org.jetbrains.compose.resources.stringResource(resource, *formatArgs)
}

@Composable
public fun annotatedStringResource(
    resource: StringResource,
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = stringResource(resource).toHtmlString(
    compactMode,
    style,
    linkInteractionListener,
)

@Composable
public fun annotatedStringResource(
    resource: StringResource,
    vararg formatArgs: Any,
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = stringResource(resource, *formatArgs).toHtmlString(
    compactMode,
    style,
    linkInteractionListener,
)

public suspend fun getAnnotatedString(
    resource: StringResource,
    localization: Localization = Localization(),
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString =
    ((if (localization.locale == Locale.current) localization.getStringOrNull(resource.key) else null)
        ?: getString(resource)).toHtmlString(
        compactMode,
        style,
        linkInteractionListener,
    )

public suspend fun getAnnotatedString(
    resource: StringResource,
    vararg formatArgs: Any,
    localization: Localization = Localization(),
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString =
    ((if (localization.locale == Locale.current) localization.getStringOrNull(resource.key)?.replaceWithArgs(*formatArgs) else null)
        ?: getString(resource, *formatArgs)).toHtmlString(
        compactMode,
        style,
        linkInteractionListener,
    )
