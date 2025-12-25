package klib.data.type.primitives.string.humanreadable

import com.ionspin.kotlin.bignum.decimal.BigDecimal
import klib.data.type.primitives.string.humanreadable.model.DistanceUnit

private const val MILE_IN_FEET = 5280

/**
 * Returns the given distance value in human-readable format.
 *
 * @receiver The distance to format.
 * @param unit The [DistanceUnit] the given value is in.
 * @param decimalsForLargeUnits The number of decimals to use in formatting larger than meters/feet.
 * @return a formatted string
 */
public fun BigDecimal.toHumanReadableDistance(
    unit: DistanceUnit = DistanceUnit.Meter,
    decimalsForLargeUnits: Int,
    meter: String = "m",
    kilometer: String = "km",
    feet: String = "ft",
    mile: String = "mi",
): String = when (unit) {
    DistanceUnit.Meter -> toMetric(decimalsForLargeUnits, meter, kilometer)
    DistanceUnit.Foot -> toImperial(decimalsForLargeUnits, feet, mile)
}

private fun BigDecimal.toMetric(
    decimalsForLargeUnits: Int,
    meter: String,
    kilometer: String,
): String {
    return if (this < 1000) {
        // Use meters
        toHumanReadable(decimals = 0) + " " + meter
    }
    else {
        // Convert to kilometers
        (this / 1000).toHumanReadable(decimals = decimalsForLargeUnits) + " " + kilometer
    }
}

private fun BigDecimal.toImperial(
    decimalsForLargeUnits: Int,
    feet: String,
    mile: String,
): String {
    return if (this < MILE_IN_FEET) {
        // Less than a mile, use feet
        toHumanReadable(decimals = 0) + " " + feet
    }
    else {
        // Convert to miles
        (this / MILE_IN_FEET).toHumanReadable(decimals = decimalsForLargeUnits) + " " + mile
    }
}
