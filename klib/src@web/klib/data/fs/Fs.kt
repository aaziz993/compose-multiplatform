@file:OptIn(ExperimentalWasmJsInterop::class)

package klib.data.fs

import js.date.Date
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsModule

@JsModule("fs")
public external object Fs {

    public fun realpathSync(path: String): String
    public fun symlinkSync(target: String, path: String)

    public fun readlinkSync(path: String): String

    public fun lstatSync(path: String): Stats
}

public external interface StatsBase<T> {

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

public open external class Stats : StatsBase<Long> {

    override fun isFile(): Boolean
    override fun isDirectory(): Boolean
    override fun isBlockDevice(): Boolean
    override fun isCharacterDevice(): Boolean
    override fun isSymbolicLink(): Boolean
    override fun isFIFO(): Boolean
    override fun isSocket(): Boolean
    override var dev: Long
    override var ino: Long
    override var mode: Long
    override var nlink: Long
    override var uid: Long
    override var gid: Long
    override var rdev: Long
    override var size: Long
    override var blksize: Long
    override var blocks: Long
    override var atimeMs: Long
    override var mtimeMs: Long
    override var ctimeMs: Long
    override var birthtimeMs: Long
    override var atime: Date
    override var mtime: Date
    override var ctime: Date
    override var birthtime: Date
}
