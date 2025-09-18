package klib.data.type.reflection

import kotlin.reflect.KProperty

public operator fun KProperty<*>.invoke(instance: Any? = null): Any? = getter(instance)

