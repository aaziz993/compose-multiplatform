package klib.data.config

import klib.data.config.client.HttpClientConfig
import klib.data.config.client.UIConfig
import klib.data.config.server.ServerConfig
import klib.data.config.validator.ValidatorConfig

public interface Config {

    public val log: LogConfig
    public val httpClient: HttpClientConfig

    //    public val consul: ConsulConfig?
    public val validator: ValidatorConfig
    public val ui: UIConfig
    public val server: ServerConfig
}
