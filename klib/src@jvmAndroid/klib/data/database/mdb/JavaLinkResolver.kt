package klib.data.database.mdb

public class JavaLinkResolver(
    private val linkResolver: com.healthmarketscience.jackcess.util.LinkResolver
) : LinkResolver {
    override fun resolveLinkedDatabase(linkerDb: Database, linkeeFileName: String): Database =
        Database(
            linkResolver.resolveLinkedDatabase(linkerDb.database, linkeeFileName)
        )

    public companion object {
        /**
         * default link resolver used if none provided
         * @usage _general_field_
         */
        public val DEFAULT: LinkResolver =
            JavaLinkResolver(com.healthmarketscience.jackcess.util.LinkResolver.DEFAULT)
    }
}

internal fun LinkResolver.toLinkResolver() =
    com.healthmarketscience.jackcess.util.LinkResolver { linkerDb, linkeeFileName ->
        resolveLinkedDatabase(Database(linkerDb), linkeeFileName).database
    }