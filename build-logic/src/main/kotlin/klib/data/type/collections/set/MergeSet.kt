@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.collections.set

import klib.data.type.collections.merge
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
        val elements = delegate.deserialize(decoder)

        return if (
            eSerializer.descriptor.kind is StructureKind ||
            eSerializer.descriptor.kind is PolymorphicKind
        ) MergeSetIml(
            elements,
            merger = { o1, o2 ->
                when {
                    o1 == null -> o2
                    o2 == null -> o1
                    else -> eSerializer.deepPlus(o1, o2, serializersModule = decoder.serializersModule) as E
                }
            },
        )
        else MergeSetIml(elements)
    }
}

internal class MergeSetIml<E>(
    delegate: Collection<E>,
    override val equator: Equator<E> = Equator.default(),
    override val merger: Merger<E> = Merger.default(),
) : MergeSet<E>, Collection<E> by delegate

internal object EmptyMergeSet : MergeSet<Nothing> {

    override val equator: Equator<Nothing> = Equator { _, _ -> false }
    override val merger: Merger<Nothing> = Merger { _, _ ->
        throw UnsupportedOperationException("Cannot merge elements of EmptyMergeSet")
    }

    override fun equals(other: Any?): Boolean = other is MergeSet<*> && other.isEmpty()
    override fun hashCode(): Int = 0
    override fun toString(): String = "[]"

    override val size: Int get() = 0
    override fun isEmpty(): Boolean = true
    override fun contains(element: Nothing): Boolean = false
    override fun containsAll(elements: Collection<Nothing>): Boolean = elements.isEmpty()

    override fun iterator(): Iterator<Nothing> = EmptyIterator
}

public fun <T> mergeSetOf(
    vararg elements: T,
    equator: Equator<T> = Equator.default(),
    merger: Merger<T> = Merger.default()
): MergeSet<T> = elements.toMergeSet(equator, merger)

@Suppress("UNCHECKED_CAST")
public fun <T> emptyMergeSet(): MergeSet<T> = EmptyMergeSet as MergeSet<T>

/**
 * Returns a [MergeSet] of all elements.
 *
 * The returned set preserves the element iteration order of the original array.
 */
public fun <T> Array<out T>.toMergeSet(
    equator: Equator<T> = Equator.default(),
    merger: Merger<T> = Merger.default()
): MergeSet<T> =
    if (isEmpty()) emptyMergeSet() else MergeSetIml(toList().merge(equator, merger), equator, merger)
