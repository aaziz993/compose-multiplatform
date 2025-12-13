@file:OptIn(ExperimentalResourceApi::class)
@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import be.digitalia.compose.htmlconverter.HtmlStyle
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import clib.presentation.locale.LocalLocalization
import klib.data.location.locale.Localization
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.case.toSnakeCase
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.StringResource

@Suppress("ComposeUnstableReceiver")
@Composable
public fun String.asStringResource(
    resources: Map<String, StringResource>,
    localization: Localization = LocalLocalization.current,
    defaultValue: () -> String = { this }
): String =
    resources[toSnakeCase()]?.let { stringResource -> stringResource(stringResource) } ?: defaultValue()

@Suppress("ComposeUnstableReceiver")
@Composable
public fun <T> T.asStringResource(
    resources: Map<String, StringResource>,
    localization: Localization = LocalLocalization.current,
): String = toString().let { string ->
    val base = string
        .substringBefore('(')
        .substringAfterLast('.')
        .substringBefore('$')
        .substringBefore('@')
        .removeSuffix("Kt")
    "${base.asStringResource(resources, localization) { base }}${
        string.substringAfter('(', "").addPrefixIfNotEmpty("(")
    }"
}

public fun String.toHtmlString(
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = htmlToAnnotatedString(this, compactMode, style, linkInteractionListener)
