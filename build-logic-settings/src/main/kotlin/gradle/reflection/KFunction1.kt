package gradle.reflection

import kotlin.reflect.KFunction1
import org.gradle.api.Action

@JvmName("tryApplyAction")
public infix fun <T> KFunction1<Action<T>, *>.tryApply(block: ((T) -> Unit)?): Any? =
    block?.let { block ->
        invoke(block)
    }
