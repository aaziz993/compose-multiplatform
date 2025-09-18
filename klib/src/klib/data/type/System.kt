package klib.data.type

import java.lang.System.getProperty
import java.lang.System.setProperty

public fun trySetSystemProperty(key: String, value: String) {
    if (getProperty(key) == null)
        setProperty(key, value)
}








