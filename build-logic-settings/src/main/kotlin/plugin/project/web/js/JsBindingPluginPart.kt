package plugin.project.web.js

import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.amper.frontend.Platform
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.amper.gradle.contains
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinWasmJsTargetDsl
import plugin.project.web.WebAwarePart
import plugin.project.web.js.karakum.configureKarakum

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class JsBindingPluginPart(
    ctx: PluginPartCtx,
) : WebAwarePart(ctx) {

    override val needToApply by lazy { Platform.JS in module }

    override val target: NamedDomainObjectCollection<out KotlinJsTargetDsl> by lazy {
        kotlinMPE.targets.withType<KotlinWasmJsTargetDsl>()
    }

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {
        super.applyBeforeEvaluate()
        project.configureKarakum()
    }
}

