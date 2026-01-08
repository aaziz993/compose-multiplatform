package klib.config

import klib.auth.model.AuthResource

public interface RouteConfig {

    public val basePaths: List<String>
    public val additionalBasePaths: List<String>
    public val authResource: AuthResource?
}
