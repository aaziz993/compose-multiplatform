package clib.presentation.locale

import klib.data.locale.Language
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

//@OptIn(InternalResourceApi::class, ExperimentalResourceApi::class)
public fun getStringResource(
    res: String,
    localization: Language,
): StringResource {
    return StringResource(
        "string:$res",
        res,
        setOf(
            ResourceItem(setOf(), "values${localization.value}/strings.xml"),
        ),
    )
}
