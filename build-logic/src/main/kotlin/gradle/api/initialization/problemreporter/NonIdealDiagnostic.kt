package gradle.api.initialization.problemreporter

@Suppress("ExperimentalAnnotationRetention")
@RequiresOptIn(
    message = "This reports a diagnostic that might not be very friendly to users. Make sure you understand the UX it " +
        "provides by reading the KDoc, and check if there are no better alternatives.",
)
public annotation class NonIdealDiagnostic
