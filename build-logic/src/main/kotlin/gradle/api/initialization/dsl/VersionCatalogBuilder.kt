package gradle.api.initialization.dsl

import gradle.api.initialization.catalogs
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.initialization.Settings
import org.gradle.api.initialization.dsl.VersionCatalogBuilder

@Suppress("UNCHECKED_CAST")
context(settings: Settings)
public fun VersionCatalogBuilder.fromLib(lib: MinimalExternalModuleDependency) {
    from(lib.toString())
    settings.catalogs[name] = VersionCatalog(lib)
}
