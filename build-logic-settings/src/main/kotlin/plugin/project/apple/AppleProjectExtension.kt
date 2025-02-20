package plugin.project.apple

import com.android.tools.r8.n
import gradle.moduleProperties
import gradle.settings
import gradle.trySet
import org.gradle.api.Project
import org.jetbrains.gradle.apple.ApplePlugin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.gradle.apple.apple
import plugin.project.apple.model.AppleBuildSettings
import plugin.project.apple.model.BuildConfiguration

internal fun Project.configureAppleProjectExtension() =
    plugins.withType<ApplePlugin> {
        moduleProperties.settings.apple.let { apple ->
            apple {
                ::teamID trySet apple.teamID
                apple.ios?.let { ios ->
                    iosApp {
                        ::bridgingHeader trySet ios.bridgingHeader

                        ios.buildConfigurations?.forEach { buildConfigurations->
                            buildConfigurations {
                               val v= (buildConfigurations.name?.let(::named)?:this)
                            }
                        }

//                        ::buildSettings trySet ios.buildSettings
//
//                        ios.configuration?.let(configuration)
//
//                        ::embedFrameworks trySet ios.embedFrameworks
//
//                        ::ipad trySet ios.ipad
//
//                        ::iphone trySet ios.iphone
//
//                        ios.productInfo?.let(productInfo::putAll)
//
//                        ::productModuleName trySet ios.productModuleName
//
//                        ::productName trySet ios.productName
//
//                        ::targetConfigurations trySet ios.targetConfigurations
//
//
//
//                        iosDeviceFragments[0].settings.ios.teamId?.let {
//                            buildSettings.DEVELOPMENT_TEAM(it)
//                        }
                        productInfo["UILaunchScreen"] = mapOf<String, Any>()
                    }
                }
            }
        }
    }
