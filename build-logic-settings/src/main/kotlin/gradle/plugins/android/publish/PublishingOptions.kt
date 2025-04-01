package gradle.plugins.android.publish

import com.android.build.api.dsl.PublishingOptions
import klib.data.type.reflection.trySet

/**
 * Maven publishing options shared by [SingleVariant] and [MultipleVariants].
 *
 * To publish sources & javadoc jar apart from AAR, use [withSourcesJar] and [withJavadocJar].
 * The following sets up publishing of sources & javadoc jar in two different publishing mechanisms.
 *
 * ```
 * android {
 *     publishing {
 *         singleVariant("release") {
 *             withSourcesJar()
 *             withJavadocJar()
 *         }
 *
 *         multipleVariants {
 *             withSourcesJar()
 *             withJavadocJar()
 *             allVariants()
 *         }
 *     }
 * }
 * ```
 */
internal interface PublishingOptions<T : PublishingOptions> {

    /**
     * Publish java & kotlin sources jar as a secondary artifact to a Maven repository.
     */
    val withSourcesJar: Boolean?

    /**
     * Publish javadoc jar generated from java & kotlin source as a secondary artifact to a Maven
     * repository.
     */
    val withJavadocJar: Boolean?

    fun applyTo(receiver: T) {
        receiver::withSourcesJar trySet withSourcesJar
        receiver::withJavadocJar trySet withJavadocJar
    }
}
