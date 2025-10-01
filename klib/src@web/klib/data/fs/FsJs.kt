@file:OptIn(ExperimentalWasmJsInterop::class)
@file:JsModule("fs")

package klib.data.fs

import js.date.Date
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.JsModule

public external fun closeSync(fd: Number)

public external fun mkdirSync(path: String): String?

public external fun openSync(path: String, flags: String): Double

public external fun opendirSync(path: String): Dir

public external fun readlinkSync(path: String): String

public external fun readSync(fd: Number, buffer: ByteArray, offset: Double, length: Double, position: Double?): Double

public external fun realpathSync(path: String): String

public external fun renameSync(oldPath: String, newPath: String)

public external fun rmdirSync(path: String)

public external fun lstatSync(path: String): Stats

public external fun fstatSync(fd: Number): Stats

public external fun unlinkSync(path: String)

public external fun writeSync(fd: Number, buffer: ByteArray): Double

public external fun writeSync(fd: Number, buffer: ByteArray, offset: Double, length: Double, position: Double): Double

public external fun ftruncateSync(fd: Number, len: Double)

public external fun symlinkSync(target: String, path: String)

public open external class Dir {

    public open var path: String
    public open fun closeSync()

    // Note that dukat's signature of readSync() returns a non-nullable Dirent; that's incorrect.
    public open fun readSync(): Dirent?
}

public open external class Dirent {

    public open fun isFile(): Boolean
    public open fun isDirectory(): Boolean
    public open fun isBlockDevice(): Boolean
    public open fun isCharacterDevice(): Boolean
    public open fun isSymbolicLink(): Boolean
    public open fun isFIFO(): Boolean
    public open fun isSocket(): Boolean
    public open var name: String
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

public open external class Stats : StatsBase<Number> {

    override fun isFile(): Boolean
    override fun isDirectory(): Boolean
    override fun isBlockDevice(): Boolean
    override fun isCharacterDevice(): Boolean
    override fun isSymbolicLink(): Boolean
    override fun isFIFO(): Boolean
    override fun isSocket(): Boolean
    override var dev: Number
    override var ino: Number
    override var mode: Number
    override var nlink: Number
    override var uid: Number
    override var gid: Number
    override var rdev: Number
    override var size: Number
    override var blksize: Number
    override var blocks: Number
    override var atimeMs: Number
    override var mtimeMs: Number
    override var ctimeMs: Number
    override var birthtimeMs: Number
    override var atime: Date
    override var mtime: Date
    override var ctime: Date
    override var birthtime: Date
}
