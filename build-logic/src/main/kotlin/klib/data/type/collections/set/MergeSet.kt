package klib.data.type.collections.set

import klib.data.type.functions.Equator
import klib.data.type.functions.Merger
import klib.data.type.serialization.deepPlus
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.descriptors.setSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable(with = MergeSetSerializer::class)
public interface MergeSet<E> : Set<E> {
    public val equator: Equator<E>
    public val merger: Merger<E>
}

public class MergeSetSerializer<E>(public val eSerializer: KSerializer<E>) : KSerializer<MergeSet<E>> {

    override val descriptor: SerialDescriptor = setSerialDescriptor(eSerializer.descriptor)

    private val delegate = ListSerializer(eSerializer)

    override fun serialize(encoder: Encoder, value: MergeSet<E>): Unit =
        delegate.serialize(encoder, value.toList())

    @Suppress("UNCHECKED_CAST")
    override fun deserialize(decoder: Decoder): MergeSet<E> {
        eSerializer as KSerializer<E & Any>

        val elements = delegate.deserialize(decoder)

        return (if (
            eSerializer.descriptor.kind is StructureKind ||
            eSerializer.descriptor.kind is PolymorphicKind
        ) MutableMergeSet<E>(
            merger = { o1, o2 ->
                when {
                    o1 == null -> o2
                    o2 == null -> o1
                    else -> eSerializer.deepPlus(o1, o2, serializersModule = decoder.serializersModule)
                }
            },
        )
        else MutableMergeSet()).apply {
            addAll(elements)
        }
    }
}
