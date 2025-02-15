package plugin.project.compose.desktop

import gradle.amperModuleExtraProperties
import gradle.trySet
import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import org.gradle.kotlin.dsl.assign
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

internal fun Project.configureDesktopExtension(extension: DesktopExtension) =
    with(amperModuleExtraProperties.settings.compose) {
        extension.apply {
            application {
                jvmArgs(
                    "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
                    "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED",
                )

                if (OperatingSystem.current() == OperatingSystem.MAC_OS) {
                    jvmArgs("--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED")
                    jvmArgs("--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
                }

                nativeDistributions {
                    targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)

                    ::packageName trySet desktop.packageName
                    ::packageVersion trySet desktop.packageVersion

                    with(desktop) {
                        linux {
                            iconFile = file(linux.iconFile)
                        }
                        windows {
                            iconFile = file(windows.iconFile)
                        }
                        macOS {
                            iconFile = file(macOs.iconFile)

                            bundleID = macOs.bundleId ?: "$group.$name"
                        }
                    }
                }
                // also proguard rules
                buildTypes.release.proguard {
                    configurationFiles.from("compose-desktop.pro")
                }
            }
        }
    }
