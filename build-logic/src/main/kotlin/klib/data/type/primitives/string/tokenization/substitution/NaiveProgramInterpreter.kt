package klib.data.type.primitives.string.tokenization.substitution

public class NaiveProgramInterpreter() : Interpreter<MachineState, Program, (path:List<Any?>)->Any?> {
    private val interpreter = NaiveInterpreter()

    override fun initialState(input: (path:List<Any?>)->Any?): MachineState = interpreter.initialState(input)

    override fun join(s: MachineState, p: Program): MachineState =
        interpreter.join(
            p.functions.fold(s) { acc, function -> acc.declareFunction(function) },
            p.mainFunction.body
        )
}
