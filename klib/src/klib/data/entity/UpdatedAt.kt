package klib.data.entity

import klib.data.entity.annotation.UpdatedAt

public interface UpdatedAt<T : Comparable<T>> {
    @UpdatedAt
    public val updatedAt: T?
}
