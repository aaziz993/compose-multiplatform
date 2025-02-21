package plugin.project.web.model

import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

internal interface KotlinJsSubTargetDsl {
    val distribution:Distribution?

    val testTask: KotlinJsTest?

    val testRuns: NamedDomainObjectContainer<KotlinJsPlatformTestRun>
}
