package gradle.api

import com.charleskorn.kaml.MultiLineStringStyle
import com.charleskorn.kaml.SingleLineStringStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import gradle.api.cache.H2Cache
import klib.data.scripting.ScriptProperties
import klib.data.type.cast
import klib.data.type.serialization.yaml.decodeAnyFromString
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.plugins.ExtensionAware
import org.gradle.api.plugins.PluginAware
import org.gradle.api.provider.HasMultipleValues
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm

public abstract class Properties : ScriptProperties() {

    public companion object {

        private val yaml = Yaml(
            configuration = YamlConfiguration(
                singleLineStringStyle = SingleLineStringStyle.PlainExceptAmbiguous,
                multiLineStringStyle = MultiLineStringStyle.Plain,
            ),
        )

        private val EXPLICIT_OPERATION_RECEIVERS = setOf(
            Property::class,
            HasMultipleValues::class,
            MapProperty::class,
            ConfigurableFileCollection::class,
        )

        private val IMPORTS = arrayOf(
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

        internal inline operator fun <reified P : Properties, reified T> File.invoke(
            evaluationImplicitReceiver: T,
        ): P where T : PluginAware, T : ExtensionAware = ScriptProperties<P>(
            path,
            { path, templatePath ->
                File(path).parentFile.resolve(templatePath).path
            },
            decoder = { file ->
                yaml.decodeAnyFromString(File(file).readText())!!.cast()
            },
            cache = H2Cache(parentFile.resolve("$nameWithoutExtension.cache")),
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
        }.also(Properties::invoke)

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
