package data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.humanreadable.toHumanReadableDistance
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import compose_app.generated.resources.Res
import compose_app.generated.resources.m
import compose_app.generated.resources.km
import compose_app.generated.resources.ft
import compose_app.generated.resources.mi
import klib.data.type.primitives.string.humanreadable.model.DistanceUnit

@Suppress("ComposeUnstableReceiver", "ComposeParameterOrder")
@Composable
public fun BigDecimal.toHumanReadableDistance(
    unit: DistanceUnit = DistanceUnit.Meter,
    decimalsForLargeUnits: Int,
): String = toHumanReadableDistance(
    unit,
    decimalsForLargeUnits,
    Res.string.m,
    Res.string.km,
    Res.string.ft,
    Res.string.mi,
)
