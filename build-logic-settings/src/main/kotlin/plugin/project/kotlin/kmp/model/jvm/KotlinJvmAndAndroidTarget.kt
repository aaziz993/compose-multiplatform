package plugin.project.kotlin.kmp.model.jvm

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import plugin.project.kotlin.kmp.model.KotlinTarget
import plugin.project.kotlin.model.HasConfigurableKotlinCompilerOptions

@Serializable
internal abstract class KotlinJvmAndAndroidTarget :
    KotlinTarget, HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    override val needKmp: Boolean
        get() = false

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinTarget>.applyTo(named)

        super<HasConfigurableKotlinCompilerOptions>.applyTo(named as org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions<*>)
    }
}

@Serializable
@SerialName("jvmAndAndroid")
internal data class KotlinJvmAndAndroidTargetImpl(
    override val compilations: List<@Serializable(with = KotlinJvmAndroidCompilationTransformingSerializer::class) KotlinJvmAndroidCompilation>? = null,
    override val compilerOptions: KotlinJvmCompilerOptions? = null,
) : KotlinJvmAndAndroidTarget() {

    override val targetName: String
        get() = ""
}
