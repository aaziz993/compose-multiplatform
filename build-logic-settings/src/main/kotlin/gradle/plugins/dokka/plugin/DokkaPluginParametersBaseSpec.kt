package gradle.plugins.dokka.plugin

import gradle.api.ProjectNamed
import gradle.serialization.serializer.JsonKeyValueTransformingContentPolymorphicSerializer
import kotlinx.serialization.Serializable

/**
 * Base class for defining Dokka Plugin configuration.
 *
 * This class should not be instantiated directly.
 * Instead, define a subclass that implements the [jsonEncode] function.
 *
 * @param[name] A descriptive name of the item in the [org.jetbrains.dokka.gradle.internal.DokkaPluginParametersContainer].
 * The name is only used for identification in the Gradle buildscripts.
 * @param[pluginFqn] Fully qualified classname of the Dokka Plugin
 */
@Serializable(with = DokkaPluginParametersBaseSpecKeyTransformingContentPolymorphicSerializer::class)
internal abstract class DokkaPluginParametersBaseSpec<T : org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec>
    : ProjectNamed<T> {

    abstract val pluginFqn: String
}

private object DokkaPluginParametersBaseSpecKeyTransformingContentPolymorphicSerializer : JsonKeyValueTransformingContentPolymorphicSerializer<DokkaPluginParametersBaseSpec<*>>(
    DokkaPluginParametersBaseSpec::class,
)
