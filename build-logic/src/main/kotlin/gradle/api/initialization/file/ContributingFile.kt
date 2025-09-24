package gradle.api.initialization.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("ContributingFile")
public data class ContributingFile(
    val source: String,
    override val into: String = "CONTRIBUTING.md",
    override val resolution: FileResolution = FileResolution.NEWER,
    val projectName: String? = null,
    val projectNamePlaceholder: String = "[INSERT PROJECT NAME]",
    val defaultBranch: String = "main",
    val defaultBranchPlaceholder: String = "[INSERT DEFAULT BRANCH]",
    val repositoryURL: String? = null,
    val repositoryUrlPlaceholder: String = "[INSERT REPOSITORY URL]",
    val documentationURL: String? = null,
    val documentationUrlPlaceholder: String = "[INSERT DOCUMENTATION URL]",
    val securityEmail: String? = null,
    val securityEmailPlaceholder: String = "[INSERT EMAIL ADDRESS]",
    override val replace: Map<String, String> = emptyMap()
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)
}
