package plugin.project.gradle.kover

import gradle.moduleProperties
import gradle.kover
import gradle.tryAssign
import kotlinx.kover.gradle.plugin.KoverGradlePlugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import plugin.project.gradle.kover.model.KoverReportFiltersConfig

internal fun Project.configureKoverExtension() =
    plugins.withType<KoverGradlePlugin> {
        moduleProperties.settings.gradle.kover.let { kover ->
            kover {
                useJacoco tryAssign kover.useJacoco
                jacocoVersion tryAssign kover.jacocoVersion

                kover.currentProject?.let { currentProject ->
                    currentProject {
                        currentProject.sources?.let { sources ->
                            sources {
                                excludeJava tryAssign sources.excludeJava
                                excludedSourceSets tryAssign sources.excludedSourceSets
                            }
                        }

                        currentProject.instrumentation?.let { instrumentation ->
                            instrumentation {
                                disabledForAll tryAssign instrumentation.disabledForAll
                                disabledForTestTasks tryAssign instrumentation.disabledForTestTasks
                                excludedClasses tryAssign instrumentation.excludedClasses
                                includedClasses tryAssign instrumentation.includedClasses
                            }
                        }
                    }
                }

                kover.reports?.let { reports ->
                    reports {
                        reports.filters?.let { filters ->
                            filters {
                                configFrom(filters)
                            }
                        }
                        reports.verify?.let { verify ->
                            verify {
                                warningInsteadOfFailure tryAssign verify.warningInsteadOfFailure
                            }
                        }
                        reports.total?.let { total ->
                            total {
                                total.filters?.let { filters ->
                                    filters {
                                        configFrom(filters)
                                    }
                                }

                                total.html?.let { html ->
                                    html {
                                        title tryAssign html.title
                                        charset tryAssign html.charset
                                        onCheck tryAssign html.onCheck
                                        htmlDir tryAssign html.htmlDir?.let(layout.projectDirectory::dir)
                                    }
                                }

                                total.xml?.let { xml ->
                                    xml {
                                        onCheck tryAssign xml.onCheck
                                        xmlFile tryAssign xml.xmlFile?.let(::file)
                                        title tryAssign xml.title
                                    }
                                }

                                total.binary?.let { binary ->
                                    binary {
                                        onCheck tryAssign binary.onCheck
                                        file tryAssign binary.file?.let(::file)
                                    }
                                }
                                total.verify?.let { verify ->
                                    verify {
                                        warningInsteadOfFailure tryAssign verify.warningInsteadOfFailure
                                        onCheck tryAssign verify.onCheck
                                    }
                                }

                                total.log?.let { log ->
                                    log {
                                        onCheck tryAssign log.onCheck
                                        header tryAssign log.header
                                        format tryAssign log.format
                                        groupBy tryAssign log.groupBy
                                        coverageUnits tryAssign log.coverageUnits
                                        aggregationForGroup tryAssign log.aggregationForGroup
                                    }
                                }

                                additionalBinaryReports tryAssign total.additionalBinaryReports?.map(::file)
                            }
                        }
                    }
                }
            }
        }
    }

private fun kotlinx.kover.gradle.plugin.dsl.KoverReportFiltersConfig.configFrom(config: KoverReportFiltersConfig) =
    apply {
        config.excludes?.let { excludes ->
            excludes {
                classes tryAssign excludes.classes
                annotatedBy tryAssign excludes.annotatedBy
                projects tryAssign excludes.projects
                inheritedFrom tryAssign excludes.inheritedFrom
            }
        }
        config.includes?.let { includes ->
            includes {
                classes tryAssign includes.classes
                annotatedBy tryAssign includes.annotatedBy
                projects tryAssign includes.projects
                inheritedFrom tryAssign includes.inheritedFrom
            }
        }
    }
