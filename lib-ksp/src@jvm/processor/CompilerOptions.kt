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
    public val errorsLoggingType: Int = (options["errorsLoggingType"]?.toIntOrNull()) ?: 1

    public val kspResourcesDir: String = options["projectDir"]?.let { projectDir -> "$projectDir/kspResources" }
        ?: error("KSP argument 'projectDir' is missing")

    public val commonMainKotlinSrc: String = options["commonMainKotlinSrc"]
        ?: error("KSP argument 'commonMainKotlinSrc' is missing")
}
