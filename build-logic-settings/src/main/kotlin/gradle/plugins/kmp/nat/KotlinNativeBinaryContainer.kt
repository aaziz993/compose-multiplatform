package gradle.plugins.kmp.nat

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer

/*
Use the following naming scheme:
    executable('foo', [debug, release]) -> fooDebugExecutable + fooReleaseExecutable
    executable() -> debugExecutable, releaseExecutable
    executable([debug]) -> debugExecutable
*/
@Serializable
internal class KotlinNativeBinaryContainer(
    @Transient
    private val binaries: Set<NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>> = mutableSetOf()
) : AbstractKotlinNativeBinaryContainer<KotlinNativeBinaryContainer>(),
    Set<@Serializable(with = NativeBinaryTransformingSerializer::class) NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>> by binaries
