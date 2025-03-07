@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package gradle.model

import gradle.tryAssign
import org.gradle.api.Named
import org.gradle.api.Project

internal interface ProducesKlib : Task {

    val produceUnpackagedKlib: Boolean?

    context(Project)
    override fun applyTo(named: Named) {
        super.applyTo(named)

        named as org.jetbrains.kotlin.gradle.internal.tasks.ProducesKlib

        named.produceUnpackagedKlib tryAssign produceUnpackagedKlib
    }
}
