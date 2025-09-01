package klib.data.type.primitives.string.tokenization.evaluation.program

import klib.data.type.primitives.string.tokenization.evaluation.Interpreter

public class NaiveInterpreter : Interpreter<MachineState, Statement, (String) -> Any?> {
    override fun initialState(input: (name: String) -> Any?): MachineState = MachineState(input)

    override fun join(s: MachineState, p: Statement): MachineState = p.join(s)
}