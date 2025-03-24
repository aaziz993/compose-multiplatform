package gradle.process

import gradle.collection.SerializableAnyList
import org.gradle.api.Project

internal interface ExecSpec<T : org.gradle.process.ExecSpec> : BaseExecSpec<T> {

    val commandLine: SerializableAnyList?

    val setCommandLine: SerializableAnyList?

    val args: SerializableAnyList?

    val setArgs: SerializableAnyList?

    context(project: Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        commandLine?.let(receiver::commandLine)
        setCommandLine?.let(receiver::setCommandLine)
        args?.let(receiver::args)
        setArgs?.let(receiver::setArgs)
    }
}
