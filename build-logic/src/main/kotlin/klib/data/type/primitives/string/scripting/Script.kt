package klib.data.type.primitives.string.scripting

import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.SourceCode
import kotlin.script.experimental.api.valueOrThrow
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

/**
 * Consumes only public implicit receivers
 */
public operator fun SourceCode.invoke(
    compilationConfiguration: ScriptCompilationConfiguration,
    evaluationConfiguration: ScriptEvaluationConfiguration?,
): Any? = BasicJvmScriptingHost()
    .eval(this, compilationConfiguration, evaluationConfiguration)
    .valueOrThrow()
    .returnValue.let { returnValue ->
        when (returnValue) {
            is ResultValue.Unit -> Unit
            is ResultValue.Value -> returnValue.value
            is ResultValue.Error -> returnValue.error
            ResultValue.NotEvaluated -> returnValue.scriptInstance
        }
    }

public operator fun SourceCode.invoke(config: ScriptConfig): Any? =
    this(
        config.toScriptCompilationConfiguration(),
        config.toScriptEvaluationConfiguration(),
    )

public operator fun String.invoke(
    compilationConfiguration: ScriptCompilationConfiguration,
    evaluationConfiguration: ScriptEvaluationConfiguration?,
): Any? = toScriptSource()(compilationConfiguration, evaluationConfiguration)

public operator fun String.invoke(config: ScriptConfig): Any? =
    this(
        config.toScriptCompilationConfiguration(),
        config.toScriptEvaluationConfiguration(),
    )
