package gradle.api

import org.gradle.api.NamedDomainObjectProvider

public fun <T : Any> NamedDomainObjectProvider<T>.configureEach(action: (T) -> Unit): Unit = configureEach(action)

public fun <T : Any> NamedDomainObjectProvider<T>.configure(action: (T) -> Unit): Unit = configure(action)
