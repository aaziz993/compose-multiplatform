package klib.data.type.primitives.string.tokenization.substitution

public class NaiveInterpreter : Interpreter<MachineState, Statement, (path:List<Any?>)->Any?> {
    override fun initialState(input: (path:List<Any?>)->Any?): MachineState = MachineState(input)

    override fun join(s: MachineState, p: Statement): MachineState = p.invoke(s)
}
