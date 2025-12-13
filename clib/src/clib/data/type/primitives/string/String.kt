@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import be.digitalia.compose.htmlconverter.HtmlStyle
import be.digitalia.compose.htmlconverter.htmlToAnnotatedString
import clib.presentation.locale.LocalLocalization
import klib.data.type.primitives.string.addPrefixIfNotEmpty
import klib.data.type.primitives.string.case.toSnakeCase
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.LocalResourceReader
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.ResourceReader
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.StringItem
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.currentOrPreview
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getResourceItemByEnvironment
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.getStringArray
import org.jetbrains.compose.resources.getStringItem
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import org.jetbrains.compose.resources.plural.PluralCategory
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.rememberResourceState
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

@Composable
public fun stringResource(resource: StringResource): String =
    LocalLocalization.current.getStringOrNull(resource.key) ?: stringResource(resource)

@OptIn(ExperimentalResourceApi::class)
@Composable
public fun pluralStringResource(resource: PluralStringResource, quantity: Int): String {
    val resourceReader = LocalResourceReader.currentOrPreview
    val pluralStr by rememberResourceState(resource, quantity, { "" }) { env ->
        loadPluralString(resource, quantity, resourceReader, env)
    }
    return pluralStr
}

@Suppress("ComposeUnstableReceiver")
@Composable
public fun String.asStringResource(
    resources: Map<String, StringResource>,
    defaultValue: () -> String = { this }
): String =
    resources[toSnakeCase()]?.let { stringResource -> stringResource(stringResource) } ?: defaultValue()

@Suppress("ComposeUnstableReceiver")
@Composable
public fun <T> T.asStringResource(resources: Map<String, StringResource>): String = toString().let { string ->
    val base = string
        .substringBefore('(')
        .substringAfterLast('.')
        .substringBefore('$')
        .substringBefore('@')
        .removeSuffix("Kt")
    "${base.asStringResource(resources) { base }}${
        string.substringAfter('(', "").addPrefixIfNotEmpty("(")
    }"
}

public fun String.toHtmlString(
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = htmlToAnnotatedString(this, compactMode, style, linkInteractionListener)

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

@Composable
public fun annotatedPluralStringResource(
    resource: PluralStringResource,
    quantity: Int,
    environment: ResourceEnvironment = getSystemResourceEnvironment(),
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = pluralStringResource(resource, quantity).toHtmlString(
    compactMode,
    style,
    linkInteractionListener,
)

@Composable
public fun annotatedPluralStringResource(
    resource: PluralStringResource,
    quantity: Int,
    vararg formatArgs: Any,
    environment: ResourceEnvironment = getSystemResourceEnvironment(),
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = pluralStringResource(resource, quantity, * formatArgs).toHtmlString(
    compactMode,
    style,
    linkInteractionListener,
)

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

public suspend fun getAnnotatedPluralString(
    resource: PluralStringResource,
    quantity: Int,
    environment: ResourceEnvironment = getSystemResourceEnvironment(),
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = getPluralString(environment, resource, quantity).toHtmlString(
    compactMode,
    style,
    linkInteractionListener,
)

public suspend fun getAnnotatedPluralString(
    resource: PluralStringResource,
    quantity: Int,
    vararg formatArgs: Any,
    environment: ResourceEnvironment = getSystemResourceEnvironment(),
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): AnnotatedString = getPluralString(environment, resource, quantity, * formatArgs).toHtmlString(
    compactMode,
    style,
    linkInteractionListener,
)

public suspend fun getAnnotatedStringArray(
    resource: StringArrayResource,
    environment: ResourceEnvironment = getSystemResourceEnvironment(),
    compactMode: Boolean = false,
    style: HtmlStyle = HtmlStyle.DEFAULT,
    linkInteractionListener: LinkInteractionListener? = null
): List<AnnotatedString> = getStringArray(environment, resource).map { value ->
    value.toHtmlString(
        compactMode,
        style,
        linkInteractionListener,
    )
}

@OptIn(ExperimentalResourceApi::class)
private suspend fun loadPluralString(
    resource: PluralStringResource,
    quantity: Int,
    resourceReader: ResourceReader,
    environment: ResourceEnvironment
): String {
    val resourceItem = resource.getResourceItemByEnvironment(environment)
    val item = getStringItem(resourceItem, resourceReader) as StringItem.Plurals
    val pluralRuleList = PluralRuleList.getInstance(
        environment.language,
        environment.region,
    )
    val pluralCategory = pluralRuleList.getCategory(quantity)
    val str = item.items[pluralCategory]
        ?: item.items[PluralCategory.OTHER]
        ?: error("Quantity string ID=`${resource.key}` does not have the pluralization $pluralCategory for quantity $quantity!")
    return str
}
