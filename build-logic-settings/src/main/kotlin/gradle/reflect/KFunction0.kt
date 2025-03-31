package gradle.reflect

import gradle.ifTrue
import kotlin.reflect.KFunction0

public infix fun <T> KFunction0<T>.trySet(value: Boolean?): Boolean? =
    value?.ifTrue(::invoke)
