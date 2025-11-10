package plugins

import io.github.smiley4.ktorswaggerui.config.SwaggerUIConfig
import io.github.smiley4.ktorswaggerui.swaggerUI
import io.ktor.server.routing.Route

public fun Route.swaggerUi(openApiUrl: String, config: SwaggerUIConfig.() -> Unit = {}) = swaggerUI(openApiUrl, config)
