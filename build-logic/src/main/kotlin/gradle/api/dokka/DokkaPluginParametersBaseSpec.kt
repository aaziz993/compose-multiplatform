package gradle.api.dokka

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer
import org.jetbrains.dokka.gradle.engine.plugins.DokkaHtmlPluginParameters
import org.jetbrains.dokka.gradle.engine.plugins.DokkaPluginParametersBaseSpec
import org.jetbrains.dokka.gradle.engine.plugins.DokkaVersioningPluginParameters

public val ExtensiblePolymorphicDomainObjectContainer<DokkaPluginParametersBaseSpec>.html: DokkaHtmlPluginParameters
    get() = getByName(DokkaHtmlPluginParameters.DOKKA_HTML_PARAMETERS_NAME) as DokkaHtmlPluginParameters

public val ExtensiblePolymorphicDomainObjectContainer<DokkaPluginParametersBaseSpec>.versioning: DokkaVersioningPluginParameters
    get() = getByName(DokkaVersioningPluginParameters.DOKKA_VERSIONING_PLUGIN_PARAMETERS_NAME) as DokkaVersioningPluginParameters
