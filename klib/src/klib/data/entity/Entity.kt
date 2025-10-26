package klib.data.entity

public interface Entity<ID : Comparable<ID>> {

    public val id: ID?
}
