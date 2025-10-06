package processor

public class CompilerOptions(
    options: Map<String, String>
) {

    /**
     * 0: Turn off all Compiler related error checking
     *
     * 1: Check for errors
     *
     * 2: Turn errors into warnings
     */
    public val errorsLoggingType: Int = (options["Compiler_Errors"]?.toIntOrNull()) ?: 1

    public val kspResourcesDir: String = options["kspResourcesDir"]
        ?: error("KSP argument 'kspResourcesDir' is missing")
}
