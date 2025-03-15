package gradle.api.tasks.download

import java.net.URI
import org.gradle.internal.impldep.org.apache.ivy.util.url.ApacheURLLister
import org.jetbrains.compose.internal.de.undercouch.gradle.tasks.download.DownloadSpec

internal interface DownloadSpec {

    val src: List<String>

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

    fun applyTo(spec: DownloadSpec) {
        spec.src(
            src.flatMap { src ->
                if (src.endsWith("/")) {
                    val urlLister = ApacheURLLister()
                    urlLister.listFiles(URI(src).toURL())
                }
                else listOf(src)
            },
        )
        spec.dest(dest)
        quiet?.let(spec::quiet)
        overwrite?.let(spec::overwrite)
        onlyIfModified?.let(spec::onlyIfModified)
        onlyIfNewer?.let(spec::onlyIfNewer)
        compress?.let(spec::compress)
        username?.let(spec::username)
        password?.let(spec::password)
        headers?.let(spec::headers)
        preemptiveAuth?.let(spec::preemptiveAuth)
        acceptAnyCertificate?.let(spec::acceptAnyCertificate)
        connectTimeout?.let(spec::connectTimeout)
        readTimeout?.let(spec::readTimeout)
        retries?.let(spec::retries)
        downloadTaskDir?.let(spec::downloadTaskDir)
        tempAndMove?.let(spec::tempAndMove)
        useETag?.let(spec::useETag)
        cachedETagsFile?.let(spec::cachedETagsFile)
        method?.let(spec::method)
        body?.let(spec::body)
    }
}
