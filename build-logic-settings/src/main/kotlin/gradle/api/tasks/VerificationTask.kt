package gradle.api.tasks

import klib.data.type.reflection.trySet
import org.gradle.api.tasks.VerificationTask

internal interface VerificationTask<T : VerificationTask> {

    val ignoreFailures: Boolean?

    fun applyTo(receiver: T) {
        receiver::setIgnoreFailures trySet ignoreFailures
    }
}
