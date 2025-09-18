package klib.data.type.primitives.string.tokenization.substitution

public interface Interpreter<TState, TProgram, TInput> {
    public fun initialState(input: TInput): TState
    public fun join(s: TState, p: TProgram): TState
}

public operator fun <TState, TProgram, TInput> Interpreter<TState, TProgram, TInput>.invoke(
    program: TProgram,
    input: TInput
): TState = join(initialState(input), program)
