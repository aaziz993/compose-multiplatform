package plugin.project.web.js

import org.jetbrains.amper.frontend.Platform
import org.jetbrains.amper.gradle.base.PluginPartCtx
import org.jetbrains.amper.gradle.contains
import org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl
import plugin.project.web.WebAwarePart
import plugin.project.web.js.karakum.configureKarakum

/**
 * Plugin logic, bind to specific module, when only default target is available.
 */
internal class JsBindingPluginPart(ctx: PluginPartCtx) : WebAwarePart(ctx) {

    override val needToApply by lazy { Platform.JS in module }

    override val target: KotlinJsTargetDsl by lazy {
        kotlinMPE.js(module.artifactPlatforms.single { it ==Platform.JS }.targetName)
    }

    /**
     * Entry point for this plugin part.
     */
    override fun applyBeforeEvaluate() {
        super.applyBeforeEvaluate()
//        project.configureKarakum()
    }

    override fun applyAfterEvaluate() {
        super.applyAfterEvaluate()
        project.configureKarakum()
    }
}

