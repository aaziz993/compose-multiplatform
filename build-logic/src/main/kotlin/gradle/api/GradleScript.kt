package gradle.api

import com.github.ajalt.colormath.model.Ansi16
import gradle.api.cache.SqliteCache
import klib.data.type.primitives.string.scripting.Script
import kotlinx.serialization.builtins.serializer
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.logging.Logger
import org.gradle.api.logging.Logging
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.PluginAware
import org.gradle.api.provider.HasMultipleValues
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import java.io.File
import klib.data.type.ansi.Attribute
import klib.data.type.ansi.ansiSpan
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

public val EXPLICIT_OPERATION_RECEIVERS: Set<KClass<out Any>> = setOf(
    Property::class,
    HasMultipleValues::class,
    MapProperty::class,
    ConfigurableFileCollection::class,
)

public val IMPORTS: Array<String> = arrayOf(
    "gradle.api.*",
    "gradle.api.artifacts.*",
    "gradle.api.artifacts.dsl.*",
    "gradle.api.plugins.*",
    "gradle.api.ci.*",
    "gradle.api.file.*",
    "gradle.api.initialization.*",
    "gradle.api.initialization.dsl.*",
    "gradle.api.project.*",
    "gradle.api.provider.*",
    "gradle.plugins.develocity.*",
    "gradle.plugins.doctor.*",
    "gradle.plugins.dokka.*",
    "gradle.plugins.knit.*",
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
    "klib.data.type.serialization.toml.*",
    "klib.data.type.serialization.properties.*",
    "klib.data.type.serialization.csv.*",
    "klib.data.type.serialization.xml.*",
)

public abstract class GradleScript : Script() {
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
