package klib.data

import klib.data.type.validator.Validator
import kotlinx.serialization.descriptors.SerialDescriptor

public interface Property {

    public val name: String
    public val descriptor: SerialDescriptor
    public val isReadOnly: Boolean
    public val validator: Validator?
}
