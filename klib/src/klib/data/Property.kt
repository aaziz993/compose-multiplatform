package klib.data

import klib.data.validator.Validator
import kotlinx.serialization.descriptors.SerialDescriptor

public interface Property {

    public val name: String
    public val descriptor: SerialDescriptor
    public val immutable: Boolean
    public val validator: Validator?
}
