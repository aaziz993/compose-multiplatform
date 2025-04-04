package gradle.plugins.kotlin.targets.nat

import klib.data.type.serialization.serializer.JsonObjectTransformingSerializer
import klib.data.type.serialization.serializer.SetSerializer
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
@Serializable(with = KotlinNativeBinaryContainerTransformingSerializer::class)
internal abstract class KotlinNativeBinaryContainer
    : AbstractKotlinNativeBinaryContainer<org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer>(),
    Set<NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>> by hashSetOf()

private object KotlinNativeBinaryContainerTransformingSerializer
    : SetSerializer<KotlinNativeBinaryContainer, NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>>(
    NativeBinary.serializer(),
)
