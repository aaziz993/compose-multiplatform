package clib.presentation.theme.model

import klib.data.type.serialization.serializers.transform.MapTransformingPolymorphicSerializer
import kotlin.reflect.KClass
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import androidx.compose.animation.core.AnimationSpec as ComposeAnimationSpec
import androidx.compose.animation.core.SpringSpec as ComposeSpringSpec
import androidx.compose.animation.core.TweenSpec as ComposeTweenSpec

@Serializable(AnimationSpecSerializer::class)
private sealed class AnimationSpec<T> {

    abstract fun toAnimatedSpec(): ComposeAnimationSpec<T>
}

@Suppress("UNCHECKED_CAST")
private class AnimationSpecSerializer<T>(tSerializer: KSerializer<T>) :
    MapTransformingPolymorphicSerializer<AnimationSpec<T>>(
        baseClass = AnimationSpec::class as KClass<AnimationSpec<T>>,
        subclasses = mapOf(
            SpringSpec::class as KClass<out AnimationSpec<T>> to SpringSpec.serializer(tSerializer) as KSerializer<out AnimationSpec<T>>,
            TweenSpec::class as KClass<out AnimationSpec<T>> to TweenSpec.serializer(tSerializer) as KSerializer<out AnimationSpec<T>>,
        ),
    )

private sealed class FiniteAnimationSpec<T> : AnimationSpec<T>()

private sealed class DurationBasedAnimationSpec<T> : FiniteAnimationSpec<T>()

@Serializable
@SerialName("spring")
private data class SpringSpec<T>(
    val dampingRatio: Float,
    val stiffness: Float,
    val visibilityThreshold: T? = null,
) : FiniteAnimationSpec<T>() {

    override fun toAnimatedSpec(): ComposeAnimationSpec<T> =
        ComposeSpringSpec(dampingRatio, stiffness, visibilityThreshold)
}

@Serializable
@SerialName("tween")
private class TweenSpec<T>(
    val durationMillis: Int,
    val delay: Int,
    val easing: EasingSerial,
) : DurationBasedAnimationSpec<T>() {

    override fun toAnimatedSpec(): ComposeAnimationSpec<T> = ComposeTweenSpec(durationMillis, delay, easing)
}

public class ComposeAnimationSpecSerializer<T>(tSerializer: KSerializer<T>) : KSerializer<ComposeAnimationSpec<T>> {

    private val delegate = AnimationSpec.serializer(tSerializer)

    override val descriptor: SerialDescriptor = delegate.descriptor

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: ComposeAnimationSpec<T>): Unit =
        encoder.encodeSerializableValue(
            delegate,
            when (value) {
                is ComposeSpringSpec<*> ->
                    SpringSpec(value.dampingRatio, value.stiffness, value.visibilityThreshold as T)

                is ComposeTweenSpec<*> -> TweenSpec(value.durationMillis, value.delay, value.easing)

                else -> throw IllegalArgumentException("Unknown easing '$value'")
            },
        )

    override fun deserialize(decoder: Decoder): ComposeAnimationSpec<T> =
        decoder.decodeSerializableValue(delegate).toAnimatedSpec()
}

public typealias AnimationSpecSerial<T> = @Serializable(with = ComposeAnimationSpecSerializer::class) ComposeAnimationSpec<T>
