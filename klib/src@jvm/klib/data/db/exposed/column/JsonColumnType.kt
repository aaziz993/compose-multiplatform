package klib.data.db.exposed.column

import klib.data.type.serialization.json.decodeFromAny
import klib.data.type.serialization.json.encodeAnyToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.v1.core.Table
import org.jetbrains.exposed.v1.core.json.json

public inline fun <reified T : Any> Table.anyJson(name: String) =
    json<T>(name, Json.Default::encodeAnyToString, Json.Default::decodeFromAny)
