package gradle.plugins.kmp.nat

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
            binaries.executable(executable.namePrefix, executable.buildTypes) {
                executable.applyTo(this)
            }
        }

        staticLib?.let { staticLib ->
            binaries.staticLib(staticLib.namePrefix, staticLib.buildTypes) {
                staticLib.applyTo(this)
            }
        }

        framework?.let { framework ->
            binaries.framework(framework.namePrefix, framework.buildTypes) {
                framework.applyTo(this)
            }
        }

        test?.let { test ->
            binaries.test(test.namePrefix, test.buildTypes) {
                test.applyTo(this)
            }
        }
    }
}
