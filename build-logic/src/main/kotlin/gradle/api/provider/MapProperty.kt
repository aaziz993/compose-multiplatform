package gradle.api.provider

import org.gradle.api.provider.MapProperty
import org.gradle.kotlin.dsl.assign

public infix fun <K : Any, V : Any> MapProperty<K, V>.tryAssign(entries: Map<out K, V>?): Unit? =
    entries?.let(::assign)

public infix fun <K : Any, V : Any> MapProperty<K, V>.tryPutAll(entries: Map<out K, V>?): Unit? =
    entries?.let(::putAll)
