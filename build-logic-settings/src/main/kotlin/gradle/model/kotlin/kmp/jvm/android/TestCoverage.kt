package gradle.model.kotlin.kmp.jvm.android

import com.android.build.api.dsl.TestCoverage
import gradle.libs
import gradle.settings
import gradle.trySet
import gradle.version
import gradle.versions
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
    fun applyTo(coverage: TestCoverage) {
        coverage::jacocoVersion trySet (jacocoVersion ?: settings.libs.versions.version("jacoco"))
    }
}
