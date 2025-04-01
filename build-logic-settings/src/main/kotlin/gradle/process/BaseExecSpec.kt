package gradle.process

import klib.data.type.reflection.trySet
import org.gradle.api.Project
import org.gradle.process.BaseExecSpec

internal interface BaseExecSpec<T : BaseExecSpec> : ProcessForkOptions<T> {

    val ignoreExitValue: Boolean?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::setIgnoreExitValue trySet ignoreExitValue
    }
}
