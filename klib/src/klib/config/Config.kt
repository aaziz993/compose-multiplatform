package klib.config

import klib.config.auth.AuthConfig
import klib.config.http.client.HttpClientConfig
import klib.config.locale.LocalizationConfig
import klib.data.validator.Validator

public interface Config {

    public val log: LogConfig
    public val localization: LocalizationConfig
    public val validators: Map<String, Map<String, Validator>>
    public val httpClient: HttpClientConfig
    public val auth: AuthConfig
    public val ui: UIConfig
    public val server: ServerConfig
}
