package plugin.project.kotlin.kmp.model.web

import gradle.kotlin
import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl

internal interface KotlinWasmJsTargetDsl : KotlinWasmTargetDsl, KotlinJsTargetDsl {

    val useD8: Boolean?
    val d8: KotlinWasmD8Dsl?

    context(Project)
    override fun applyTo(named: Named) {
        super<KotlinWasmTargetDsl>.applyTo(named)
        super<KotlinJsTargetDsl>.applyTo(named)

        named as KotlinWasmJsTargetDsl

        useD8?.takeIf { it }?.run { named.d8() }

        d8?.let { d8 ->
            named.d8 {
                d8.applyTo(this)
            }
        }
    }

    context(Project)
    override fun applyTo() =
        applyTo(kotlin.targets.withType<KotlinWasmJsTargetDsl>())
}
