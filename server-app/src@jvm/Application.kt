import arrow.continuations.SuspendApp
import arrow.continuations.ktor.server
import arrow.fx.coroutines.resourceScope
import com.github.ajalt.colormath.model.Ansi16
import io.ktor.server.application.*
import io.ktor.server.engine.applicationEnvironment
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import klib.data.type.ansi.Attribute
import klib.data.type.ansi.ansiSpan
import klib.data.type.primitives.string.scripting.Script
import klib.data.type.primitives.string.scripting.ScriptConfig
import klib.data.type.serialization.serializers.any.SerializableAny
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlinx.coroutines.awaitCancellation
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer

public fun main(): Unit = SuspendApp {
    resourceScope {
        server(
            Netty,
            rootConfig = serverConfig {
                module(Application::module)
            },
            configure = {

            },
        )

        awaitCancellation()
    }
}

@Suppress("unused")
public fun Application.module() {
}

public fun Application.ping(): Routing = routing {
    get("/ping") { call.respondText("pong") }
}

public fun Application.indexHtml(): Routing = routing {
    staticResources("/", "static") {
        default("static/index.html")
    }
}


@Serializable
public class ApplicationScript(
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>,
    override val fileTree: Map<String, List<String>>,
): Script(){
    public companion object {

        private val logger: Logger = Logging.getLogger(GradleScript::class.java)

        internal inline operator fun <reified P : GradleScript, reified T> File.invoke(
            evaluationImplicitReceiver: T
        ): P where T : PluginAware, T : ExtensionAware = Script<P>(
            path,
            cache = SqliteCache(
                parentFile.resolve(".$name.cache"),
                String.serializer(),
                String.serializer(),
            ),
            explicitOperationReceivers = EXPLICIT_OPERATION_RECEIVERS,
            implicitOperation = ::tryAssignProperty,
        ) {
            compilationImplicitReceivers = listOf(T::class)
            evaluationImplicitReceivers = listOf(evaluationImplicitReceiver)

            compilationBody = {
                jvm {
                    dependenciesFromCurrentContext(wholeClasspath = true)
                }

                defaultImports(*IMPORTS)
            }
        }.also { properties ->
            logger.lifecycle(
                evaluationImplicitReceiver.toString()
                    .uppercase()
                    .ansiSpan {
                        attribute(Attribute.INTENSITY_BOLD)
                        attribute(Ansi16(36))
                    },
            )
            logger.lifecycle(properties.toString())
        }

        internal fun tryAssignProperty(valueClass: KClass<*>, value: Any?): String? =
            when {
                valueClass.isSubclassOf(Property::class) ||
                    valueClass.isSubclassOf(HasMultipleValues::class) ||
                    valueClass.isSubclassOf(MapProperty::class) -> ".set($value)"

                valueClass.isSubclassOf(ConfigurableFileCollection::class) -> ".setFrom($value)"

                else -> null
            }
    }
}
