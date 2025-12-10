package klib.data.config

import klib.data.config.auth.AuthConfig
import klib.data.config.http.client.HttpClientConfig
import klib.data.config.locale.Localization
import klib.data.validator.Validator

public interface Config {

    public val log: LogConfig
    public val localization: Localization
    public val validator: Map<String, Map<String, Validator>>
    public val httpClient: HttpClientConfig
    public val auth: AuthConfig
    public val ui: UIConfig
    public val server: ServerConfig
}
