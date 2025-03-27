package gradle.process

import gradle.collection.SerializableAnyList
import org.gradle.api.Project

internal interface ExecSpec<T : org.gradle.process.ExecSpec> : BaseExecSpec<T> {

    val commandLine: SerializableAnyList?

    val setCommandLine: SerializableAnyList?

    val args: SerializableAnyList?

    val setArgs: SerializableAnyList?

    context(Project)
    override fun applyTo(receiver: T) {
        super.applyTo(receiver)

        receiver::commandLine trySet commandLine
        receiver::setCommandLine trySet setCommandLine
        receiver::args trySet args
        receiver::setArgs trySet setArgs
    }
}
