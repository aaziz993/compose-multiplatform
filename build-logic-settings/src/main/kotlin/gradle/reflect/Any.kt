package gradle.reflect

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass

public operator fun Any.get(key: String): Any? {
    return null
}

@Suppress("UNCHECKED_CAST")
public fun <T : Any> T.genericTypes(): Array<Class<*>> =
    (javaClass.getGenericSuperclass() as ParameterizedType)
        .actualTypeArguments  as Array<Class<*>>
