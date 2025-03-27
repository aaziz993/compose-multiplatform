package gradle.plugins.kotlin.targets.web

import gradle.accessors.kotlin
import gradle.api.applyTo
import gradle.api.publish.maven.MavenPublication
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.Project

@Serializable
@SerialName("js")
internal data class KotlinJsTarget(
    override val name: String? = "js",
    override val withSourcesJar: Boolean? = null,
    override val mavenPublication: MavenPublication? = null,
    override val compilations: LinkedHashSet<@Serializable(with = KotlinJsIrCompilationKeyTransformingSerializer::class) KotlinJsIrCompilation>? = null,
    override val nodejs: KotlinJsNodeDsl? = null,
    override val moduleName: String? = null,
    override val browser: KotlinJsBrowserDsl? = null,
    override val useCommonJs: Boolean? = null,
    override val useEsModules: Boolean? = null,
    override val passAsArgumentToMainFunction: String? = null,
    override val generateTypeScriptDefinitions: Boolean? = null,
    override val compilerOptions: KotlinJsCompilerOptions? = null,
    override val binaries: KotlinJsBinaryContainer? = null,
) : KotlinJsTargetDsl<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl> {

    context(Project)
    @Suppress("UNCHECKED_CAST")
    override fun applyTo() =
        applyTo(
            project.kotlin.targets.matching { target ->
                target::class == org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl::class
            } as NamedDomainObjectCollection<org.jetbrains.kotlin.gradle.targets.js.dsl.KotlinJsTargetDsl>,
        ) { name, action ->
            project.kotlin.js(name, action::execute)
        }
}
