package plugin.project.apple.model

internal interface AppleTarget {

    val bridgingHeader: String?

    val buildConfigurations: List<BuildConfiguration>?

    val buildSettings: Map<AppleBuildSettings, String>?

    val embedFrameworks: Boolean?

    val ipad: Boolean?

    val iphone: Boolean?

    val name: String?

    val productInfo: Map<String, Any>?

    val productModuleName: String?

    val productName: String?

//    public open fun buildConfigurations(configureClosure: groovy.lang.Closure<Any?>): Unit { /* compiled code */ }
//
//    public open fun buildConfigurations(configure: org.gradle.api.NamedDomainObjectContainer<BuildConfiguration>.() -> Unit): Unit { /* compiled code */ }
//
//    public open fun dependencies(configureClosure: groovy.lang.Closure<Any?>): Unit { /* compiled code */ }
//
//    fun dependencies(configure: AppleDependencyHandler.() -> Unit): Unit
}

