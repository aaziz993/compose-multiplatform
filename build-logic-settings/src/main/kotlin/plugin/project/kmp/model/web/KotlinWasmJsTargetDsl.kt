package plugin.project.kmp.model.web

import gradle.kotlin
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

internal interface KotlinWasmJsTargetDsl : KotlinWasmTargetDsl, KotlinJsTargetDsl {

    val d8: Boolean?
    val d8Dsl: KotlinWasmD8Dsl?

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinWasmTargetDsl>.applyTo(named)
        super<KotlinJsTargetDsl>.applyTo(named)

        named as KotlinWasmJsTargetDsl

        d8?.takeIf { it }?.run { named.d8() }

        d8Dsl?.let { d8Dsl ->
            named.d8 {
                d8Dsl.applyTo(this)
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(kotlin.targets.withType<KotlinWasmJsTargetDsl>())
}
