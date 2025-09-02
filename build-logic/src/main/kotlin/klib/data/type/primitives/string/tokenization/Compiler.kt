package klib.data.type.primitives.string.tokenization

public interface Compiler<TSource, TTarget> {
    public fun compile(source: TSource): TTarget
}