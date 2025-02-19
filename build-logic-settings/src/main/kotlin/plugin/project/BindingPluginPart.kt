package plugin.project

import org.gradle.api.Project

/**
 * Shared module plugin properties.
 */
internal interface BindingPluginPart {
    val project: Project

    val needToApply: Boolean get() = false

    /**
     * Logic, that needs to be applied before project evaluation.
     */
    fun applyBeforeEvaluate() {}

    /**
     * Logic, that needs to be applied after project evaluation.
     */
    fun applyAfterEvaluate() {}
}
