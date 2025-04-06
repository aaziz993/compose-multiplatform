package gradle.api

import org.gradle.api.Describable

/**
 * Types can implement this interface when they provide a human-readable display name.
 * It is strongly encouraged to compute this display name lazily: computing a display name,
 * even if it's only a string concatenation, can take a significant amount of time during
 * configuration for something that would only be used, typically, in error messages.
 *
 * @since 3.4
 */
internal interface Describable<T : Describable> {

    fun applyTo(receiver: T)
}
