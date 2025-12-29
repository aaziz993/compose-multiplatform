package klib.data.type.primitives.time.hlc

import arrow.core.flatMap
import klib.data.fs.path.div
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow
import kotlin.time.Clock
import kotlinx.io.buffered
import kotlinx.io.files.FileSystem
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.io.writeString

/**
 * Implementation of a HLC [1][2]
 *
 * Largely a rip of Jared Forsyth's impl[3] with some kotlinisms
 *
 * [1]: https://cse.buffalo.edu/tech-reports/2014-04.pdf
 * [2]: https://muratbuffalo.blogspot.com/2014/07/hybrid-logical-clocks.html
 * [3]: https://jaredforsyth.com/posts/hybrid-logical-clocks/
 */
public data class HybridLogicalClock(
    val timestamp: Timestamp = Timestamp.now(Clock.System),
    val node: NodeID = NodeID.mint(),
    val counter: Int = 0,
) : Comparable<HybridLogicalClock> {

    public companion object {

        private const val CLOCK_FILE = "clock.hlc"

        /**
         * This should be called every time a new event is generated locally, the result becomes the events timestamp and the new local time
         */
        public fun localTick(
            local: HybridLogicalClock,
            wallClockTime: Timestamp = Timestamp.now(Clock.System),
            maxClockDrift: Int = 1000 * 60,
        ): Result<HybridLogicalClock> =
            if (wallClockTime.epochMillis > local.timestamp.epochMillis)
                Result.success(local.copy(timestamp = wallClockTime))
            else Result.success(local.copy(counter = local.counter + 1)).flatMap { clock ->
                validate(clock, wallClockTime, maxClockDrift)
            }

        /**
         * This should be called every time a new event is received from a remote node, the result becomes the new local time
         */
        public fun remoteTock(
            local: HybridLogicalClock,
            remote: HybridLogicalClock,
            wallClockTime: Timestamp = Timestamp.now(Clock.System),
            maxClockDrift: Int = 1000 * 60,
        ): Result<HybridLogicalClock> =
            when {
                local.node.identifier == remote.node.identifier ->
                    Result.failure(HLCException.DuplicateNodeException(local.node))

                wallClockTime.epochMillis > local.timestamp.epochMillis &&
                    wallClockTime.epochMillis > remote.timestamp.epochMillis ->
                    Result.success(local.copy(timestamp = wallClockTime, counter = 0))

                local.timestamp.epochMillis == remote.timestamp.epochMillis ->
                    Result.success(local.copy(counter = max(local.counter, remote.counter) + 1))

                local.timestamp.epochMillis > remote.timestamp.epochMillis ->
                    Result.success(local.copy(counter = local.counter + 1))

                else -> Result.success(local.copy(timestamp = remote.timestamp, counter = remote.counter + 1))
            }.flatMap { clock ->
                validate(clock, wallClockTime, maxClockDrift)
            }

        private fun validate(clock: HybridLogicalClock, now: Timestamp, maxClockDrift: Int): Result<HybridLogicalClock> {
            if (clock.counter > 36f.pow(5).toInt())
                return Result.failure(HLCException.CausalityOverflowException)

            if (abs(clock.timestamp.epochMillis - now.epochMillis) > maxClockDrift)
                return Result.failure(HLCException.ClockDriftException(clock.timestamp, now))

            return Result.success(clock)
        }

        public fun encodeToString(hlc: HybridLogicalClock): String =
            with(hlc) {
                "${
                    timestamp.epochMillis.toString().padStart(15, '0')
                }:${counter.toString(36).padStart(5, '0')}:${node.identifier}"
            }

        public fun decodeFromString(encoded: String): Result<HybridLogicalClock> {
            val parts = encoded.split(":")

            if (parts.size < 3) return Result.failure(HLCDecodeException.TimestampDecodeException(encoded))

            val timestamp = parts.firstOrNull()?.let {
                Timestamp(it.toLong())
            } ?: return Result.failure(HLCDecodeException.TimestampDecodeException(encoded))

            val counter = parts.getOrNull(1)?.toInt(36)
                ?: return Result.failure(HLCDecodeException.CounterDecodeException(encoded))
            val node = parts.getOrNull(2)?.let { NodeID(it) }
                ?: return Result.failure(HLCDecodeException.NodeDecodeException(encoded))

            return Result.success(HybridLogicalClock(timestamp, node, counter))
        }

        /**
         * Persists the clock to a disk file at the specified directory path
         *
         * This call is blocking
         *
         * Usage:
         * val directory = "/Users/alice".toPath()
         * HybridLogicalClock.store(hlc, path)
         */
        public fun store(
            hlc: HybridLogicalClock,
            directory: Path,
            fileSystem: FileSystem = SystemFileSystem,
            fileName: String = CLOCK_FILE,
        ) {
            fileSystem.createDirectories(directory)
            val filepath = directory / fileName
            fileSystem.sink(filepath).use { sink ->
                val bufferedSink = sink.buffered()
                bufferedSink.writeString(hlc.toString())
                bufferedSink.flush()
            }
        }

        /**
         * Attempts to load a clock from a disk file at the specified directory path
         *
         * This call is blocking and will return null if no file is found
         *
         * Usage:
         * val directory = "/Users/alice".toPath()
         * val nullableClock = HybridLogicalClock.load(path)
         */
        public fun load(directory: Path, fileSystem: FileSystem = SystemFileSystem, fileName: String = CLOCK_FILE): HybridLogicalClock? {
            val filepath = directory / fileName
            if (!fileSystem.exists(filepath)) return null
            val encoded = fileSystem.source(filepath).use { source -> source.buffered().readString() }
            return decodeFromString(encoded).getOrNull()
        }
    }

    override fun compareTo(other: HybridLogicalClock): Int =
        if (timestamp.epochMillis == other.timestamp.epochMillis) {
            if (counter == other.counter) node.identifier.compareTo(other.node.identifier) else counter - other.counter
        }
        else timestamp.epochMillis.compareTo(other.timestamp.epochMillis)

    override fun toString(): String = encodeToString(this)
}
