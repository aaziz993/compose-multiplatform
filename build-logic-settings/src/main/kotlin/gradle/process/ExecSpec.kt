package gradle.process

import klib.data.type.serialization.serializer.SerializableAnyList
import org.gradle.api.Project

internal interface ExecSpec<T : org.gradle.process.ExecSpec> : BaseExecSpec<T> {

    val commandLineArgs: SerializableAnyList?

    val setCommandLineArgs: SerializableAnyList?

    val args: SerializableAnyList?

    val setArgs: SerializableAnyList?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        commandLineArgs?.let(receiver::commandLine)
        setCommandLineArgs?.let(receiver::setCommandLine)
        args?.let(receiver::args)
        setArgs?.let(receiver::setArgs)
    }
}
