package gradle.api

import org.gradle.api.tasks.VerificationTask

internal interface VerificationTask<in T : VerificationTask> {

    val ignoreFailures: Boolean?

    fun applyTo(recipient: T) {
        ignoreFailures?.let(recipient::setIgnoreFailures)
    }
}
