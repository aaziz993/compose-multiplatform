@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.yaml

import com.charleskorn.kaml.YamlNode
import com.charleskorn.kaml.YamlOutput

internal fun YamlOutput.encodeYamlNode(node: YamlNode) =
    encodeSerializableValue(YamlNode.serializer(), node)
