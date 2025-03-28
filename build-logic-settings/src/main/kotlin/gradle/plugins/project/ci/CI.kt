package gradle.plugins.project.ci

internal interface CI {

    val dependenciesCheck: Boolean

    val signaturesCheck: Boolean

    val formatCheck: Boolean

    val qualityCheck: Boolean

    val test: Boolean

    val coverageVerify: Boolean

    val docSamplesCheck: Boolean

    val publishToGithubPackages: Boolean

    val publishToSpacePackages: Boolean

    val publishToMavenRepository: Boolean
}
