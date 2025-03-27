package gradle.plugins.kotlin.hierarchy

import org.jetbrains.kotlin.gradle.dsl.KotlinHierarchyDsl

/**
 * A DSL to apply hierarchy templates in a Kotlin project.
 */
internal interface KotlinHierarchyDsl<T : KotlinHierarchyDsl> {

    /**
     * Allows creating a fully custom hierarchy (no defaults applied).
     *
     * **Note: ** Using the custom hierarchy requires setting the edges to 'commonMain' and 'commonTest' SourceSets by
     * using the `common` group.
     *
     * *Examples:*
     *
     * - Share code between iOS and JVM targets:
     * ```kotlin
     * applyHierarchyTemplate {
     *     common {
     *         withJvm()
     *         group("ios") {
     *             withIos()
     *         }
     *     }
     * }
     * ```
     *
     * This configuration creates two [KotlinSourceSetTree] using the 'common' and 'ios' groups,
     * applied on the "test" and "main" compilations.
     * When the following targets are specified:
     * - jvm()
     * - iosX64()
     * - iosArm64()
     * ```
     *                    "main"                               "test"
     *                  commonMain                           commonTest
     *                      |                                    |
     *                      |                                    |
     *           +----------+----------+              +----------+----------+
     *           |                     |              |                     |
     *         iosMain               jvmMain        iosTest               jvmTest
     *           |                                    |
     *      +----+-----+                         +----+-----+
     *      |          |                         |          |
     * iosX64Main   iosArm64Main            iosX64Test   iosArm64Test
     * ```
     *
     * - Create a 'diamond structure'
     * ```kotlin
     * applyHierarchyTemplate {
     *     common {
     *         group("ios") {
     *             withIos()
     *         }
     *
     *         group("frontend") {
     *             withJvm()
     *             group("ios") // <- ! We can again reference the 'ios' group
     *         }
     *
     *         group("apple") {
     *             withMacos()
     *             group("ios") // <- ! We can again reference the 'ios' group
     *         }
     *     }
     * }
     * ```
     *
     * In this case, the _group_ "ios" can be created with 'group("ios")' and later referenced with the same construction to build
     * the tree.
     * Apply the descriptor from the example to the following targets:
     * - iosX64()
     * - iosArm64()
     * - macosX64()
     * - jvm()
     *
     * To create the following 'main' KotlinSourceSetTree:
     *
     * ```
     *                      commonMain
     *                           |
     *              +------------+----------+
     *              |                       |
     *          frontendMain            appleMain
     *              |                        |
     *    +---------+------------+-----------+----------+
     *    |                      |                      |
     * jvmMain                iosMain               macosX64Main
     *                           |
     *                           |
     *                      +----+----+
     *                      |         |
     *                iosX64Main   iosArm64Main
     * ```
     */
    val applyHierarchyTemplate: KotlinHierarchyBuilder.Root?

    fun applyTo(receiver: KotlinHierarchyDsl) {
        applyHierarchyTemplate?.let { applyHierarchyTemplate ->
            receiver.applyHierarchyTemplate(applyHierarchyTemplate::applyTo)
        }
    }
}
