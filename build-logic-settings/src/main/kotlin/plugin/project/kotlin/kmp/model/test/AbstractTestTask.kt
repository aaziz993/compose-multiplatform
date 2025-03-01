package plugin.project.kotlin.kmp.model.test

internal interface AbstractTestTask {
    val filter: DefaultTestFilter?
    val ignoreFailures: Boolean?
}
