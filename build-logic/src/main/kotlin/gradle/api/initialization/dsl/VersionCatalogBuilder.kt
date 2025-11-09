package gradle.api.initialization.dsl

import gradle.api.initialization.catalogs
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder

@Suppress("UNCHECKED_CAST")
context(settings: Settings)
public fun VersionCatalogBuilder.from0(dependencyNotation: Any) {
    from(dependencyNotation)
    settings.catalogs[name] = VersionCatalog(dependencyNotation)
}
