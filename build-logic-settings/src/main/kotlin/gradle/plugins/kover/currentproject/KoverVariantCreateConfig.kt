package gradle.plugins.kover.currentproject

import kotlinx.kover.gradle.plugin.dsl.KoverVariantCreateConfig
import kotlinx.serialization.Serializable

@Serializable
internal data class KoverVariantCreateConfig(
    override val sources: KoverVariantSources? = null,
    /**
     * Add to created variant classes, tests and instrumented classes from report variant with name [variantNames].
     * This variant is taken only from the current project.
     *
     * If [optional] is `false` and a variant with given name is not found in the current project, an error [KoverIllegalConfigException] is thrown.
     */
    val adds: Set<Variant>? = null,
    /**
     * Add to created variant classes, tests and instrumented classes from report variant with name [variantNames].
     * This variant is taken from the current project and all `kover(project("name"))` dependency projects.
     *
     * If [optional] is `false` and a variant with given name is not found in the current project, an error [KoverIllegalConfigException] is thrown.
     *
     * If [optional] is `true` and a variant with given name is not found in the current project - in this case, the variant will not be searched even in dependencies.
     */
    val addsWithDependencies: Set<Variant>? = null,
) : KoverVariantConfig<KoverVariantCreateConfig> {

    override fun applyTo(receiver: KoverVariantCreateConfig) {
        super.applyTo(receiver)

        adds?.forEach { (names, optional) ->
            receiver.add(names, optional)
        }

        addsWithDependencies?.forEach { (names, optional) ->
            receiver.addWithDependencies(names, optional)
        }
    }
}

