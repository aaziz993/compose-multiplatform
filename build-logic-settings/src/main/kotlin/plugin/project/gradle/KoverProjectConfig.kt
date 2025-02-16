package plugin.project.gradle

import gradle.libs
import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.KoverProjectExtension
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.assign

internal fun Project.configureKoverProject() {
    apply(plugin=libs.plugins.kover.get().pluginId)


    extensions.configure<KoverProjectExtension>(::configureKoverProjectExtension)

    tasks.register<Task>("koverReport") {
        dependsOn("koverHtmlReport", "koverXmlReport")
    }
}

private fun Project.configureKoverProjectExtension(extension: KoverProjectExtension) =
    extension.apply {
        reports {
            total {
                html {
                    title = "Tests using Kover with Gradle"
                    htmlDir = layout.projectDirectory.file("build/reports/kover").asFile
                    onCheck = true
                }
                xml {
                    title = "Tests using Kover with Gradle"
                    xmlFile = layout.projectDirectory.file("build/reports/kover/report.xml").asFile
                    onCheck = true
                }
            }
            filters {
                includes {
                    providers.gradleProperty("kover.filters.include.classes").get().let { c ->
                        if (c.isNotEmpty()) {
                            classes(
                                c.split(",").map { it.trim() },
                            )
                        }
                    }
                    providers.gradleProperty("kover.filters.include.packages").get().let { p ->
                        if (p.isNotEmpty()) {
                            packages(
                                p.split(",").map { it.trim() },
                            )
                        }
                    }
                }
                excludes {
                    providers.gradleProperty("kover.filters.exclude.classes").get().let { c ->
                        if (c.isNotEmpty()) {
                            classes(
                                c.split(",").map { it.trim() },
                            )
                        }
                    }
                    providers.gradleProperty("kover.filters.exclude.packages").get().let { p ->
                        if (p.isNotEmpty()) {
                            packages(
                                p.split(",").map { it.trim() },
                            )
                        }
                    }
                }
            }

            verify {
                rule {
                    disabled = providers.gradleProperty("kover.verify.rule.disabled").get().toBoolean()
                    bound {
                        minValue = providers.gradleProperty("kover.verify.rule.min.value").orNull?.toInt()
                        maxValue = providers.gradleProperty("kover.verify.rule.max.value").orNull?.toInt()
                        coverageUnits = providers.gradleProperty("kover.verify.rule.coverage-unit").orNull?.let {
                            CoverageUnit.valueOf(it.uppercase())
                        } ?: CoverageUnit.LINE
                    }
                }
            }
        }
    }

