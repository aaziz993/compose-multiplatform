package klib.data.db.crud.model.entity

public interface UpdatedAt<T : Comparable<T>> {

    public val updatedAt: T?
}
