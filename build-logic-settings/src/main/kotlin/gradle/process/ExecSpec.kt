package gradle.process

import gradle.collection.SerializableAnyList
import org.gradle.api.Project

internal interface ExecSpec : BaseExecSpec< org.gradle.process.ExecSpec> {

    val commandLine: SerializableAnyList?

    val setCommandLine: SerializableAnyList?

    val args: SerializableAnyList?

    val setArgs: SerializableAnyList?

    context(Project)
    override fun applyTo(options:  org.gradle.process.ExecSpec) {
        super.applyTo(options)

        commandLine?.let(options::commandLine)
        setCommandLine?.let(options::setCommandLine)
        args?.let(options::args)
        setArgs?.let(options::setArgs)
    }
}
