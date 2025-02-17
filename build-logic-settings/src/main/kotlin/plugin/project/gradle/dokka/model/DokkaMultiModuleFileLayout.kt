package plugin.project.gradle.dokka.model

import org.jetbrains.dokka.gradle.AbstractDokkaTask
import org.jetbrains.dokka.gradle.DokkaMultiModuleFileLayout

/**
 * @see DokkaMultiModuleFileLayout.targetChildOutputDirectory
 * @see NoCopy
 * @see CompactInParent
 */
internal enum class DokkaMultiModuleFileLayout {

    /**
     * Will link to the original [AbstractDokkaTask.outputDirectory]. This requires no copying of the output files.
     */
    NoCopy,

    /**
     * Will point to a subfolder inside the output directory of the parent.
     * The subfolder will follow the structure of the gradle project structure
     * e.g.
     * :parentProject:firstAncestor:secondAncestor will be be resolved to
     * {parent output directory}/firstAncestor/secondAncestor
     */
    CompactInParent;

    fun toDokkaMultiModuleFileLayout(): DokkaMultiModuleFileLayout = when (this) {
        NoCopy -> DokkaMultiModuleFileLayout.NoCopy
        CompactInParent -> DokkaMultiModuleFileLayout.CompactInParent
    }
}

