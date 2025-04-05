package gradle.plugins.android.test

import com.android.build.api.dsl.TestCoverage
import gradle.accessors.settings
import gradle.api.libs
import klib.data.type.reflection.trySet
import kotlinx.serialization.Serializable
import org.gradle.api.Project

/** Settings related to the gathering of code-coverage data from tests */
@Serializable
internal data class TestCoverage(
    /**
     * The version of JaCoCo to use.
     */
    val jacocoVersion: String? = null,
) {

    context(Project)
    fun applyTo(receiver: TestCoverage) {
        receiver::jacocoVersion trySet (jacocoVersion ?: project.settings.libs.versionOrNull("jacoco"))
    }
}
