package plugins.initialization.problemreporter

@RequiresOptIn(
    message = "This reports a diagnostic that might not be very friendly to users. Make sure you understand the UX it " +
            "provides by reading the KDoc, and check if there are no better alternatives.",
)
internal annotation class NonIdealDiagnostic
