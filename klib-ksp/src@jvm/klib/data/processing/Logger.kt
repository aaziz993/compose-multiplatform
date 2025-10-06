package klib.data.processing

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSNode

public class Logger(
    private val kspLogger: KSPLogger,
    private val loggingType: Int
) : KSPLogger by kspLogger {

    override fun error(
        message: String,
        symbol: KSNode?,
    ) {
        when (loggingType) {
            // Do nothing.
            0 -> {}

            // Throw compile errors for Compiler.
            1 -> kspLogger.error("Compiler: $message", symbol)

            // Turn errors into compile warnings.
            2 -> kspLogger.warn("Compiler: $message", symbol)
        }
    }
}
