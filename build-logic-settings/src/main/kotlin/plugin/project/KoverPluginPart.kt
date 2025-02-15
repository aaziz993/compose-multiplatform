package plugin.project

import gradle.tryAssign
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.amper.gradle.adjustXmlFactories
import org.jetbrains.amper.gradle.base.BindingPluginPart
import org.jetbrains.amper.gradle.base.PluginPartCtx

internal class KoverPluginPart(ctx: PluginPartCtx) : BindingPluginPart by ctx {

    private val koverRE
        get() = project.extensions.findByType<KoverProjectExtension>()!!

    override val needToApply: Boolean by lazy {
        module.leafFragments.any { it.settings.kover?.enabled == true }
    }

    override fun applyBeforeEvaluate() {
        project.plugins.apply("org.jetbrains.kotlinx.kover")
        applySettings()
    }

    fun applySettings() {
        val koverSettings = module.leafFragments.map { it.settings.kover }.firstOrNull()
        val htmlPart = koverSettings?.html
        val xmlPart = koverSettings?.xml

        koverRE.apply {
            reports {
                total {
                    if (htmlPart != null) {
                        html {
                            title = htmlPart.title
                            charset = htmlPart.charset
                            if (htmlPart.onCheck != null) {
                                onCheck tryAssign htmlPart.onCheck
                            }


                            htmlDir tryAssign htmlPart.reportDir?.toFile()
                        }
                    }

                    if (xmlPart != null) {
                        xml {
                            onCheck tryAssign xmlPart.onCheck
                            xmlFile tryAssign xmlPart.reportFile?.toFile()
                        }

                        adjustXmlFactories()
                    }
                }

            }
        }
    }
}
