package clib.data.location.country

import clib.generated.resources.Res
import clib.generated.resources.allDrawableResources
import clib.generated.resources.image_load_error
import klib.data.location.country.Country
import org.jetbrains.compose.resources.DrawableResource

public val Country.flag: DrawableResource
    get() = Res.allDrawableResources["flag_${toString().lowercase()}"] ?: Res.drawable.image_load_error
