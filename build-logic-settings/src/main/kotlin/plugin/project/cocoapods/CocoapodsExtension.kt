package plugin.project.cocoapods

import com.gradle.enterprise.testacceleration.client.executor.remote.o
import gradle.cocoapods
import gradle.libs
import gradle.moduleProperties
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.plugin.cocoapods.KotlinCocoapodsPlugin
import plugin.project.kotlinnative.configureFrom

internal fun Project.configureCocoapodsExtension() =
    plugins.withType<KotlinCocoapodsPlugin> {
        moduleProperties.settings.cocoapods.let { cocoapods ->
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
