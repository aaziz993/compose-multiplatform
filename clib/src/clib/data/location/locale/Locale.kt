package clib.data.location.locale

import klib.data.location.locale.Locale
import org.jetbrains.compose.resources.ResourceItem
import org.jetbrains.compose.resources.StringResource

public fun stringResource(res: String, locale: Locale): StringResource = StringResource(
    "string:$res",
    res,
    setOf(
        ResourceItem(setOf(), "values$locale/strings.xml", -1, -1),
    ),
)
