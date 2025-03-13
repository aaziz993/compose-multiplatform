package gradle.plugins.spotless

import com.diffplug.gradle.spotless.HasBuiltinDelimiterForLicense

/**
 * Every [FormatExtension] has a method
 * [license(licenseContent, licenseDelimiter)][FormatExtension.licenseHeader],
 * where licenseDelimiter is a regex that separates the license part of the code from the content.
 * For some kinds of format -
 * such as [java][JavaExtension], [kotlin][gradle.plugins.spotless.kotlin.KotlinExtension], and [groovy][gradle.plugins.spotless.groovy.GroovyExtension] -
 * we already have a defined delimiter, so users don't have to provide it.
 * By having the java, kotlin, and groovy formats implement this interface,
 * you can write generic code for enforcing whitespace and licenses.
 */
internal interface HasBuiltinDelimiterForLicense {

    /**
     * Content that should be at the top of every file.
     */
    val licenseHeader: FormatExtension.LicenseHeaderConfig?

    fun applyTo(license: HasBuiltinDelimiterForLicense) {
        licenseHeader?.let { licenseHeader ->
            licenseHeader.applyTo(
                licenseHeader.header?.let(license::licenseHeader)
                    ?: licenseHeader.headerFile!!.let(license::licenseHeaderFile),
            )
        }
    }
}
