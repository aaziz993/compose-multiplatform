package gradle.api.tasks.download

import gradle.api.tasks.Task
import gradle.collection.SerializableAnyMap
import kotlinx.serialization.Serializable

@Serializable
internal data class Download(
    override val dependsOn: List<String>? = null,
    override val onlyIf: Boolean? = null,
    override val doNotTrackState: String? = null,
    override val notCompatibleWithConfigurationCache: String? = null,
    override val didWork: Boolean? = null,
    override val enabled: Boolean? = null,
    override val properties: SerializableAnyMap? = null,
    override val description: String? = null,
    override val group: String? = null,
    override val mustRunAfter: List<String>? = null,
    override val finalizedBy: List<String>? = null,
    override val shouldRunAfter: List<String>? = null,
    override val name: String = "",
    override val src: List<String>,
    override val dest: String,
    override val quiet: Boolean? = null,
    override val overwrite: Boolean? = null,
    override val onlyIfModified: Boolean? = null,
    override val onlyIfNewer: Boolean? = null,
    override val compress: Boolean? = null,
    override val username: String? = null,
    override val password: String? = null,
    override val headers: Map<String, String>? = null,
    override val preemptiveAuth: Boolean? = null,
    override val acceptAnyCertificate: Boolean? = null,
    override val connectTimeout: Int? = null,
    override val readTimeout: Int? = null,
    override val retries: Int? = null,
    override val downloadTaskDir: String? = null,
    override val tempAndMove: Boolean? = null,
    override val useETag: String? = null,
    override val cachedETagsFile: String? = null,
    override val method: String? = null,
    override val body: String? = null,
) : Task, DownloadSpec
