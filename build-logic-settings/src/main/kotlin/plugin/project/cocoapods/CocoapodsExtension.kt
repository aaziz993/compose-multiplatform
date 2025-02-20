package plugin.project.cocoapods

import gradle.cocoapods
import gradle.kotlin
import gradle.moduleProperties
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import plugin.project.kotlinnative.configureFrom

internal fun Project.configureCocoapodsExtension() =
    plugins.withType<KotlinCocoapodsPlugin> {
        moduleProperties.settings.cocoapods.let { cocoapods ->
            kotlin {
                cocoapods {
                    ::version trySet cocoapods.version
                    ::authors trySet cocoapods.authors
                    ::podfile trySet cocoapods.podfile?.let(::file)
                    cocoapods.needPodspec?.takeIf { it }?.run { noPodspec() }
                    ::name trySet cocoapods.name
                    ::license trySet cocoapods.license
                    ::summary trySet cocoapods.summary
                    ::homepage trySet cocoapods.homepage
                    ::source trySet cocoapods.source
                    ::extraSpecAttributes trySet cocoapods.extraSpecAttributes?.toMutableMap()

                    cocoapods.framework?.let { framework ->
                        framework {
                            configureFrom(framework)
                        }
                    }

                    cocoapods.xcodeConfigurationToNativeBuildType?.let(xcodeConfigurationToNativeBuildType::putAll)
                    ::publishDir trySet cocoapods.publishDir?.let(::file)

                    cocoapods.specRepos?.let { specRepos ->
                        specRepos {
                            specRepos.forEach(::url)
                        }
                    }

                    cocoapods.pods?.forEach { pod ->
                        pod(
                            pod.name,
                            pod.version,
                            pod.path?.let(::file),
                            pod.moduleName,
                            pod.headers,
                            pod.linkOnly,
                        )
                    }


                    cocoapods.podDependencies?.forEach { podDependency ->
                        pod(name) {
                            ::moduleName trySet podDependency.moduleName
                            ::headers trySet podDependency.headers
                            ::version trySet podDependency.version
                            ::source trySet podDependency.source?.toPodLocation()
                            ::extraOpts trySet podDependency.extraOpts
                            ::packageName trySet podDependency.packageName
                            ::linkOnly trySet podDependency.linkOnly
                            podDependency.interopBindingDependencies?.let(interopBindingDependencies::addAll)
                            podDependency.podspecDirectory?.let(::path)
                        }
                    }

                    cocoapods.ios?.let { _ios ->
                        ios.apply {
                            ::deploymentTarget trySet _ios.deploymentTarget
                        }
                    }

                    cocoapods.osx?.let { _osx ->
                        osx.apply {
                            ::deploymentTarget trySet _osx.deploymentTarget
                        }
                    }

                    cocoapods.tvos?.let { _tvos ->
                        tvos.apply {
                            ::deploymentTarget trySet _tvos.deploymentTarget
                        }
                    }

                    cocoapods.watchos?.let { _watchos ->
                        watchos.apply {
                            ::deploymentTarget trySet _watchos.deploymentTarget
                        }
                    }
                }
            }
        }
    }
