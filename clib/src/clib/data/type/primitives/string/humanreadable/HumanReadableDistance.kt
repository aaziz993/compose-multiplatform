package clib.data.type.primitives.string.humanreadable

import androidx.compose.runtime.Composable
import clib.data.type.primitives.string.stringResource
import com.ionspin.kotlin.bignum.decimal.BigDecimal
import klib.data.type.primitives.string.humanreadable.model.DistanceUnit
import klib.data.type.primitives.string.humanreadable.toHumanReadableDistance
import org.jetbrains.compose.resources.StringResource

@Suppress("ComposeUnstableReceiver", "ComposeParameterOrder")
@Composable
public fun BigDecimal.toHumanReadableDistance(
    unit: DistanceUnit = DistanceUnit.Meter,
    decimalsForLargeUnits: Int,
    meter: StringResource,
    kilometer: StringResource,
    feet: StringResource,
    mile: StringResource,
): String = toHumanReadableDistance(
    unit,
    decimalsForLargeUnits,
    stringResource(meter),
    stringResource(kilometer),
    stringResource(feet),
    stringResource(mile),
)
