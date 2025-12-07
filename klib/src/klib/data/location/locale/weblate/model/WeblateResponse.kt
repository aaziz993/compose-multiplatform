package klib.data.location.locale.weblate.model

public interface WeblateResponse<T> {

    public val count: Int
    public val next: String?
    public val previous: String?
    public val results: Set<T>
}
