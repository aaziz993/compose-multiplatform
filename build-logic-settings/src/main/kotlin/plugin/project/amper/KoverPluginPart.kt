package plugin.project.amper

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
                            title.assign(htmlPart.title)
                            charset = htmlPart.charset
                            if (htmlPart.onCheck != null) {
                                onCheck = htmlPart.onCheck ?: false
                            }

                            htmlPart.reportDir?.toFile()?.let { htmlDir = it }
                        }
                    }

                    if (xmlPart != null) {
                        xml {
                            onCheck = xmlPart.onCheck ?: false
                            xmlPart.reportFile?.toFile()?.let { xmlFile = it }
                        }

                        adjustXmlFactories()
                    }
                }

            }
        }
    }
}