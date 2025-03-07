package gradle.model.android

import kotlinx.serialization.Serializable

/** Settings related to the gathering of code-coverage data from tests */
@Serializable
internal data class TestCoverage {
    /**
     * The version of JaCoCo to use.
     */
    var jacocoVersion: String
}
