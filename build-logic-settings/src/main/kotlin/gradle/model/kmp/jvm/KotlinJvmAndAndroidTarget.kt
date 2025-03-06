package gradle.model.kmp.jvm

import gradle.kotlin
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget
import gradle.model.kmp.KotlinTarget
import gradle.model.kotlin.HasConfigurableKotlinCompilerOptions

@Serializable
internal abstract class KotlinJvmAndAndroidTarget :
    KotlinTarget, HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    override val needKMP: Boolean
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

    context(Project)
    override fun applyTo() {
        super.applyTo(kotlin.targets.withType<KotlinJvmTarget>())
        super.applyTo(kotlin.targets.withType<KotlinAndroidTarget>())
    }
}
