package klib.data.type

import com.ashampoo.xmp.XMPMeta
import com.ashampoo.xmp.XMPMetaFactory
import com.ashampoo.xmp.options.ParseOptions
import com.ashampoo.xmp.options.SerializeOptions

public fun String.parseXMP(options: ParseOptions? = null): XMPMeta = XMPMetaFactory.parseFromString(this, options)

public fun XMPMeta.serialize(options: SerializeOptions? = null): String = XMPMetaFactory.serializeToString(this, options)
