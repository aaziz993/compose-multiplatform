package klib.data.type.primitives.string.tokenization.evaluation.program

import klib.data.type.primitives.string.tokenization.evaluation.Interpreter

public class NaiveProgramInterpreter : Interpreter<MachineState, Program, (String) -> Any?> {
    private val interpreter = NaiveInterpreter()

    override fun initialState(input: (name: String) -> Any?): MachineState = interpreter.initialState(input)

    override fun join(state: MachineState, program: Program): MachineState =
        interpreter.join(
            program.functions.fold(state) { acc, function -> acc.declareFunction(function) },
            program.mainFunction.body
        )
}