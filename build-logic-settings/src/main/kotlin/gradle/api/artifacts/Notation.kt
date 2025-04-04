package gradle.api.artifacts

internal abstract class Notation {

    abstract val version: String?

    protected abstract val _notation: String?
}
