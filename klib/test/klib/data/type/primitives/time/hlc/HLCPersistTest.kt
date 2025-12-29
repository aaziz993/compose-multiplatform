package klib.data.type.primitives.time.hlc

import klib.data.fs.path.div
import klib.data.fs.path.toPath
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.uuid.Uuid
import kotlinx.io.buffered
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

class HLCPersistTest {

    @Test
    fun `can store the hlc into a file at a given path`() {
        val fileSystem = SystemFileSystem

        val epochMillis = 943920000000L
        val counter = 15
        val node = Uuid.random()

        val clock = HybridLogicalClock(Timestamp(epochMillis), NodeID.mint(node), counter)
        val path = "/Users/alice".toPath()
        val filename = "test.hlc"

        HybridLogicalClock.store(clock, path, fileSystem, filename)

        val expectedEncoded = "${epochMillis.toString().padStart(15, '0')}:${counter.toString(36).padStart(5, '0')}:${node.toString().replace("-", "").takeLast(16)}"
        val result = fileSystem.source(path / filename).use { source ->
            source.buffered().readString()
        }

        assertEquals(expectedEncoded, result)
    }

    @Test
    fun `can load a hlc from a given path`() {
        val fileSystem = SystemFileSystem
        val path = "/Users/alice".toPath()
        fileSystem.createDirectories(path)
        val filename = "test.hlc"

        val epochMillis = 943920000000L
        val counter = 15
        val node = Uuid.random().toString().replace("-", "").takeLast(16)

        val encoded = "${epochMillis.toString().padStart(15, '0')}:${counter.toString(36).padStart(5, '0')}:$node"

        fileSystem.sink(path / filename).use { sink ->
            val bufferedSink = sink.buffered()
            bufferedSink.writeString(encoded)
            bufferedSink.flush()
        }

        val result = HybridLogicalClock.load(path, fileSystem, filename)

        assertNotNull(result)
        assertEquals(result.timestamp.epochMillis, epochMillis)
        assertEquals(result.counter, counter)
        assertEquals(result.node.identifier, node)
    }

    @Test
    fun `can store and load a hlc to and from a given path`() {
        val fileSystem = SystemFileSystem
        val path = "/Users/alice".toPath()
        fileSystem.createDirectories(path)
        val filename = "test.hlc"

        val epochMillis = 943920000000L
        val counter = 15
        val node = Uuid.random()

        val clock = HybridLogicalClock(Timestamp(epochMillis), NodeID.mint(node), counter)
        HybridLogicalClock.store(clock, path, fileSystem, filename)
        val result = HybridLogicalClock.load(path, fileSystem, filename)

        assertNotNull(result)
        assertEquals(result.timestamp.epochMillis, epochMillis)
        assertEquals(result.counter, counter)
        assertEquals(result.node.identifier, NodeID.mint(node).identifier)
    }
}
