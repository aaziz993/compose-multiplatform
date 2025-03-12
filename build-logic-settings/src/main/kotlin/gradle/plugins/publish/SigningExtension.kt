//package gradle.model.gradle.publish
//
//import kotlinx.serialization.Serializable
//import org.gradle.api.artifacts.PublishArtifact
//import org.gradle.plugins.signing.SignOperation
//
///**
// * The global signing configuration for a project.
// */
//@Serializable
//public data class SigningExtension (
//    /**
//     * The configuration that signature artifacts will be placed into.
//     *
//     *
//     * Changing this will not affect any signing already configured.
//     */
//    private var configuration: Configuration?
//
//    private var required: Any? = true
//
//    /**
//     * The provider of signature types.
//     */
//    private var signatureTypes: SignatureTypeProvider
//
//    /**
//     * The provider of signatories.
//     */
//    private var signatories: SignatoryProvider<*>
//
//    /**
//     * Configures the signing settings for the given project.
//     */
//    init {
//        this.project = project
//        this.configuration = this.defaultConfiguration
//        this.signatureTypes = createSignatureTypeProvider()
//        this.signatories = createSignatoryProvider()
//        project.getTasks().withType(Sign::class.java, { spec: SignatureSpec -> this.addSignatureSpecConventions(spec) })
//    }
//
//    public fun getProject(): Project {
//        return project
//    }
//
//    /**
//     * Whether this task should fail if no signatory or signature type are configured at generation time.
//     *
//     * @since 4.0
//     */
//    public fun setRequired(required: Boolean) {
//        this.required = required
//    }
//
//    /**
//     * Whether this task should fail if no signatory or signature type are configured at generation time.
//     *
//     * If `required` is a [Callable], it will be stored and "called" on demand (i.e. when [.isRequired] is called) and the return value will be interpreting according to the Groovy
//     * Truth. For example:
//     *
//     * <pre>
//     * signing {
//     * required = { gradle.taskGraph.hasTask("publish") }
//     * }
//    </pre> *
//     *
//     * Because the task graph is not known until Gradle starts executing, we must use defer the decision. We can do this via using a [Closure] (which is a [Callable]).
//     *
//     * For any other type, the value will be stored and evaluated on demand according to the Groovy Truth.
//     *
//     * <pre>
//     * signing {
//     * required = false
//     * }
//    </pre> *
//     */
//    public fun setRequired(required: Any?) {
//        this.required = required
//    }
//
//    /**
//     * Whether this task should fail if no signatory or signature type are configured at generation time.
//     *
//     *
//     * Defaults to `true`.
//     *
//     * @see .setRequired
//     */
//    @ToBeReplacedByLazyProperty
//    public fun isRequired(): Boolean {
//        return castToBoolean(force(required))
//    }
//
//    protected val defaultConfiguration: Configuration?
//        /**
//         * Provides the configuration that signature artifacts are added to. Called once during construction.
//         */
//        get() {
//            val configurations: RoleBasedConfigurationContainerInternal =
//                (project as ProjectInternal).getConfigurations()
//            val configuration: Configuration? =
//                configurations.findByName(DEFAULT_CONFIGURATION_NAME)
//            return if (configuration != null)
//                configuration
//            else
//                configurations.migratingUnlocked(
//                    DEFAULT_CONFIGURATION_NAME,
//                    ConfigurationRolesForMigration.LEGACY_TO_CONSUMABLE
//                )
//        }
//
//    /**
//     * Provides the signature type provider. Called once during construction.
//     */
//    protected fun createSignatureTypeProvider(): SignatureTypeProvider {
//        return DefaultSignatureTypeProvider()
//    }
//
//    /**
//     * Provides the signatory provider. Called once during construction.
//     */
//    protected fun createSignatoryProvider(): SignatoryProvider<*> {
//        return PgpSignatoryProvider()
//    }
//
//    /**
//     * Configures the signatory provider (delegating to its [configure method][SignatoryProvider.configure]).
//     *
//     * @param closure the signatory provider configuration DSL
//     * @return the configured signatory provider
//     */
//    @Suppress("unused")
//    public fun signatories(closure: Closure<*>?): SignatoryProvider<*> {
//        signatories.configure(this, closure)
//        return signatories
//    }
//
//    val signatory: Signatory
//        /**
//         * The signatory that will be used for signing when an explicit signatory has not been specified.
//         *
//         *
//         * Delegates to the signatory provider's default signatory.
//         */
//        @ToBeReplacedByLazyProperty get() {
//            return signatories.getDefaultSignatory(project)
//        }
//
//    /**
//     * The signatory that will be used for signing when an explicit signatory has not been specified.
//     *
//     *
//     * Delegates to the signatory provider's default signatory.
//     */
//    @ToBeReplacedByLazyProperty
//    public fun getSignatory(): Signatory {
//        return signatories.getDefaultSignatory(project)
//    }
//
//    /**
//     * The signature type that will be used for signing files when an explicit signature type has not been specified.
//     *
//     *
//     * Delegates to the signature type provider's default type.
//     */
//    @ToBeReplacedByLazyProperty
//    public fun getSignatureType(): SignatureType {
//        return signatureTypes.getDefaultType()
//    }
//
//    @Suppress("unused")
//    public fun setSignatureTypes(signatureTypes: SignatureTypeProvider) {
//        this.signatureTypes = signatureTypes
//    }
//
//    @Suppress("unused")
//    @ToBeReplacedByLazyProperty
//    public fun getSignatureTypes(): SignatureTypeProvider {
//        return signatureTypes
//    }
//
//    public fun setSignatories(signatories: SignatoryProvider<*>) {
//        this.signatories = signatories
//    }
//
//    public fun setConfiguration(configuration: Configuration?) {
//        this.configuration = configuration
//    }
//
//    /**
//     * Use GnuPG agent to perform signing work.
//     *
//     * @since 4.5
//     */
//    @Suppress("unused")
//    public fun useGpgCmd() {
//        setSignatories(GnupgSignatoryProvider())
//    }
//
//    /**
//     * Use the supplied ascii-armored in-memory PGP secret key and password
//     * instead of reading it from a keyring.
//     *
//     * <pre>`
//     * signing {
//     * def secretKey = findProperty("mySigningKey")
//     * def password = findProperty("mySigningPassword")
//     * useInMemoryPgpKeys(secretKey, password)
//     * }
//    `</pre> *
//     *
//     * @since 5.4
//     */
//    @Suppress("unused")
//    public fun useInMemoryPgpKeys(@Nullable defaultSecretKey: String?, @Nullable defaultPassword: String?) {
//        setSignatories(InMemoryPgpSignatoryProvider(defaultSecretKey, defaultPassword))
//    }
//
//    /**
//     * Use the supplied ascii-armored in-memory PGP secret key and password
//     * instead of reading it from a keyring.
//     * In case a signing subkey is provided, keyId must be provided as well.
//     *
//     * <pre>`
//     * signing {
//     * def keyId = findProperty("keyId")
//     * def secretKey = findProperty("mySigningKey")
//     * def password = findProperty("mySigningPassword")
//     * useInMemoryPgpKeys(keyId, secretKey, password)
//     * }
//    `</pre> *
//     *
//     * @since 6.0
//     */
//    @Suppress("unused")
//    public fun useInMemoryPgpKeys(
//        @Nullable defaultKeyId: String?,
//        @Nullable defaultSecretKey: String?,
//        @Nullable defaultPassword: String?
//    ) {
//        setSignatories(InMemoryPgpSignatoryProvider(defaultKeyId, defaultSecretKey, defaultPassword))
//    }
//
//    /**
//     * The configuration that signature artifacts are added to.
//     */
//    @ToBeReplacedByLazyProperty
//    public fun getConfiguration(): Configuration? {
//        return configuration
//    }
//
//    /**
//     * Adds conventions to the given spec, using this settings object's default signatory and signature type as the default signatory and signature type for the spec.
//     */
//    protected fun addSignatureSpecConventions(spec: SignatureSpec) {
//        if (spec !is IConventionAware) {
//            throw InvalidUserDataException("Cannot add conventions to signature spec '" + spec + "' as it is not convention aware")
//        }
//
//        val conventionMapping: ConventionMapping = (spec as IConventionAware).getConventionMapping()
//        conventionMapping.map("signatory", { this.signatory })
//        conventionMapping.map("signatureType", { this.getSignatureType() })
//        conventionMapping.map("required", { this.isRequired() })
//    }
//
//    /**
//     * Creates signing tasks that depend on and sign the "archive" produced by the given tasks.
//     *
//     *
//     * The created tasks will be named "sign*&lt;input task name capitalized&gt;*". That is, given a task with the name "jar" the created task will be named "signJar".
//     *
//     * If the task is not
//     * an [org.gradle.api.tasks.bundling.AbstractArchiveTask], an [InvalidUserDataException] will be thrown.
//     *
//     * The signature artifact for the created task is added to the [ ][.getConfiguration].
//     *
//     * @param tasks The tasks whose archives are to be signed
//     * @return the created tasks.
//     */
//    public fun sign(vararg tasks: Task): List<Sign?> {
//        val result: List<Sign?> = ArrayList(tasks.size)
//        for (taskToSign in tasks) {
//            result.add(
//                createSignTaskFor(taskToSign.getName(), Action { task ->
//                    task.setDescription("Signs the archive produced by the '" + taskToSign.getName() + "' task.")
//                    task.sign(taskToSign)
//                })
//            )
//        }
//        return result
//    }
//
//    /**
//     * Creates signing tasks that sign [all artifacts][Configuration.getAllArtifacts] of the given configurations.
//     *
//     *
//     * The created tasks will be named "sign*&lt;configuration name capitalized&gt;*". That is, given a configuration with the name "conf" the created task will be named "signConf".
//     *
//     * The signature artifacts for the created tasks are added to the [configuration][.getConfiguration] for this settings object.
//     *
//     * @param configurations The configurations whose archives are to be signed
//     * @return the created tasks.
//     */
//    public fun sign(vararg configurations: Configuration): List<Sign?> {
//        val result: List<Sign?> = ArrayList(configurations.size)
//        for (configurationToSign in configurations) {
//            result.add(
//                createSignTaskFor(configurationToSign.getName(), Action { task ->
//                    task.setDescription("Signs all artifacts in the '" + configurationToSign.getName() + "' configuration.")
//                    task.sign(configurationToSign)
//                })
//            )
//        }
//        return result
//    }
//
//    /**
//     * Creates signing tasks that sign all publishable artifacts of the given publications.
//     *
//     *
//     * The created tasks will be named "sign*&lt;publication name capitalized&gt;*Publication".
//     * That is, given a publication with the name "mavenJava" the created task will be named "signMavenJavaPublication".
//     *
//     * The signature artifacts for the created tasks are added to the publishable artifacts of the given publications.
//     *
//     * @param publications The publications whose artifacts are to be signed
//     * @return the created tasks.
//     * @since 4.8
//     */
//    public fun sign(vararg publications: Publication?): List<Sign?> {
//        val result: List<Sign?> = ArrayList(publications.size)
//        for (publication in publications) {
//            result.add(createSignTaskFor<PublicationArtifact?>(publication as PublicationInternal<*>?))
//        }
//        return result
//    }
//
//    /**
//     * Creates signing tasks that sign all publishable artifacts of the given publication collection.
//     *
//     *
//     * The created tasks will be named "sign*&lt;publication name capitalized&gt;*Publication".
//     * That is, given a publication with the name "mavenJava" the created task will be named "signMavenJavaPublication".
//     *
//     * The signature artifacts for the created tasks are added to the publishable artifacts of the given publications.
//     *
//     * @param publications The collection of publications whose artifacts are to be signed
//     * @return the created tasks.
//     * @since 4.8
//     */
//    public fun sign(publications: DomainObjectCollection<Publication?>): List<Sign?> {
//        val result: List<Sign?> = ArrayList()
//        publications.all({ publication -> result.add(createSignTaskFor<PublicationArtifact?>(publication as PublicationInternal<*>?)) })
//        publications.whenObjectRemoved({ publication ->
//            val tasks: TaskContainer = project.getTasks()
//            val task: Task = tasks.getByName(determineSignTaskNameForPublication(publication))
//            task.setEnabled(false)
//            result.remove(task)
//        })
//        return result
//    }
//
//    private fun <T : PublicationArtifact?> createSignTaskFor(publicationToSign: PublicationInternal<T?>): Sign {
//        val signTaskName = determineSignTaskNameForPublication(publicationToSign)
//        if (project.getTasks().getNames().contains(signTaskName)) {
//            return project.getTasks().named(signTaskName, Sign::class.java).get()
//        }
//        @Suppress("deprecation") val signTask: Sign =
//            project.getTasks().create(signTaskName, Sign::class.java, { task ->
//                task.setDescription("Signs all artifacts in the '" + publicationToSign.getName() + "' publication.")
//                task.sign(publicationToSign)
//            })
//        val artifacts: Map<Signature?, T?> = HashMap()
//        signTask.getSignatures().all({ signature ->
//            val artifact: T? = publicationToSign.addDerivedArtifact(
//                Cast.uncheckedNonnullCast(signature.getSource()),
//                DefaultDerivedArtifactFile(signature, signTask)
//            )
//            artifact.builtBy(signTask)
//            artifacts.put(signature, artifact)
//        })
//        signTask.getSignatures().whenObjectRemoved({ signature ->
//            val artifact: T? = artifacts.remove(signature)
//            publicationToSign.removeDerivedArtifact(artifact)
//        })
//        return signTask
//    }
//
//    private fun determineSignTaskNameForPublication(publication: Publication): String {
//        return "sign" + capitalize(publication.name) + "Publication"
//    }
//
//    private fun createSignTaskFor(name: CharSequence?, taskConfiguration: Action<Sign?>?): Sign {
//        val signTaskName = "sign" + capitalize(name)
//        if (project.getTasks().getNames().contains(signTaskName)) {
//            return project.getTasks().named(signTaskName, Sign::class.java).get()
//        }
//        @Suppress("deprecation") val signTask: Sign =
//            project.getTasks().create(signTaskName, Sign::class.java, taskConfiguration)
//        addSignaturesToConfiguration(signTask, getConfiguration())
//        return signTask
//    }
//
//    protected fun addSignaturesToConfiguration(task: Sign, configuration: Configuration): Any {
//        task.getSignatures().all({ sig -> configuration.getArtifacts().add(sig) })
//        return task.getSignatures().whenObjectRemoved({ sig -> configuration.getArtifacts().remove(sig) })
//    }
//
//    /**
//     * Digitally signs the publish artifacts, generating signature files alongside them.
//     *
//     *
//     * The project's default signatory and default signature type from the [signing settings][SigningExtension] will be used to generate the signature.
//     * The returned [sign operation][SignOperation] gives access to the created signature files.
//     *
//     * If there is no configured default signatory available, the sign operation will fail.
//     *
//     * @param publishArtifacts The publish artifacts to sign
//     * @return The executed [sign operation][SignOperation]
//     */
//    public fun sign(vararg publishArtifacts: PublishArtifact?): SignOperation {
//        return doSignOperation({ operation -> operation.sign(publishArtifacts) })
//    }
//
//    /**
//     * Digitally signs the files, generating signature files alongside them.
//     *
//     *
//     * The project's default signatory and default signature type from the [signing settings][SigningExtension] will be used to generate the signature.
//     * The returned [sign operation][SignOperation] gives access to the created signature files.
//     *
//     * If there is no configured default signatory available, the sign operation will fail.
//     *
//     * @param files The files to sign.
//     * @return The executed [sign operation][SignOperation].
//     */
//    public fun sign(vararg files: File?): SignOperation {
//        return doSignOperation({ operation -> operation.sign(files) })
//    }
//
//    /**
//     * Digitally signs the files, generating signature files alongside them.
//     *
//     *
//     * The project's default signatory and default signature type from the [signing settings][SigningExtension] will be used to generate the signature.
//     * The returned [sign][SignOperation] gives access to the created signature files.
//     *
//     * If there is no configured default signatory available, the sign operation will fail.
//     *
//     * @param classifier The classifier to assign to the created signature artifacts.
//     * @param files The publish artifacts to sign.
//     * @return The executed [sign operation][SignOperation].
//     */
//    public fun sign(classifier: String?, vararg files: File?): SignOperation {
//        return doSignOperation({ operation -> operation.sign(classifier, files) })
//    }
//
//    /**
//     * Creates a new [sign operation][SignOperation] using the given action to configure it before executing it.
//     *
//     *
//     * The project's default signatory and default signature type from the [signing settings][SigningExtension] will be used to generate the signature.
//     * The returned [sign][SignOperation] gives access to the created signature files.
//     *
//     * If there is no configured default signatory available (and one is not explicitly specified in this operation's configuration), the sign operation will fail.
//     *
//     * @param setup The configuration action of the [sign operation][SignOperation].
//     * @return The executed [sign operation][SignOperation].
//     * @since 7.5
//     */
//    public fun sign(setup: Action<SignOperation?>): SignOperation {
//        return doSignOperation(setup)
//    }
//
//
//
//
//    @ToBeReplacedByLazyProperty
//    public fun getSignatories(): SignatoryProvider<*> {
//        return signatories
//    }
//
//
//
//    private class DefaultDerivedArtifactFile public constructor(signature: Signature, signTask: Sign) :
//        DerivedArtifact {
//        private val signature: Signature
//        private val signTask: Sign
//
//        init {
//            this.signature = signature
//            this.signTask = signTask
//        }
//
//        public override fun create(): File {
//            return signature.getFile()
//        }
//
//        public override fun shouldBePublished(): Boolean {
//            return signTask.isEnabled()
//                    && signTask.getOnlyIf().isSatisfiedBy(signTask)
//        }
//    }
//
//
//)
