package klib.data.type.serialization

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.model.MetaData
import com.fleeksoft.ksoup.network.parseGetRequest
import com.fleeksoft.ksoup.network.parsePostRequest
import com.fleeksoft.ksoup.network.parseSubmitRequest
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.parseFile
import com.fleeksoft.ksoup.parseSource
import com.fleeksoft.ksoup.parser.Parser
import com.fleeksoft.ksoup.safety.Safelist
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import kotlinx.io.RawSource
import kotlinx.io.Source
import kotlinx.io.files.Path

public val Xml: Parser = Parser.xmlParser()

public typealias Html = Parser

public fun String.isValidHtml(safelist: Safelist = Safelist.relaxed()): Boolean =
    Ksoup.isValid(this, safelist)

public fun String.cleanHtml(
    safelist: Safelist = Safelist.relaxed(),
    baseUri: String = "",
    outputSettings: Document.OutputSettings? = null
): String = Ksoup.clean(
    this,
    safelist,
    baseUri,
    outputSettings,
)

public fun Parser.parse(value: String, baseUri: String = ""): Document =
    Ksoup.parse(value, this, baseUri)

public fun Parser.parse(
    filePath: String,
    baseUri: String = filePath,
    charsetName: String? = null,
): Document =
    Ksoup.parseFile(
        filePath,
        baseUri,
        charsetName,
        this,
    )

public fun Parser.parse(
    path: Path,
    baseUri: String = path.name,
    charsetName: String? = null,
): Document =
    Ksoup.parseFile(
        path,
        baseUri,
        charsetName,
        this,
    )

public fun Parser.parse(
    source: RawSource,
    baseUri: String = "",
    charsetName: String? = null,
): Document = Ksoup.parseSource(
    source,
    baseUri,
    charsetName,
    this,
)

public fun Parser.parse(
    source: Source,
    baseUri: String = "",
    charsetName: String? = null,
): Document =
    Ksoup.parseSource(
        source,
        baseUri,
        charsetName,
        this,
    )

public suspend fun Parser.parseGetRequest(
    url: String,
    httpClient: HttpClient? = null,
    httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
): Document = Ksoup.parseGetRequest(url, this, httpClient, httpRequestBuilder)

public suspend fun Parser.parseSubmitRequest(
    url: String,
    params: Map<String, String> = emptyMap(),
    httpClient: HttpClient? = null,
    httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
): Document = Ksoup.parseSubmitRequest(url, params, this, httpClient, httpRequestBuilder)

public suspend fun Parser.parsePostRequest(
    url: String,
    httpRequestBuilder: HttpRequestBuilder.() -> Unit = {},
): Document = Ksoup.parsePostRequest(url, httpRequestBuilder, this)

public fun Element.metadata(): MetaData = Ksoup.parseMetaData(this)

public fun String.decodeMetadata(baseUri: String = "", interceptor: ((Element, MetaData) -> Unit)? = null): MetaData =
    Ksoup.parseMetaData(this, baseUri, interceptor)

public fun String.decodeXml(baseUri: String = ""): Document = Ksoup.parseXml(this, baseUri)

public fun String.decodeHtml(baseUri: String = ""): Document = Ksoup.parse(this, baseUri)
