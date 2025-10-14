package klib.data.location.country

import platform.CoreTelephony.CTTelephonyNetworkInfo
import platform.Foundation.NSLocale
import platform.Foundation.countryCode
import platform.Foundation.currentLocale

public actual val Country.Companion.current: Country?
    get() {
        // From SIM Card.
        val carrier = CTTelephonyNetworkInfo().subscriberCellularProvider
        val simCountryCode = carrier?.isoCountryCode

        // Note: may be null if:
        // 1. Airplane mode;
        // 2. No SIM card;
        // 3. Device outside cellular service range.

        // From Current Locale.
        val currentLocale = NSLocale.currentLocale
        val localeCountryCode = currentLocale.countryCode

        // Prefer SIM country if available, otherwise fallback to locale.
        return (simCountryCode ?: localeCountryCode)?.let(Country::forCode)
    }
