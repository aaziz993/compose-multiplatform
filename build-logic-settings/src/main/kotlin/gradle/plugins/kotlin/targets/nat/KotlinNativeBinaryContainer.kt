package gradle.plugins.kotlin.targets.nat

import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.KeepGeneratedSerializer
import kotlinx.serialization.Serializable

/*
Use the following naming scheme:
    executable('foo', [debug, release]) -> fooDebugExecutable + fooReleaseExecutable
    executable() -> debugExecutable, releaseExecutable
    executable([debug]) -> debugExecutable
*/
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@KeepGeneratedSerializer
@Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class)
internal class KotlinNativeBinaryContainer(
    private val delegate: Set<NativeBinary<out @Contextual org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>>
) : AbstractKotlinNativeBinaryContainer<org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer>(),
    Set<NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>> by delegate

private object KotlinNativeBinaryContainerTransformingSerializer
    : JsonObjectTransformingSerializer<KotlinNativeBinaryContainer>(
    KotlinNativeBinaryContainer.generatedSerializer(),
    "delegate",
)
