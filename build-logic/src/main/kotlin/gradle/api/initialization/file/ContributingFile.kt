package gradle.api.initialization.file

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@SerialName("ContributingFile")
public data class ContributingFile(
    val source: String = "files/CONTRIBUTING.md",
    override val into: String = "CONTRIBUTING.md",
    override val resolution: FileResolution = FileResolution.NEWER,
    val projectName: String? = null,
    val projectNamePlaceholder: String = "[INSERT PROJECT NAME]",
    val defaultBranch: String? = null,
    val defaultBranchPlaceholder: String = "[INSERT DEFAULT BRANCH]",
    val repositoryURL: String? = null,
    val repositoryUrlPlaceholder: String = "[INSERT REPOSITORY URL]",
    val documentationURL: String? = null,
    val documentationUrlPlaceholder: String = "[INSERT DOCUMENTATION URL]",
    val contactMethod: String? = null,
    val contactMethodPlaceholder: String = "[INSERT CONTACT METHOD]",
) : ProjectFile() {

    @Transient
    override val from: List<String> = listOf(source)

    @Transient
    override val replace: Map<String, String> = listOfNotNull(
        projectName?.let { projectName -> projectNamePlaceholder to projectName },
        defaultBranch?.let { defaultBranch -> defaultBranchPlaceholder to defaultBranch },
        repositoryURL?.let { repositoryURL -> repositoryUrlPlaceholder to repositoryURL },
        documentationURL?.let { documentationURL -> documentationUrlPlaceholder to documentationURL },
        contactMethod?.let { contactMethod -> contactMethodPlaceholder to contactMethod },
    ).toMap()
}
