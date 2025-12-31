package klib.data.database.mdb

import kotlinx.io.IOException

/**
 * Resolver for linked databases.
 *
 * @author Aziz Atoev
 * @usage _intermediate_class_
 */
public fun interface LinkResolver {
    /**
     * Returns the appropriate Database instance for the linkeeFileName from the
     * given linkerDb.
     */
    @Throws(IOException::class)
    public fun resolveLinkedDatabase(linkerDb: Database, linkeeFileName: String): Database
}
