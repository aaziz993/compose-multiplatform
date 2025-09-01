package klib.data.type.primitives.string.tokenization.evaluation

public interface Compiler<TSource, TTarget> {
    public fun compile(source: TSource): TTarget
}