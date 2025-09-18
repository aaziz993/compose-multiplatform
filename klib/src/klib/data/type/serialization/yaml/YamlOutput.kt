@file:Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")

package klib.data.type.serialization.yaml

import com.charleskorn.kaml.YamlOutput
import com.charleskorn.kaml.YamlNode

internal fun YamlOutput.encodeYamlNode(node: YamlNode) =
    encodeSerializableValue(YamlNode.serializer(), node)