@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.data.filesystem

import js.date.Date
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsAny
import kotlin.js.JsModule
import kotlin.js.JsNumber

@Suppress("ClassName")
@JsModule("fs")
public external object fs {

    public fun realpathSync(path: String): String
    public fun symlinkSync(target: String, path: String)

    public fun readlinkSync(path: String): String

    public fun lstatSync(path: String): Stats
}

public external interface StatsBase<T : JsAny> {

    public fun isFile(): Boolean
    public fun isDirectory(): Boolean
    public fun isBlockDevice(): Boolean
    public fun isCharacterDevice(): Boolean
    public fun isSymbolicLink(): Boolean
    public fun isFIFO(): Boolean
    public fun isSocket(): Boolean
    public var dev: T
    public var ino: T
    public var mode: T
    public var nlink: T
    public var uid: T
    public var gid: T
    public var rdev: T
    public var size: T
    public var blksize: T
    public var blocks: T
    public var atimeMs: T
    public var mtimeMs: T
    public var ctimeMs: T
    public var birthtimeMs: T
    public var atime: Date
    public var mtime: Date
    public var ctime: Date
    public var birthtime: Date
}

public open external class Stats : StatsBase<JsNumber> {

    override fun isFile(): Boolean
    override fun isDirectory(): Boolean
    override fun isBlockDevice(): Boolean
    override fun isCharacterDevice(): Boolean
    override fun isSymbolicLink(): Boolean
    override fun isFIFO(): Boolean
    override fun isSocket(): Boolean
    override var dev: JsNumber
    override var ino: JsNumber
    override var mode: JsNumber
    override var nlink: JsNumber
    override var uid: JsNumber
    override var gid: JsNumber
    override var rdev: JsNumber
    override var size: JsNumber
    override var blksize: JsNumber
    override var blocks: JsNumber
    override var atimeMs: JsNumber
    override var mtimeMs: JsNumber
    override var ctimeMs: JsNumber
    override var birthtimeMs: JsNumber
    override var atime: Date
    override var mtime: Date
    override var ctime: Date
    override var birthtime: Date
}
