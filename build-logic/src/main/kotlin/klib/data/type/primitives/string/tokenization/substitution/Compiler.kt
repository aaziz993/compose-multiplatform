package klib.data.type.primitives.string.tokenization.substitution

public interface Compiler<TSource, TTarget> {
    public fun compile(source: TSource): TTarget
}
