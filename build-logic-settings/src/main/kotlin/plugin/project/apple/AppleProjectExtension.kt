package plugin.project.apple

import gradle.moduleProperties
import gradle.trySet
import org.gradle.api.Project
import org.gradle.kotlin.dsl.withType
import org.jetbrains.gradle.apple.ApplePlugin
import org.jetbrains.gradle.apple.apple

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
