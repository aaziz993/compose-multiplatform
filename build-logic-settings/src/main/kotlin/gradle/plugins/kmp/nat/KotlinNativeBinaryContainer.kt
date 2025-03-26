package gradle.plugins.kmp.nat

import gradle.serialization.serializer.DelegateTransformingSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.jsonObject

/*
Use the following naming scheme:
    executable('foo', [debug, release]) -> fooDebugExecutable + fooReleaseExecutable
    executable() -> debugExecutable, releaseExecutable
    executable([debug]) -> debugExecutable
*/
@Serializable
@Suppress("JavaDefaultMethodsNotOverriddenByDelegation")
internal class KotlinNativeBinaryContainer(
    private val delegate: Set<@Serializable(with = NativeBinaryKeyTransformingSerializer::class) NativeBinary<*>> = mutableSetOf()
) : AbstractKotlinNativeBinaryContainer<org.jetbrains.kotlin.gradle.dsl.KotlinNativeBinaryContainer>(),
    Set<NativeBinary<out org.jetbrains.kotlin.gradle.plugin.mpp.NativeBinary>> by delegate

internal object KotlinNativeBinaryContainerTransformingSerializer
    : DelegateTransformingSerializer<KotlinNativeBinaryContainer>(KotlinNativeBinaryContainer.serializer())
