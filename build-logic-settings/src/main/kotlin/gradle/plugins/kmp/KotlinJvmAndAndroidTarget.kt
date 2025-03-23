package gradle.plugins.kmp

import gradle.accessors.kotlin

import gradle.plugins.kmp.jvm.KotlinJvmCompilerOptions
import gradle.plugins.kotlin.HasConfigurableKotlinCompilerOptions
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinAndroidTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

internal abstract class KotlinJvmAndAndroidTarget :
    KotlinTarget, HasConfigurableKotlinCompilerOptions<KotlinJvmCompilerOptions> {

    override val needKMP: Boolean
        get() = false

        context(Project)
    override fun applyTo(recipient: T) {
        super<KotlinTarget>.applyTo(named)

        super<HasConfigurableKotlinCompilerOptions>.applyTo(named as org.jetbrains.kotlin.gradle.dsl.HasConfigurableKotlinCompilerOptions<*>)
    }
}

@Serializable
@SerialName("jvmCommon")
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
