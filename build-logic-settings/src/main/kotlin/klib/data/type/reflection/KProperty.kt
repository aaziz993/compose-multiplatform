package klib.data.type.reflection

import kotlin.reflect.KProperty

public operator fun KProperty<*>.invoke(): Any? = getter()

public operator fun KProperty<*>.invoke(arg: Any): Any? = getter(arg)

