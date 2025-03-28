package gradle.plugins.android.test

import com.android.build.api.dsl.TestCoverage
import gradle.accessors.catalog.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import gradle.api.trySet
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
        receiver::jacocoVersion trySet (jacocoVersion ?: project.settings.libs.version("jacoco"))
    }
}
