package gradle.plugins.kotlin.targets.web

import org.gradle.api.Project

internal abstract class AbstractSettings<T> {

    context(Project)
    open fun applyTo(receiver: T) {
    }
}
