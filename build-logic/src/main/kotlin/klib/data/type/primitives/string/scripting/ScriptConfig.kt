package klib.data.type.primitives.string.scripting

import java.io.File
import kotlin.reflect.KClass
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.compilerOptions
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.api.implicitReceivers
import kotlin.script.experimental.api.importScripts
import kotlin.script.experimental.api.providedProperties
import kotlin.script.experimental.api.scriptFileLocationVariable
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.jdkHome
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvm.jvmTarget
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
public data class ScriptConfig(
    var jdkHome: String? = null,
    var jvmTarget: String? = null,
    var properties: Map<String, String> = emptyMap(),
    var scriptFile: String? = null,
    var imports: Set<String> = emptySet(),
    var scripts: Set<String> = emptySet(),
    var compilerOptions: Set<String> = emptySet(),
    @Transient
    var compilationImplicitReceivers: List<KClass<*>> = emptyList(),
    @Transient
    var evaluationImplicitReceivers: List<Any> = emptyList(),
    @Transient
    var compilationBody: ScriptCompilationConfiguration.Builder.() -> Unit = {},
    @Transient
    var evaluationBody: ScriptEvaluationConfiguration.Builder.() -> Unit = {},
) {

    public fun toScriptCompilationConfiguration(
        body: ScriptCompilationConfiguration.Builder.() -> Unit = {}
    ): ScriptCompilationConfiguration = ScriptCompilationConfiguration {
        jvm {
            this@ScriptConfig.jdkHome?.let(::File)?.let { jdkHome -> jdkHome(jdkHome) }
            this@ScriptConfig.jvmTarget?.let { jvmTarget -> jvmTarget(jvmTarget) }
        }

        providedProperties(
            * properties.map { (key, value) -> key to value }.toTypedArray(),
        )

        scriptFile?.let { scriptFile -> scriptFileLocationVariable(scriptFile) }

        defaultImports(*imports.toTypedArray())

        importScripts(
            *scripts.map(String::toScriptSource).toTypedArray(),
        )

        compilerOptions.append(* this@ScriptConfig.compilerOptions.toTypedArray())

        implicitReceivers(*compilationImplicitReceivers.toTypedArray())

        compilationBody()

        body()
    }

    public fun toScriptEvaluationConfiguration(
        body: ScriptEvaluationConfiguration.Builder.() -> Unit = {}
    ): ScriptEvaluationConfiguration = ScriptEvaluationConfiguration {
        implicitReceivers(*evaluationImplicitReceivers.toTypedArray())

        evaluationBody()

        body()
    }
}
