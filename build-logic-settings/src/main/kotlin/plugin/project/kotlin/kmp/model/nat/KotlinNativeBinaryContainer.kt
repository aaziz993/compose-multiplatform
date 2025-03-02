package plugin.project.kotlin.kmp.model.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.gradle.kotlin.dsl.container
import org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer

/*
Use the following naming scheme:
    executable('foo', [debug, release]) -> fooDebugExecutable + fooReleaseExecutable
    executable() -> debugExecutable, releaseExecutable
    executable([debug]) -> debugExecutable
*/
@Serializable
internal data class KotlinNativeBinaryContainer(
    val executable: Executable? = null,
    val staticLib: SharedLibrary? = null,
    val framework: Framework? = null,
    val test: TestExecutable? = null,
) {

    context(Project)
    fun applyTo(binaries: KotlinNativeBinaryContainer) {
        executable?.let { executable ->
            executable.buildTypes?.also { buildTypes ->
                binaries.executable(buildTypes) {
                    executable.applyTo(container { this })
                }
            } ?: binaries.executable {
                executable.applyTo(container { this })
            }
        }

        staticLib?.let { staticLib ->
            staticLib.buildTypes?.also { buildTypes ->
                binaries.staticLib(buildTypes) {
                    staticLib.applyTo(container { this })
                }
            } ?: binaries.staticLib {
                staticLib.applyTo(container { this })
            }
        }

        framework?.let { framework ->
            framework.buildTypes?.also { buildTypes ->
                binaries.framework(buildTypes) {
                    framework.applyTo(container { this })
                }
            } ?: binaries.framework {
                framework.applyTo(container { this })
            }
        }

        test?.let { test ->
            test.buildTypes?.also { buildTypes ->
                binaries.test(buildTypes) {
                    test.applyTo(container { this })
                }
            } ?: binaries.test {
                test.applyTo(container { this })
            }
        }
    }
}
