package klib.data.net.http

import io.ktor.http.content.MultiPartData
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart

public suspend fun MultiPartData.readParts(): List<PartData> = mutableListOf<PartData>().apply {
    forEachPart(::add)
}

public suspend fun MultiPartData.readFormData(): Map<String?, String> =
    readParts().associate { item ->
        item as PartData.FormItem
        item.name to item.value
    }
