package gradle.plugins.kmp.nat.tasks

abstract class AbstractKotlinCompileTool @Inject constructor: DefaultTask(),
    KotlinCompileTool,
    KotlinCompilerArgumentsProducer,
    DeprecatedCompilerArgumentAware<T>,
    TaskWithLocalState {

    @Internal
    protected val sourceFileFilter = PatternSet()

    init {
        sourceFileFilter.include(
            DEFAULT_KOTLIN_SOURCE_FILES_EXTENSIONS.flatMap { ext -> ext.fileExtensionCasePermutations().map { "**/*.$it" } }
        )
    }

    @get:Internal
    internal val sourceFiles = objectFactory.fileCollection()

    override val sources: FileCollection = objectFactory.fileCollection()
        .from(
            { sourceFiles.asFileTree.matching(sourceFileFilter) }
        )

    override fun source(vararg sources: Any) {
        sourceFiles.from(sources)
    }

    override fun setSource(vararg sources: Any) {
        sourceFiles.from(sources)
    }

    fun disallowSourceChanges() {
        sourceFiles.disallowChanges()
    }

    @Internal
    final override fun getIncludes(): MutableSet<String> = sourceFileFilter.includes

    @Internal
    final override fun getExcludes(): MutableSet<String> = sourceFileFilter.excludes

    final override fun setIncludes(includes: Iterable<String>): PatternFilterable = also {
        sourceFileFilter.setIncludes(includes)
    }

    final override fun setExcludes(excludes: Iterable<String>): PatternFilterable = also {
        sourceFileFilter.setExcludes(excludes)
    }

    final override fun include(vararg includes: String?): PatternFilterable = also {
        sourceFileFilter.include(*includes)
    }

    final override fun include(includes: Iterable<String>): PatternFilterable = also {
        sourceFileFilter.include(includes)
    }

    final override fun include(includeSpec: Spec<FileTreeElement>): PatternFilterable = also {
        sourceFileFilter.include(includeSpec)
    }

    final override fun include(includeSpec: Closure<*>): PatternFilterable = also {
        sourceFileFilter.include(includeSpec)
    }

    final override fun exclude(vararg excludes: String?): PatternFilterable = also {
        sourceFileFilter.exclude(*excludes)
    }

    final override fun exclude(excludes: Iterable<String>): PatternFilterable = also {
        sourceFileFilter.exclude(excludes)
    }

    final override fun exclude(excludeSpec: Spec<FileTreeElement>): PatternFilterable = also {
        sourceFileFilter.exclude(excludeSpec)
    }

    final override fun exclude(excludeSpec: Closure<*>): PatternFilterable = also {
        sourceFileFilter.exclude(excludeSpec)
    }

    @get:Internal
    final override val metrics: Property<BuildMetricsReporter<GradleBuildTime, GradleBuildPerformanceMetric>> = project.objects
        .property(GradleBuildMetricsReporter())

    /**
     * By default, should be set by plugin from [COMPILER_CLASSPATH_CONFIGURATION_NAME] configuration.
     *
     * Empty classpath will fail the build.
     */
    @get:Classpath
    internal val defaultCompilerClasspath: ConfigurableFileCollection =
        project.objects.fileCollection()

    @get:Internal
    internal abstract val runViaBuildToolsApi: Property<Boolean>

    protected fun validateCompilerClasspath() {
        // Note that the check triggers configuration resolution
        require(!defaultCompilerClasspath.isEmpty) {
            "Default Kotlin compiler classpath is empty! Task: $path (${this::class.qualifiedName})"
        }
    }
}
