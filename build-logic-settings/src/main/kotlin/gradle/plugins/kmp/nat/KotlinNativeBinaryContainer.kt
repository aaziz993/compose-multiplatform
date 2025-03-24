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
    override val executable: ExecutableSettings? = null,
    override val staticLib: StaticLibrarySettings? = null,
    override val sharedLib: SharedLibrarySettings? = null,
    override val framework: FrameworkSettings? = null,
    override val test: TestExecutableSettings? = null,
) : AbstractKotlinNativeBinaryContainer<KotlinNativeBinaryContainer>()
