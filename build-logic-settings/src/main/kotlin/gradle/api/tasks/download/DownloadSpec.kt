package gradle.api.tasks.download

import gradle.reflect.trySet
import java.net.URI
import org.gradle.internal.impldep.org.apache.ivy.util.url.ApacheURLLister
import org.jetbrains.compose.internal.de.undercouch.gradle.tasks.download.DownloadSpec

internal interface DownloadSpec {

    val srcs: LinkedHashSet<String>

    val dest: String

    val quiet: Boolean?

    val overwrite: Boolean?

    val onlyIfModified: Boolean?

    val onlyIfNewer: Boolean?

    val compress: Boolean?

    val username: String?

    val password: String?

    val headers: Map<String, String>?

    val preemptiveAuth: Boolean?

    val acceptAnyCertificate: Boolean?

    val connectTimeout: Int?

    val readTimeout: Int?

    val retries: Int?

    val downloadTaskDir: String?

    val tempAndMove: Boolean?

    val useETag: String?

    val cachedETagsFile: String?

    val method: String?

    val body: String?

    fun applyTo(receiver: DownloadSpec) {
        receiver.src(
            srcs.flatMap { src ->
                if (src.endsWith("/")) {
                    val urlLister = ApacheURLLister()
                    urlLister.listFiles(URI(src).toURL())
                }
                else listOf(src)
            },
        )
        receiver.dest(dest)
        receiver::quiet trySet quiet
        receiver::overwrite trySet overwrite
        receiver::onlyIfModified trySet onlyIfModified
        receiver::onlyIfNewer trySet onlyIfNewer
        receiver::compress trySet compress
        receiver::username trySet username
        receiver::password trySet password
        receiver::headers trySet headers
        receiver::preemptiveAuth trySet preemptiveAuth
        receiver::acceptAnyCertificate trySet acceptAnyCertificate
        receiver::connectTimeout trySet connectTimeout
        receiver::readTimeout trySet readTimeout
        receiver::retries trySet retries
        receiver::downloadTaskDir trySet downloadTaskDir
        receiver::tempAndMove trySet tempAndMove
        receiver::useETag trySet useETag
        receiver::cachedETagsFile trySet cachedETagsFile
        receiver::method trySet method
        receiver::body trySet body
    }
}
