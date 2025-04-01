package gradle.api.provider

import klib.data.type.act
import org.gradle.api.provider.HasMultipleValues
import org.gradle.api.provider.Provider

public infix fun <T, E> T.trySet(elements: Iterable<E>?): Unit? where T : Provider<out MutableCollection<E>>,
                                                                      T : HasMultipleValues<E> =
    tryAddAll(elements?.act(get()::clear))
