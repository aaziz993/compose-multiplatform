package klib.data.type.primitives.string.tokenization.substitution

import klib.data.type.primitives.string.tokenization.Interpreter

public class NaiveProgramInterpreter : Interpreter<MachineState, Program, (String) -> Any?> {
    private val interpreter = NaiveInterpreter()

    override fun initialState(input: (name: String) -> Any?): MachineState = interpreter.initialState(input)

    override fun join(s: MachineState, p: Program): MachineState =
        interpreter.join(
            p.functions.fold(s) { acc, function -> acc.declareFunction(function) },
            p.mainFunction.body
        )
}
