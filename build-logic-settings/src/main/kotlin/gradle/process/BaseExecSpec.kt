package gradle.process

import org.gradle.api.Project
import org.gradle.process.BaseExecSpec

internal interface BaseExecSpec<T : BaseExecSpec> : ProcessForkOptions<T> {

    val ignoreExitValue: Boolean?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        ignoreExitValue?.let(recipient::setIgnoreExitValue)
    }
}
