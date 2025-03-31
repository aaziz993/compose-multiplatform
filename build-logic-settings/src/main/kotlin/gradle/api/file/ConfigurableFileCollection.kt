package gradle.api.file

import org.gradle.api.file.ConfigurableFileCollection

public infix fun ConfigurableFileCollection.from(value: Iterable<*>): ConfigurableFileCollection =
    from(* value.toList().toTypedArray())

public infix fun ConfigurableFileCollection.tryFrom(value: Iterable<*>?): ConfigurableFileCollection? =
    value?.let(::from)

public infix fun ConfigurableFileCollection.trySetFrom(value: Iterable<*>?): Unit? =
    value?.let(::setFrom)
