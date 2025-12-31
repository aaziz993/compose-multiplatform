package klib.data.database.mdb.query


/**
 * Query interface which represents a DDL query.
 *
 * @author Aziz Atoev
 */
public interface DataDefinitionQuery : Query {
    public val ddLString: String
}
