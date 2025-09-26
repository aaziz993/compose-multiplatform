package gradle.plugins.compose.apple

public interface Asset {

    public val filename: String?
    public val idiom: String
    public val size: String
    public val scale: String
}
