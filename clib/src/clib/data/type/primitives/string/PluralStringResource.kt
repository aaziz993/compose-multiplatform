@file:OptIn(ExperimentalResourceApi::class)
@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package clib.data.type.primitives.string

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkInteractionListener
import be.digitalia.compose.htmlconverter.HtmlStyle
import clib.presentation.locale.LocalLocalization
import klib.data.location.locale.Locale
import klib.data.location.locale.Localization
import klib.data.location.locale.current
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.LocalResourceReader
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.ResourceEnvironment
import org.jetbrains.compose.resources.ResourceReader
import org.jetbrains.compose.resources.StringItem
import org.jetbrains.compose.resources.currentOrPreview
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getResourceItemByEnvironment
import org.jetbrains.compose.resources.getStringItem
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import org.jetbrains.compose.resources.plural.PluralCategory
import org.jetbrains.compose.resources.rememberResourceState
import org.jetbrains.compose.resources.replaceWithArgs

@Composable
public fun pluralStringResource(resource: PluralStringResource, quantity: Int): String {
    val localization = LocalLocalization.current
    val resourceReader = LocalResourceReader.currentOrPreview
    val pluralStr by rememberResourceState(resource, quantity, { "" }) { env ->
        loadPluralString(resource, quantity, localization, resourceReader, env)
    }
    return pluralStr
}

@Composable
public fun pluralStringResource(resource: PluralStringResource, quantity: Int, vararg formatArgs: Any): String {
    val localization = LocalLocalization.current
    val resourceReader = LocalResourceReader.currentOrPreview
    val args = formatArgs.map { it.toString() }
    val pluralStr by rememberResourceState(resource, quantity, args, { "" }) { env ->
        loadPluralString(resource, quantity, args, localization, resourceReader, env)
    }
    return pluralStr
}

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

private suspend fun loadPluralString(
    resource: PluralStringResource,
    quantity: Int,
    localization: Localization,
    resourceReader: ResourceReader,
    environment: ResourceEnvironment
): String {
    val resourceItem = resource.getResourceItemByEnvironment(environment)
    val item = getStringItem(resourceItem, resourceReader) as StringItem.Plurals
    val pluralRuleList = PluralRuleList.getInstance(
        environment.language,
        environment.region,
    )
    val pluralCategory: PluralCategory = pluralRuleList.getCategory(quantity)
    val pluralCategoryIndex: Int = pluralRuleList.getCategoryIndex(quantity)

    val str = (if (localization.locale == Locale.current)
        localization.getStringOrNull(resource.key, pluralCategoryIndex)
    else null)
        ?: item.items[pluralCategory]
        ?: item.items[PluralCategory.OTHER]
        ?: error("Quantity string ID=`${resource.key}` does not have the pluralization $pluralCategory for quantity $quantity!")

    return str
}

private suspend fun loadPluralString(
    resource: PluralStringResource,
    quantity: Int,
    args: List<String>,
    localization: Localization,
    resourceReader: ResourceReader,
    environment: ResourceEnvironment
): String {
    val str = loadPluralString(resource, quantity, localization, resourceReader, environment)
    return str.replaceWithArgs(args)
}
