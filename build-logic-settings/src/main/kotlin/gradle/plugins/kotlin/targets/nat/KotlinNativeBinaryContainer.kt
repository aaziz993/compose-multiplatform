package gradle.plugins.kotlin.targets.nat

import gradle.serialization.serializer.DelegateTransformingSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/*
Use the following naming scheme:
    executable('foo', [debug, release]) -> fooDebugExecutable + fooReleaseExecutable
    executable() -> debugExecutable, releaseExecutable
    executable([debug]) -> debugExecutable
*/
@Serializable
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
internal class KotlinNativeBinaryContainer(
    private val delegate: Set<@Serializable(with = NativeBinaryKeyTransformingSerializer::class) NativeBinary<out @Contextual org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>>
) : AbstractKotlinNativeBinaryContainer<org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer>(),
    Set<NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>> by delegate

internal object KotlinNativeBinaryContainerTransformingSerializer
    : DelegateTransformingSerializer<KotlinNativeBinaryContainer>(KotlinNativeBinaryContainer.serializer())
