package gradle.plugins.spotless

import com.diffplug.gradle.spotless.BaseKotlinExtension
import gradle.accessors.libs
import gradle.accessors.settings
import gradle.accessors.version
import gradle.accessors.versions
import org.gradle.api.Project

internal abstract class BaseKotlinExtension : FormatExtension {

    abstract val diktat: DiktatConfig?

    /** Uses the <a href="https://github.com/facebookincubator/ktfmt">ktfmt</a> jar to format source code. */
    abstract val ktfmt: List<KtfmtConfig>?

    abstract val ktlint: KtlintConfig?

    context(Project)
    override fun applyTo(extension: com.diffplug.gradle.spotless.FormatExtension) {
        super.applyTo(extension)

        extension as BaseKotlinExtension

        diktat?.let { diktat ->
            diktat.applyTo(
                (diktat.version?.resolveVersion() ?: settings.libs.versions.version("diktat"))
                    ?.let(extension::diktat) ?: extension.diktat(),
            )
        }

        ktfmt?.forEach { ktfmt ->
            ktfmt.applyTo(
                    (ktfmt.version?.resolveVersion() ?: settings.libs.versions.version("ktfmt"))
                            ?.let(extension::ktfmt) ?: extension.ktfmt(),
            )
        }

        ktlint?.let { ktlint ->
            ktlint.applyTo(
                    (ktlint.version?.resolveVersion() ?: settings.libs.versions.version("ktlint"))
                            ?.let(extension::ktlint) ?: extension.ktlint(),
            )
        }
    }
}
