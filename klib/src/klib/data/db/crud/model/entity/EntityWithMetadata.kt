package klib.data.db.crud.model.entity

public interface EntityWithMetadata<
    ID : Comparable<ID>,
    C : Comparable<C>,
    U : Comparable<U>,
    > : Entity<ID>, CreatedBy, CreatedAt<C>, UpdatedBy, UpdatedAt<U>
