package config

import com.github.ajalt.colormath.model.Ansi16
import io.ktor.server.config.yaml.YamlConfig
import java.io.File
import klib.data.type.ansi.Attribute
import klib.data.type.ansi.ansiSpan
import klib.data.type.primitives.string.scripting.Script
import klib.data.type.primitives.string.scripting.ScriptConfig
import klib.data.type.serialization.serializers.any.SerializableAny
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlinx.serialization.Serializable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public val IMPORTS: Array<String> = arrayOf(
    "io.ktor.http.*",
    "io.ktor.http.content.*",
    "io.ktor.server.application.*",
    "io.ktor.server.http.content.*",
    "io.ktor.server.netty.*",
    "io.ktor.server.response.*",
    "io.ktor.server.routing.*",
    "io.github.smiley4.ktoropenapi.*",
    "plugins.*",
    "klib.data.type.*",
    "klib.data.type.primitives.*",
    "klib.data.type.primitives.string.*",
    "klib.data.type.collections.*",
    "klib.data.type.functions.*",
    "klib.data.type.reflection.*",
    "klib.data.type.tuples.*",
    "klib.data.type.serialization.*",
    "klib.data.type.serialization.annotations.*",
    "klib.data.type.serialization.coders.*",
    "klib.data.type.serialization.serializers.*",
    "klib.data.type.serialization.json.*",
    "klib.data.type.serialization.yaml.*",
//    "klib.data.type.serialization.toml.*",
    "klib.data.type.serialization.properties.*",
    "klib.data.type.serialization.csv.*",
    "klib.data.type.serialization.xml.*",
)

public lateinit var applicationScript: ApplicationScript

@Serializable
public class ApplicationScript(
    public val host: String = "0.0.0.0",
    public val port: Int = 80,
    public val sslPort: Int = 443,
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>,
    override val fileTree: Map<String, List<String>>,
) : Script() {

    public companion object {

        private val logger: Logger = LoggerFactory.getLogger(Script::class.java)

        internal inline operator fun <reified TConfiguration : Any> invoke(
            engineConfigEvaluationImplicitReceiver: TConfiguration,
            serverConfigEvaluationImplicitReceiver: ServerConfig,
        ): ApplicationScript {
            val bootstrap = loadBootstrap()

            val environment = bootstrap.getOrElse("environment") { "dev" }.toString()

            val applicationFileName = "application-$environment.yaml"

            val applicationFile = {}.javaClass.classLoader.getResource(applicationFileName)?.toURI()?.path
                ?: error("$applicationFileName not found in resources")

            return Script<ApplicationScript>(
                File(applicationFile).canonicalPath,
            ) {
                compilationImplicitReceivers = listOf(TConfiguration::class, serverConfigEvaluationImplicitReceiver::class)
                evaluationImplicitReceivers = listOf(engineConfigEvaluationImplicitReceiver, serverConfigEvaluationImplicitReceiver)

                compilationBody = {
                    jvm {
                        dependenciesFromCurrentContext(wholeClasspath = true)
                    }

                    defaultImports(*IMPORTS)
                }
            }.also { properties ->
                logger.info(
                    "${System.lineSeparator()}${
                        engineConfigEvaluationImplicitReceiver.toString()
                            .uppercase()
                            .ansiSpan {
                                attribute(Attribute.INTENSITY_BOLD)
                                attribute(Ansi16(36))
                            }
                    }/${environment.ansiSpan { attribute(Ansi16(34)) }}",
                )
                logger.info("${System.lineSeparator()}$properties")

                applicationScript = properties
            }
        }

        private fun loadBootstrap(path: String = "bootstrap.yaml"): Map<String, Any?> {
            val yaml = YamlConfig(path) ?: return emptyMap()
            return yaml.toMap()
        }
    }
}
