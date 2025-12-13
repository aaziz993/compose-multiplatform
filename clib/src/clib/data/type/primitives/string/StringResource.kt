package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import be.digitalia.compose.htmlconverter.HtmlStyle
import clib.presentation.locale.LocalLocalization
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

@Composable
public fun stringResource(resource: StringResource): String =
    LocalLocalization.current.getStringOrNull(resource.key)
        ?: org.jetbrains.compose.resources.stringResource(resource)

@Composable
public fun stringResource(resource: StringResource, vararg formatArgs: Any): String =
    LocalLocalization.current.getStringOrNull(resource.key)
        ?: org.jetbrains.compose.resources.stringResource(resource, *formatArgs)

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
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = getString(resource).toHtmlString(
    compactMode,
    style,
    linkInteractionListener,
)

public suspend fun getAnnotatedString(
    resource: StringResource,
    vararg formatArgs: Any,
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = getString(resource, *formatArgs).toHtmlString(
    compactMode,
    style,
    linkInteractionListener,
)
