package klib.data.location.country

import klib.data.location.locale.Locale
import klib.data.location.locale.current

public actual val Country.Companion.current: Country?
    get() = Locale.current.country()
