package gradle.api.tasks.download

import java.net.URI

import org.gradle.internal.impldep.org.apache.ivy.util.url.ApacheURLLister
import org.jetbrains.compose.internal.de.undercouch.gradle.tasks.download.DownloadSpec

internal interface DownloadSpec {

    val src: LinkedHashSet<String>

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
            src.flatMap { src ->
                if (src.endsWith("/")) {
                    val urlLister = ApacheURLLister()
                    urlLister.listFiles(URI(src).toURL())
                }
                else listOf(src)
            },
        )
        receiver.dest(dest)
        quiet?.let(receiver::quiet)
        overwrite?.let(receiver::overwrite)
        onlyIfModified?.let(receiver::onlyIfModified)
        onlyIfNewer?.let(receiver::onlyIfNewer)
        compress?.let(receiver::compress)
        username?.let(receiver::username)
        password?.let(receiver::password)
        headers?.let(receiver::headers)
        preemptiveAuth?.let(receiver::preemptiveAuth)
        acceptAnyCertificate?.let(receiver::acceptAnyCertificate)
        connectTimeout?.let(receiver::connectTimeout)
        readTimeout?.let(receiver::readTimeout)
        retries?.let(receiver::retries)
        downloadTaskDir?.let(receiver::downloadTaskDir)
        tempAndMove?.let(receiver::tempAndMove)
        useETag?.let(receiver::useETag)
        cachedETagsFile?.let(receiver::cachedETagsFile)
        method?.let(receiver::method)
        body?.let(receiver::body)
    }
}
