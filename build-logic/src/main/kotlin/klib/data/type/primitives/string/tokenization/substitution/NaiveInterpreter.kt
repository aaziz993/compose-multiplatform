package klib.data.type.primitives.string.tokenization.substitution

import klib.data.type.primitives.string.tokenization.Interpreter

public class NaiveInterpreter : Interpreter<MachineState, Statement, (String) -> Any?> {
    override fun initialState(input: (name: String) -> Any?): MachineState = MachineState(input)

    override fun join(s: MachineState, p: Statement): MachineState = p.join(s)
}
