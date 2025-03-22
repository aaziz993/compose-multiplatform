package gradle.plugins.java.tasks.shadow

import com.github.jengelman.gradle.plugins.shadow.relocation.SimpleRelocator
import kotlinx.serialization.Serializable

/**
 * Modified from org.apache.maven.plugins.shade.relocation.SimpleRelocator.java
 *
 * @author Jason van Zyl
 * @author Mauro Talevi
 * @author John Engelman
 */
@Serializable
internal data class Relocator(
    val pattern: String? = null,
    val shadedPattern: String? = null,
    val includes: List<String>? = null,
    val excludes: List<String>? = null,
    val rawString: Boolean = false
) {

    fun toRelocator() = SimpleRelocator(
        pattern,
        shadedPattern,
        includes,
        excludes,
        rawString,
    )
}
