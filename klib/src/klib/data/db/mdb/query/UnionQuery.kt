package klib.data.db.mdb.query

/**
 * Query interface which represents a UNION query, e.g.:
 * `SELECT <query1> UNION SELECT <query2>`
 *
 * @author Aziz Atoev
 */
public interface UnionQuery : Query {
    public val unionType: String

    public val unionString1: String

    public val unionString2: String

    public val orderings: List<String>
}
