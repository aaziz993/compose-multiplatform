package gradle.plugins.web

import gradle.api.repositories.maybeRedirect
import gradle.api.trySet
import org.jetbrains.kotlin.gradle.targets.js.AbstractSettings

internal abstract class AbstractSettings {

    // To prevent Kotlin build from failing (due to `-Werror`), only deprecate after upgrade of bootstrap version
//    @Deprecated("This property has been migrated to support the Provider API. Use downloadBaseUrlProperty instead. This will be removed in version 2.2.")
    abstract val downloadBaseUrl: String?

    // To prevent Kotlin build from failing (due to `-Werror`), only deprecate after upgrade of bootstrap version
//    @Deprecated("This property has been migrated to support the Provider API. Use corresponding spec (extension with name *Spec) instead. This will be removed in version 2.2.")
    abstract val version: String?

    fun applyTo(settings: AbstractSettings<*>) {
        settings::downloadBaseUrl trySet downloadBaseUrl
        settings::version trySet version
    }
}
