package gradle.api.provider

import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.assign

public infix fun <K, V> MapProperty<K, V>.tryPutAll(entries: Map<out K?, V?>?): Unit? = entries?.let(::putAll)

public infix fun <K, V> MapProperty<K, V>.tryAssign(entries: Map<out K?, V?>?): Unit? = entries?.let(::assign)

public infix fun <K, V> MapProperty<K, V>.tryAssign(provider: Provider<out Map<out K?, V?>?>): Unit? =
    provider.takeIf(Provider<out Map<out K?, V?>?>::isPresent)?.let(::assign)
