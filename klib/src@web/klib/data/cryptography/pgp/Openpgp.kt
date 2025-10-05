@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.data.cryptography.pgp

import js.buffer.ArrayBuffer
import js.date.Date
import js.objects.JsPlainObject
import js.typedarrays.Uint8Array
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsArray
import kotlin.js.JsBoolean
import kotlin.js.JsModule
import kotlin.js.Promise
import kotlin.js.definedExternally

@Suppress("ClassName")
@JsModule("openpgp")
public external object openpgp {

    public external fun generateKey(options: GenerateKeyOptions): Promise<dynamic>

    public external fun readKey(options: ReadKeyOptions): Promise<Key>

    public external fun readKeys(options: ReadKeysOptions): Promise<JsArray<Key>>

    public external fun readPrivateKey(options: ReadKeyOptions): Promise<PrivateKey>

    public external fun readPrivateKeys(options: ReadKeysOptions): Promise<JsArray<PrivateKey>>

    public external fun encryptKey(options: EncryptDecryptKeyOptions): Promise<PrivateKey>

    public external fun decryptKey(options: EncryptDecryptKeyOptions): Promise<PrivateKey>

    public external fun revokeKey(options: RevokeKeyOptions): Promise<dynamic>

    public external fun createMessage(options: CreateMessageOptions): Promise<Message>

    public external fun createCleartextMessage(options: CreateCleartextMessageOptions): Promise<CleartextMessage>

    public external fun encrypt(options: EncryptOptions): Promise<dynamic>

    public external fun readMessage(options: ReadMessageOptions): Promise<Message>

    public external fun decrypt(options: DecryptOptions): Promise<DecryptVerifyMessageResult>

    public external fun sign(options: SignOptions): Promise<JsAny>

    public external fun verify(options: VerifyOptions): Promise<DecryptVerifyMessageResult>

    public external fun readSignature(options: ReadSignatureOptions): Promise<Signature>
}

@JsPlainObject
public external interface UserID : JsAny {

    public var name: String?
    public var email: String?
    public var comment: String?
}

@JsPlainObject
public external interface Config : JsAny {

    public var preferredHashAlgorithm: Int?
    public var preferredSymmetricAlgorithm: Int?
    public var preferredCompressionAlgorithm: Int?
}

@JsPlainObject
public external interface SubkeyOptions : JsAny {

    public var type: String?
    public var curve: String?
    public var rsaBits: Double?
    public var date: Date?
    public var sign: Boolean?
    public var config: Config?
}

@JsPlainObject
public external interface GenerateKeyOptions : JsAny {

    public var userIDs: Array<UserID>?
    public var passphrase: String?
    public var type: String
    public var curve: String?
    public var rsaBits: Double?
    public var keyExpirationTime: Double?
    public var date: Date?
    public var subkeys: Array<SubkeyOptions>?
    public var format: String
    public var config: Config?
}

public open external class Key : JsAny {

    public fun write(): Uint8Array<ArrayBuffer>
    public fun armor(config: Config? = definedExternally): String
    public fun getExpirationTime(
        userID: UserID = definedExternally,
        config: Config = definedExternally,
    ): Promise<JsAny? /* Date | typeof Infinity | null */>

    public fun getUserIDs(): Array<String>
    public fun toPublic(): Key
    public fun getFingerprint(): String
    public fun getCreationTime(): Date
    public fun isPrivate(): Boolean
}

public external class PrivateKey : Key {

    public fun isDecrypted(): Boolean
}

@JsPlainObject
public external interface ReadKeyOptions : JsAny {

    public var armoredKey: String?
    public var binaryKey: Uint8Array<ArrayBuffer>?
}

@JsPlainObject
public external interface ReadKeysOptions : JsAny {

    public var armoredKeys: String?
    public var binaryKeys: Uint8Array<ArrayBuffer>?
}

@JsPlainObject
public external interface EncryptDecryptKeyOptions : JsAny {

    public var privateKey: PrivateKey
    public var passphrase: Array<String>
}

@JsPlainObject
public external interface RevokeKeyOptions : JsAny {

    public var key: PrivateKey
    public var format: String
}

public external interface Message : JsAny

@JsPlainObject
public external interface CreateMessageOptions : JsAny {

    public var text: Any?
    public var binary: Any?
}

public external interface CleartextMessage : JsAny

@JsPlainObject
public external interface CreateCleartextMessageOptions : JsAny {

    public var text: String
}

@JsPlainObject
public external interface EncryptOptions : JsAny {

    public var message: Message
    public var encryptionKeys: Array<Key>?
    public var signingKeys: Array<PrivateKey>?
    public var passwords: Array<String>?
    public var format: String?
}

@JsPlainObject
public external interface ReadMessageOptions : JsAny {

    public var armoredMessage: String
    public var binaryMessage: Any
}

public external class KeyID : JsAny {

    public fun toHex(): String
}

@JsPlainObject
public external interface VerificationResult : JsAny {

    public var keyID: KeyID
    public var verified: Promise<JsBoolean>
    public var signature: Promise<Signature>
}

@JsPlainObject
public external interface DecryptVerifyMessageResult : JsAny {

    public var data: dynamic
    public var signatures: Array<VerificationResult>
}

public external class Signature : JsAny

@JsPlainObject
public external interface DecryptOptions : JsAny {

    public var message: Message
    public var decryptionKeys: Array<PrivateKey>
    public var passwords: Array<String>?
    public var verificationKeys: Array<Key>?
    public var format: String?
    public var signature: Signature?
}

@JsPlainObject
public external interface SignOptions : JsAny {

    public var message: Any
    public var signingKeys: Array<PrivateKey>
    public var format: String?
    public var detached: Boolean?
}

@JsPlainObject
public external interface VerifyOptions : JsAny {

    public var message: Any
    public var verificationKeys: Array<Key>
    public var format: String?
    public var signature: Signature?
}

@JsPlainObject
public external interface ReadSignatureOptions : JsAny {

    public var armoredSignature: String?
    public var binarySignature: Uint8Array<ArrayBuffer>?
}
