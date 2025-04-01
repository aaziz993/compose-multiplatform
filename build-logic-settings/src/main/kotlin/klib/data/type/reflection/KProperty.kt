package klib.data.type.reflection

import kotlin.reflect.KProperty

public operator fun KProperty<*>.invoke() = getter()

public operator fun KProperty<*>.invoke(arg: Any) = getter(arg)

