package gradle.model.kotlin.kmp.nat

import kotlinx.serialization.Serializable
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer

/*
Use the following naming scheme:
    executable('foo', [debug, release]) -> fooDebugExecutable + fooReleaseExecutable
    executable() -> debugExecutable, releaseExecutable
    executable([debug]) -> debugExecutable
*/
@Serializable
internal data class KotlinNativeBinaryContainer(
    val executable: ExecutableSettings? = null,
    val staticLib: SharedLibrarySettings? = null,
    val framework: FrameworkSettings? = null,
    val test: TestExecutableSettings? = null,
) {

    context(Project)
    fun applyTo(binaries: KotlinNativeBinaryContainer) {
        executable?.let { executable ->
            executable.buildTypes?.also { buildTypes ->
                binaries.executable(buildTypes) {
                    executable.applyTo(this)
                }
            } ?: binaries.executable {
                executable.applyTo(this)
            }
        }

        staticLib?.let { staticLib ->
            staticLib.buildTypes?.also { buildTypes ->
                binaries.staticLib(buildTypes) {
                    staticLib.applyTo(this)
                }
            } ?: binaries.staticLib {
                staticLib.applyTo(this)
            }
        }

        framework?.let { framework ->
            framework.buildTypes?.also { buildTypes ->
                binaries.framework(buildTypes) {
                    framework.applyTo(this)
                }
            } ?: binaries.framework {
                framework.applyTo(this)
            }
        }

        test?.let { test ->
            test.buildTypes?.also { buildTypes ->
                binaries.test(buildTypes) {
                    test.applyTo(this)
                }
            } ?: binaries.test {
                test.applyTo(this)
            }
        }
    }
}
