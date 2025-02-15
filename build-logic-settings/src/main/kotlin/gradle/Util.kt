package gradle

import kotlin.reflect.KMutableProperty0

public infix fun <T : Any> KMutableProperty0<T>.trySet(value: T?) =
    value?.let { set(it) }
