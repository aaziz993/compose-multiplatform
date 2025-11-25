package clib.presentation.components.map

import klib.data.type.primitives.number.toDegrees
import klib.data.type.primitives.number.toRadians
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.exp
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.sin

/**
 * A utility class of methods that help when
 * dealing with standard Mercator projections.
 * @author joshua.marinacci@sun.com
 */
public object MercatorUtils {

    /**
     * @param longitudeDegrees the longitude in degrees
     * @param radius the world radius in pixels
     * @return the x value
     */
    public fun longToX(longitudeDegrees: Double, radius: Double): Int {
        val longitude: Double = longitudeDegrees.toRadians()
        return (radius * longitude).toInt()
    }

    /**
     * @param latitudeDegrees the latitude in degrees
     * @param radius the world radius in pixels
     * @return the y value
     */
    public fun latToY(latitudeDegrees: Double, radius: Double): Int {
        val latitude: Double = latitudeDegrees.toRadians()
        val y: Double = radius / 2.0 * ln((1.0 + sin(latitude)) / (1.0 - sin(latitude)))
        return y.toInt()
    }

    /**
     * @param x the x value
     * @param radius the world radius in pixels
     * @return the longitude in degrees
     */
    public fun xToLong(x: Int, radius: Double): Double {
        val longRadians = x / radius
        val longDegrees: Double = longRadians.toDegrees()
        /*
         * The user could have panned around the world a lot of times. Lat long goes from -180 to 180. So every time a
         * user gets to 181 we want to subtract 360 degrees. Every time a user gets to -181 we want to add 360 degrees.
         */
        val rotations = floor((longDegrees + 180) / 360)
        val longitude = longDegrees - (rotations * 360)
        return longitude
    }

    /**
     * @param y the y value
     * @param radius the world radius in pixels
     * @return the latitude in degrees
     */
    public fun yToLat(y: Int, radius: Double): Double {
        val latitude: Double = (PI / 2) - (2 * atan(exp(-1.0 * y / radius)))
        return latitude.toDegrees()
    }
}
