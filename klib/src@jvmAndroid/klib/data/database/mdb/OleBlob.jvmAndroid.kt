package klib.data.database.mdb

import com.fleeksoft.io.kotlinx.asSource
import klib.data.database.mdb.OleBlob.Companion.CONTENT_TYPE_MAP
import klib.data.type.collections.bimap.biMapOf
import kotlinx.io.IOException
import kotlinx.io.RawSource
import kotlinx.io.Sink
import kotlinx.io.asOutputStream

public actual class OleBlob(public val oleBlob: com.healthmarketscience.jackcess.util.OleBlob) {

    @Throws(IOException::class)
    public actual fun writeTo(out: Sink): Unit = oleBlob.writeTo(out.asOutputStream())

    public actual val content: Content = when (val content = oleBlob.content) {
        is com.healthmarketscience.jackcess.util.OleBlob.LinkContent -> JavaLinkContent(content)
        is com.healthmarketscience.jackcess.util.OleBlob.SimplePackageContent -> JavaSimplePackageContent(content)
        is com.healthmarketscience.jackcess.util.OleBlob.OtherContent -> JavaOtherContent(content)
        is com.healthmarketscience.jackcess.util.OleBlob.PackageContent -> JavaPackageContent(content)
        is com.healthmarketscience.jackcess.util.OleBlob.EmbeddedContent -> JavaEmbeddedContent(content)

        else -> error("Unknown content '$content'")
    }

    public companion object {

        internal val CONTENT_TYPE_MAP = biMapOf(
            com.healthmarketscience.jackcess.util.OleBlob.ContentType.LINK to ContentType.LINK,
            com.healthmarketscience.jackcess.util.OleBlob.ContentType.SIMPLE_PACKAGE to ContentType.SIMPLE_PACKAGE,
            com.healthmarketscience.jackcess.util.OleBlob.ContentType.COMPOUND_STORAGE to ContentType.COMPOUND_STORAGE,
            com.healthmarketscience.jackcess.util.OleBlob.ContentType.OTHER to ContentType.OTHER,
            com.healthmarketscience.jackcess.util.OleBlob.ContentType.UNKNOWN to ContentType.UNKNOWN,
        )
    }
}

public abstract class JavaContent(private val content: com.healthmarketscience.jackcess.util.OleBlob.Content) :
    Content {

    override val type: ContentType
        get() = CONTENT_TYPE_MAP[content.type]!!

    override val blob: OleBlob
        get() = OleBlob(content.blob)
}

public class JavaPackageContent(
    public val packageContent: com.healthmarketscience.jackcess.util.OleBlob.PackageContent
) : JavaContent(packageContent), PackageContent {

    override val prettyName: String
        get() = packageContent.prettyName

    override val className: String
        get() = packageContent.className

    override val typeName: String
        get() = packageContent.typeName
}

public class JavaEmbeddedContent(
    public val embeddedContent: com.healthmarketscience.jackcess.util.OleBlob.EmbeddedContent
) : JavaContent(embeddedContent), EmbeddedContent {

    override val length: Long
        get() = embeddedContent.length()

    override val source: RawSource
        get() = embeddedContent.stream.asSource()
}

public class JavaLinkContent(
    public val linkContent: com.healthmarketscience.jackcess.util.OleBlob.LinkContent
) : LinkContent, PackageContent by JavaPackageContent(linkContent) {

    override val fileName: String
        get() = linkContent.fileName
    override val linkPath: String
        get() = linkContent.linkPath
    override val filePath: String
        get() = linkContent.filePath
    override val linkSource: RawSource
        get() = linkContent.linkStream.asSource()
}

public class JavaSimplePackageContent(
    public val simplePackageContent: com.healthmarketscience.jackcess.util.OleBlob.SimplePackageContent,
) : SimplePackageContent,
    PackageContent by JavaPackageContent(simplePackageContent),
    EmbeddedContent by JavaEmbeddedContent(simplePackageContent) {

    override val type: ContentType
        get() = CONTENT_TYPE_MAP[simplePackageContent.type]!!

    override val blob: OleBlob
        get() = OleBlob(simplePackageContent.blob)

    override val fileName: String
        get() = simplePackageContent.fileName

    override val filePath: String
        get() = simplePackageContent.filePath

    override val localFilePath: String
        get() = simplePackageContent.localFilePath
}

public class JavaCompoundContent(
    public val compoundContent: com.healthmarketscience.jackcess.util.OleBlob.CompoundContent
) : CompoundContent,
    PackageContent by JavaPackageContent(compoundContent),
    EmbeddedContent by JavaEmbeddedContent(compoundContent) {

    override val type: ContentType
        get() = CONTENT_TYPE_MAP[compoundContent.type]!!

    override val blob: OleBlob
        get() = OleBlob(compoundContent.blob)

    override fun getEntry(entryName: String): CompoundContent.Entry =
        JavaEntry(compoundContent.getEntry(entryName))

    override fun hasContentsEntry(): Boolean = compoundContent.hasContentsEntry()

    override val contentsEntry: CompoundContent.Entry = JavaEntry(compoundContent.contentsEntry)

    override fun iterator(): Iterator<CompoundContent.Entry> =
        compoundContent.asSequence().map(::JavaEntry).iterator()

    public inner class JavaEntry(
        public val entry: com.healthmarketscience.jackcess.util.OleBlob.CompoundContent.Entry
    ) : CompoundContent.Entry, EmbeddedContent by JavaEmbeddedContent(entry) {

        override val name: String
            get() = entry.name

        override val parent: CompoundContent
            get() = JavaCompoundContent(entry.parent)
    }
}

public class JavaOtherContent(
    public val otherContent: com.healthmarketscience.jackcess.util.OleBlob.OtherContent,
) : PackageContent by JavaPackageContent(otherContent),
    EmbeddedContent by JavaEmbeddedContent(otherContent) {

    override val type: ContentType
        get() = CONTENT_TYPE_MAP[otherContent.type]!!

    override val blob: OleBlob
        get() = OleBlob(otherContent.blob)
}
