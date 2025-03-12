package plugins.project

import gradle.accessors.projectProperties
import gradle.api.trySetSystemProperty
import gradle.api.version
import gradle.plugins.project.PROJECT_PROPERTIES_FILE
import gradle.plugins.project.ProjectProperties.Companion.load
import gradle.plugins.project.ProjectProperties.Companion.yaml
import gradle.serialization.encodeToAny
import javax.xml.stream.XMLEventFactory
import javax.xml.stream.XMLInputFactory
import javax.xml.stream.XMLOutputFactory
import kotlinx.serialization.json.Json
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import plugins.android.AndroidPlugin
import plugins.apple.ApplePlugin
import plugins.cmp.CMPPlugin
import plugins.gradle.animalsniffer.AnimalSnifferPlugin
import plugins.gradle.apivalidation.ApiValidationPlugin
import plugins.gradle.buildconfig.BuildConfigPlugin
import plugins.gradle.doctor.DoctorPlugin
import plugins.gradle.dokka.DokkaPlugin
import plugins.gradle.knit.KnitPlugin
import plugins.gradle.kover.KoverPlugin
import plugins.gradle.publish.PublishPlugin
import plugins.gradle.shadow.ShadowPlugin
import plugins.gradle.sonar.SonarPlugin
import plugins.gradle.spotless.SpotlessPlugin
import plugins.java.JavaPlugin
import plugins.kmp.KMPPlugin
import plugins.kotlin.allopen.AllOpenPlugin
import plugins.kotlin.apollo.ApolloPlugin
import plugins.kotlin.atomicfu.AtomicFUPlugin
import plugins.kotlin.ksp.KspPlugin
import plugins.kotlin.ktorfit.KtorfitPlugin
import plugins.kotlin.noarg.NoArgPlugin
import plugins.kotlin.powerassert.PowerAssertPlugin
import plugins.kotlin.room.RoomPlugin
import plugins.kotlin.rpc.RpcPlugin
import plugins.kotlin.serialization.SerializationPlugin
import plugins.kotlin.sqldelight.SqlDelightPlugin
import plugins.nat.NativePlugin
import plugins.initialization.problemreporter.SLF4JProblemReporterContext
import plugins.web.JsPlugin
import plugins.web.WasmPlugin
import plugins.web.WasmWasiPlugin

public class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project): Unit = with(SLF4JProblemReporterContext()) {
        with(target) {
            // Load and apply project.yaml to build.gradle.kts properties.
            projectProperties = layout.projectDirectory.load().also { properties ->
                println("Load and apply $PROJECT_PROPERTIES_FILE to: $name")
                println(yaml.dump(Json.Default.encodeToAny(properties)))
            }

            if (projectProperties.kotlin.targets.isNotEmpty()) {
                projectProperties.group?.let(::setGroup)
                projectProperties.description?.let(::setDescription)
                project.version = version()
            }

            projectProperties.buildscript?.applyTo()

            //  Don't change order!
            project.plugins.apply(DoctorPlugin::class.java)
            project.plugins.apply(BuildConfigPlugin::class.java)
            project.plugins.apply(SpotlessPlugin::class.java)
            project.plugins.apply(KoverPlugin::class.java)
            project.plugins.apply(SonarPlugin::class.java)
            project.plugins.apply(KnitPlugin::class.java)
            project.plugins.apply(DokkaPlugin::class.java)
            project.plugins.apply(ShadowPlugin::class.java)
            project.plugins.apply(ApiValidationPlugin::class.java)
            project.plugins.apply(PublishPlugin::class.java)
            project.plugins.apply(AllOpenPlugin::class.java)
            project.plugins.apply(NoArgPlugin::class.java)
            project.plugins.apply(AtomicFUPlugin::class.java)
            project.plugins.apply(SerializationPlugin::class.java)
            project.plugins.apply(SqlDelightPlugin::class.java)
            project.plugins.apply(RoomPlugin::class.java)
            project.plugins.apply(RpcPlugin::class.java)
            project.plugins.apply(KtorfitPlugin::class.java)
            project.plugins.apply(ApolloPlugin::class.java)
            project.plugins.apply(PowerAssertPlugin::class.java)
            project.plugins.apply(JavaPlugin::class.java)
            project.plugins.apply(AndroidPlugin::class.java) // apply and configure android library or application plugin.
            project.plugins.apply(AnimalSnifferPlugin::class.java)
            project.plugins.apply(KMPPlugin::class.java) // need android library or application plugin applied.
            project.plugins.apply(KspPlugin::class.java) // kspCommonMainMetadata need kmp plugin applied.
            project.plugins.apply(NativePlugin::class.java)
            project.plugins.apply(ApplePlugin::class.java)
            project.plugins.apply(JsPlugin::class.java)
            project.plugins.apply(WasmPlugin::class.java)
            project.plugins.apply(WasmWasiPlugin::class.java)
            project.plugins.apply(CMPPlugin::class.java)

            projectProperties.nodeJsEnv.applyTo()
            projectProperties.yarn.applyTo()
            projectProperties.npm.applyTo()

            projectProperties.dependencies?.forEach { dependency ->
                dependencies {
                    dependency.applyTo(this)
                }
            }

            projectProperties.tasks?.forEach { task ->
                task.applyTo()
            }

            if (problemReporter.getErrors().isNotEmpty()) {
                throw GradleException(problemReporter.getGradleError())
            }

            afterEvaluate {
                // W/A for XML factories mess within apple plugin classpath.
                val hasAndroidPlugin = plugins.hasPlugin("com.android.application") ||
                    plugins.hasPlugin("com.android.library")
                if (hasAndroidPlugin) {
                    adjustXmlFactories()
                }
            }
        }
    }

    /**
     * W/A for service loading conflict between apple plugin
     * and android plugin.
     */
    private fun adjustXmlFactories() {
        trySetSystemProperty(
            XMLInputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLInputFactoryImpl",
        )
        trySetSystemProperty(
            XMLOutputFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.XMLOutputFactoryImpl",
        )
        trySetSystemProperty(
            XMLEventFactory::class.qualifiedName!!,
            "com.sun.xml.internal.stream.events.XMLEventFactoryImpl",
        )
    }
}
