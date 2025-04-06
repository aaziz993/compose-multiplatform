package gradle.plugins.kotlin.targets.nat

import klib.data.type.serialization.serializer.SetSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.NothingSerializer

/*
Use the following naming scheme:
    executable('foo', [debug, release]) -> fooDebugExecutable + fooReleaseExecutable
    executable() -> debugExecutable, releaseExecutable
    executable([debug]) -> debugExecutable
*/
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
@Serializable(with = KotlinNativeBinaryContainerSetSerializer::class)
internal abstract class KotlinNativeBinaryContainer
    : AbstractKotlinNativeBinaryContainer<org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer>(),
    Set<NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>>

@Serializable
private class MutableKotlinNativeBinaryContainer : KotlinNativeBinaryContainer(), MutableSet<NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>> by linkedSetOf()

@Suppress("UNCHECKED_CAST")
private object KotlinNativeBinaryContainerSetSerializer :
    KSerializer<KotlinNativeBinaryContainer> by SetSerializer(
        NativeBinary.serializer(NothingSerializer()) as KSerializer<NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>>,
        ::MutableKotlinNativeBinaryContainer,
    )
