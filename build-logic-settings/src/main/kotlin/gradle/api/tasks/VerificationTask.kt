package gradle.api.tasks

import org.gradle.api.tasks.VerificationTask

internal interface VerificationTask<T : VerificationTask> {

    val ignoreFailures: Boolean?

    fun applyTo(receiver: T) {
        ignoreFailures?.let(receiver::setIgnoreFailures)
    }
}
