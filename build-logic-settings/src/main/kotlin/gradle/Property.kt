package gradle

internal fun trySetSystemProperty(key: String, value: String) {
    if (System.getProperty(key) == null)
        System.setProperty(key, value)
}
