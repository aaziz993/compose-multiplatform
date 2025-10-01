package klib.data.db.mdb

import kotlinx.io.RawSource
import kotlinx.io.Sink
import kotlinx.io.IOException

/**
 * Extensions of the Blob interface with additional functionality for working
 * with the OLE content from an access database.  The ole data type in access
 * has a wide range of functionality (including wrappers with nested wrappers
 * with nested filesystems!), and jackcess only supports a small portion of
 * it.  That said, jackcess should support the bulk of the common
 * functionality.
 * <p>
 * The main Blob methods will interact with the <i>entire</i> OLE field data
 * which, in most cases, contains additional wrapper information.  In order to
 * access the ultimate "content" contained within the OLE data, the {@link
 * #getContent} method should be used.  The type of this content may be a
 * variety of formats, so additional sub-interfaces are available to interact
 * with it.  The most specific sub-interface can be determined by the {@link
 * ContentType} of the Content.
 * <p>
 * Once an OleBlob is no longer useful, <i>it should be closed</i> using
 * {@link #free} or {@link #close} methods (after which, the instance will no
 * longer be functional).
 * <p>
 * Note, the OleBlob implementation is read-only (through the interface).  In
 * order to modify blob contents, create a new OleBlob instance using {@link
 * OleBlob.Builder} and write it to the access database.
 * <p>
 * <b>Example for interpreting an existing OLE field:</b>
 * <pre>
 *   OleBlob oleBlob = null;
 *   try {
 *     oleBlob = row.getBlob("MyOleColumn");
 *     Content content = oleBlob.getContent()
 *     if(content.getType() == OleBlob.ContentType.SIMPLE_PACKAGE) {
 *       FileOutputStream out = ...;
 *       ((SimplePackageContent)content).writeTo(out);
 *       out.closee();
 *     }
 *   } finally {
 *     if(oleBlob != null) { oleBlob.close(); }
 *   }
 * </pre>
 * <p>
 * <b>Example for creating new, embedded ole data:</b>
 * <pre>
 *   OleBlob oleBlob = null;
 *   try {
 *     oleBlob = new OleBlob.Builder()
 *       .setSimplePackage(new File("some_data.txt"))
 *       .toBlob();
 *     db.addRow(1, oleBlob);
 *   } finally {
 *     if(oleBlob != null) { oleBlob.close(); }
 *   }
 * </pre>
 * <p>
 * <b>Example for creating new, linked ole data:</b>
 * <pre>
 *   OleBlob oleBlob = null;
 *   try {
 *     oleBlob = new OleBlob.Builder()
 *       .setLink(new File("some_data.txt"))
 *       .toBlob();
 *     db.addRow(1, oleBlob);
 *   } finally {
 *     if(oleBlob != null) { oleBlob.close(); }
 *   }
 * </pre>
 *
 * @author Aziz Atoev
 */

public expect class OleBlob {
    /**
     * Writes the entire raw blob data to the given stream (this is the access
     * db internal format, which includes all wrapper information).
     *
     * @param out stream to which the blob will be written
     */
    @Throws(IOException::class)
    public fun writeTo(out: Sink)

    // @get:Throws(IOException::class)
    public val content: Content
}


public interface Content {
    /**
     * Returns the type of this content.
     */
    public val type: ContentType

    /**
     * Returns the blob which owns this content.
     */
    public val blob: OleBlob
}

/**
 * Intermediate sub-interface for Content which has a nested package.
 */
public interface PackageContent : Content {
    // @get:Throws(IOException::class)
    public val prettyName: String

    // @get:Throws(IOException::class)
    public val className: String

    // @get:Throws(IOException::class)
    public val typeName: String
}

/**
 * Intermediate sub-interface for Content which has embedded content.
 */
public interface EmbeddedContent : Content {
    public val length: Long

    // @get:Throws(IOException::class)
    public val source: RawSource
}

/**
 * Sub-interface for Content which has the [ContentType.LINK] type.
 * The actual content is external to the access database and can be found at
 * [.getLinkPath].
 */
public interface LinkContent : PackageContent {
    public val fileName: String

    public val linkPath: String

    public val filePath: String

    // @get:Throws(IOException::class)
    public val linkSource: RawSource
}

/**
 * Sub-interface for Content which has the [ ][ContentType.SIMPLE_PACKAGE] type.  The actual content is embedded within
 * the access database (but the original file source path can also be found
 * at [.getFilePath]).
 */
public interface SimplePackageContent : PackageContent, EmbeddedContent {
    public val fileName: String

    public val filePath: String

    public val localFilePath: String
}

/**
 * Sub-interface for Content which has the [ ][ContentType.COMPOUND_STORAGE] type.  Compound storage is a complex
 * embedding format also known as OLE2.  In some situations (mostly
 * non-microsoft office file formats) the actual content is available from
 * the [.getContentsEntry] method (if [.hasContentsEntry]
 * returns `true`).  In other situations (e.g. microsoft office file
 * formats), the actual content is most or all of the compound content (but
 * retrieving the final file may be a complex operation beyond the scope of
 * jackcess).  Note that the CompoundContent type will only be available if
 * the POI library is in the classpath, otherwise compound content will be
 * returned as OtherContent.
 */
public interface CompoundContent : PackageContent, EmbeddedContent, Iterable<CompoundContent.Entry> {
    @Throws(IOException::class)
    public fun getEntry(entryName: String): Entry

    @Throws(IOException::class)
    public fun hasContentsEntry(): Boolean

    // @get:Throws(IOException::class)
    public val contentsEntry: Entry

    /**
     * A document entry in the compound storage.
     */
    public interface Entry : EmbeddedContent {
        public val name: String

        /**
         * Returns the CompoundContent which owns this entry.
         */
        public val parent: CompoundContent
    }
}

/**
 * Sub-interface for Content which has the [ContentType.OTHER] type.
 * This may be a simple embedded file or some other, currently not
 * understood complex type.
 */
public interface OtherContent : PackageContent, EmbeddedContent

/** Enum describing the types of blob contents which are currently
 * supported/understood  */
public enum class ContentType {
    /** the blob contents are a link (file path) to some external content.
     * Content will be an instance of LinkContent  */
    LINK,

    /** the blob contents are a simple wrapper around some embedded content
     * and related file names/paths.  Content will be an instance
     * SimplePackageContent  */
    SIMPLE_PACKAGE,

    /** the blob contents are a complex embedded data known as compound
     * storage (aka OLE2).  Working with compound storage requires the
     * optional POI library.  Content will be an instance of CompoundContent.
     * If the POI library is not available on the classpath, then compound
     * storage data will instead be returned as type [.OTHER].  */
    COMPOUND_STORAGE,

    /** the top-level blob wrapper is understood, but the nested blob contents
     * are unknown, probably just some embedded content.  Content will be an
     * instance of OtherContent  */
    OTHER,

    /** the top-level blob wrapper is not understood (this may not be a valid
     * ole instance).  Content will simply be an instance of Content (the
     * data can be accessed from the main blob instance)  */
    UNKNOWN
}
