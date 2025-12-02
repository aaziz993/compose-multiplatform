package klib.data.config

import klib.data.config.di.KoinConfig
import klib.data.config.http.client.HttpClientConfig
import klib.data.config.validator.ValidatorConfig

public interface Config {

    public val log: LogConfig
    public val koin: KoinConfig
    public val localization: Localization
    public val validator: ValidatorConfig
    public val httpClient: HttpClientConfig
    public val ui: UIConfig
    public val server: ServerConfig
}
