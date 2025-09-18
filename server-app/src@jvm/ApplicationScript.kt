import com.github.ajalt.colormath.model.Ansi16
import java.io.File
import klib.data.cache.SqliteCache
import klib.data.type.ansi.Attribute
import klib.data.type.ansi.ansiSpan
import klib.data.type.primitives.string.scripting.Script
import klib.data.type.primitives.string.scripting.ScriptConfig
import klib.data.type.serialization.serializers.any.SerializableAny
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

public val IMPORTS: Array<String> = arrayOf(
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

@Serializable
public class ApplicationScript(
    override val config: ScriptConfig = ScriptConfig(),
    override val script: List<SerializableAny>,
    override val fileTree: Map<String, List<String>>,
) : Script() {

    public companion object {

        private val log: Logger = LoggerFactory.getLogger(Script::class.java)

        internal inline operator fun <reified T : Any> invoke(
            file: File,
            evaluationImplicitReceiver: T
        ): ApplicationScript = Script<ApplicationScript>(
            file.path,
            cache = SqliteCache(
                file.parentFile.resolve(".${file.name}.cache"),
                String.serializer(),
                String.serializer(),
            ),
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
            log.info(
                evaluationImplicitReceiver.toString()
                    .uppercase()
                    .ansiSpan {
                        attribute(Attribute.INTENSITY_BOLD)
                        attribute(Ansi16(36))
                    },
            )
            log.info(properties.toString())
        }
    }
}
