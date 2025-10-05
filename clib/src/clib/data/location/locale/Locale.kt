package clib.data.location.locale

import klib.data.location.locale.Locale
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

public fun getStringResource(
    res: String,
    localization: Locale,
): StringResource {
    return StringResource(
        "string:$res",
        res,
        setOf(
            ResourceItem(setOf(), "values${localization.toLanguageTag()}/strings.xml", -1, -1),
        ),
    )
}
