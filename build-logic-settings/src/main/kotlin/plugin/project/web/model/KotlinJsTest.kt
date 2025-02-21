package plugin.project.web.model

import kotlinx.serialization.Serializable
import org.jetbrains.kotlin.gradle.tasks.KotlinTest

@Serializable
internal data class KotlinJsTest(
var compilation: KotlinJsIrCompilation,
) : KotlinTest(),
    RequiresNpmDependencies {
    @Transient
    private val nodeJs = project.kotlinNodeJsEnvSpec

    private val nodeExecutable = nodeJs.produceEnv(project.providers).map { it.executable }

    @Input
    var environment = mutableMapOf<String, String>()

    @get:Internal
    var testFramework: KotlinJsTestFramework? = null
        set(value) {
            field = value
            onTestFrameworkCallbacks.all { callback ->
                value?.let { callback.execute(it) }
            }
        }

    private var onTestFrameworkCallbacks = project.objects.domainObjectSet<Action<KotlinJsTestFramework>>()

    fun onTestFrameworkSet(action: Action<KotlinJsTestFramework>) {
        onTestFrameworkCallbacks.add(action)
    }

    @Suppress("unused")
    val testFrameworkSettings: String
        @Input get() = testFramework!!.settingsState

    @PathSensitive(PathSensitivity.ABSOLUTE)
    @InputFile
    @NormalizeLineEndings
    val inputFileProperty: RegularFileProperty = project.newFileProperty()

    @Input
    var debug: Boolean = false

    @Suppress("unused")
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    @get:IgnoreEmptyDirectories
    @get:NormalizeLineEndings
    @get:InputFiles
    val runtimeClasspath: FileCollection by lazy {
        compilation.runtimeDependencyFiles
    }

    @Suppress("unused")
    @get:IgnoreEmptyDirectories
    @get:InputFiles
    @get:NormalizeLineEndings
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    internal val compilationOutputs: FileCollection by lazy {
        compilation.output.allOutputs
    }

    @Suppress("unused")
    @get:Input
    val compilationId: String by lazy {
        compilation.let {
            val target = it.target
            target.project.path + "@" + target.name + ":" + it.compilationName
        }
    }

    @Input
    val nodeJsArgs: MutableList<String> =
        mutableListOf()

    override val requiredNpmDependencies: Set<RequiredKotlinJsDependency>
        @Internal get() = testFramework!!.requiredNpmDependencies

    @Deprecated("Use useMocha instead", ReplaceWith("useMocha()"))
    fun useNodeJs() = useMocha()

    @Deprecated("Use useMocha instead", ReplaceWith("useMocha(body)"))
    fun useNodeJs(body: KotlinMocha.() -> Unit) = useMocha(body)

    @Deprecated("Use useMocha instead", ReplaceWith("useMocha(fn)"))
    fun useNodeJs(fn: Action<KotlinMocha>) {
        useMocha {
            fn.execute(this)
        }
    }

    fun useMocha() = useMocha {}
    fun useMocha(body: KotlinMocha.() -> Unit) = use(KotlinMocha(compilation, path), body)
    fun useMocha(fn: Action<KotlinMocha>) {
        useMocha {
            fn.execute(this)
        }
    }

    fun useKarma() = useKarma {}
    fun useKarma(body: KotlinKarma.() -> Unit) = use(
        KotlinKarma(compilation, { services }, path),
        body
    )

    fun useKarma(fn: Action<KotlinKarma>) {
        useKarma {
            fn.execute(this)
        }
    }

    fun environment(key: String, value: String) {
        this.environment[key] = value
    }

    private inline fun <T : KotlinJsTestFramework> use(runner: T, body: T.() -> Unit): T {
        check(testFramework == null) {
            "testFramework already configured for task ${this.path}"
        }

        val testFramework = runner.also(body)
        this.testFramework = testFramework

        return testFramework
    }

    override fun createTestExecutionSpec(): TCServiceMessagesTestExecutionSpec {
        val forkOptions = DefaultProcessForkOptions(fileResolver)
        forkOptions.workingDir = testFramework!!.workingDir.getFile()
        forkOptions.executable = nodeExecutable.get()

        environment.forEach { (key, value) ->
            forkOptions.environment(key, value)
        }

        return testFramework!!.createTestExecutionSpec(
            task = this,
            forkOptions = forkOptions,
            nodeJsArgs = nodeJsArgs,
            debug = debug
        )
    }
}
