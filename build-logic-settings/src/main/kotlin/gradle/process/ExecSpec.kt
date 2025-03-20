package gradle.process

import gradle.collection.SerializableAnyList
import org.gradle.api.Project

internal interface ExecSpec<T : org.gradle.process.ExecSpec> : BaseExecSpec<T> {

    val commandLine: SerializableAnyList?

    val setCommandLine: SerializableAnyList?

    val args: SerializableAnyList?

    val setArgs: SerializableAnyList?

    context(Project)
    override fun applyTo(recipient: T) {
        super.applyTo(recipient)

        commandLine?.let(recipient::commandLine)
        setCommandLine?.let(recipient::setCommandLine)
        args?.let(recipient::args)
        setArgs?.let(recipient::setArgs)
    }
}
