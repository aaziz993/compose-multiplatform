//package klib.data.type.serialization.serializers.primitive//package klib.data.type.serialization.serializers.primitive
//
//import diglol.encoding.decodeBase64ToBytes
//import diglol.encoding.encodeBase64ToString
//
//public class Base64Serializer : PrimitiveStringSerializer<String>(
//    "diglol.encoding.Base64", { it.encodeToByteArray().encodeBase64ToString() },
//    { it.decodeBase64ToBytes()!!.decodeToString() },
//)
