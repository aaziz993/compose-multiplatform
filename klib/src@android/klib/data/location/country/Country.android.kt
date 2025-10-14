package klib.data.location.country

import android.content.Context
import android.telephony.TelephonyManager
import splitties.init.appCtx

/**
 * Get country iso code from device
 * @return Country object
 * @see Country
 */
public actual val Country.Companion.current: Country?
    get() {
        val telephonyManager =
            appCtx.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager

        val iso = telephonyManager.networkCountryIso ?: telephonyManager.simCountryIso
        ?: appCtx.resources.configuration.locales.get(0).country

        return iso?.let(Country::forCode)
    }
