package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import be.digitalia.compose.htmlconverter.HtmlStyle
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import klib.data.type.primitives.string.case.toSnakeCase
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Suppress("ComposeUnstableReceiver")
@Composable
public fun String.asStringResource(
    resources: Map<String, StringResource>,
    defaultValue: () -> String = { this }
): String =
    resources[lowercase().toSnakeCase()]?.let { stringResource -> stringResource(stringResource) } ?: defaultValue()

@Suppress("ComposeUnstableReceiver")
@Composable
public fun String.toHtmlString(
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = htmlToAnnotatedString(this, compactMode, style, linkInteractionListener)

@Suppress("ComposeUnstableReceiver")
@Composable
public fun String.toRichHtmlString(): AnnotatedString {
    val state = rememberRichTextState()

    LaunchedEffect(this) {
        state.setHtml(this@toRichHtmlString)
    }

    return state.annotatedString
}
