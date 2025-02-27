package plugin.project.kotlin.model.test

internal interface AbstractTestTask {
    val filter: DefaultTestFilter?
    val ignoreFailures: Boolean?
}
