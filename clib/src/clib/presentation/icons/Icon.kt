package clib.presentation.icons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.DotLottie
import io.github.alexzhirkevich.compottie.InternalCompottieApi
import io.github.alexzhirkevich.compottie.LocalLottieCache
import io.github.alexzhirkevich.compottie.LottieCancellationBehavior
import io.github.alexzhirkevich.compottie.LottieClipSpec
import io.github.alexzhirkevich.compottie.LottieComposition
import io.github.alexzhirkevich.compottie.LottieCompositionCache
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.Url
import io.github.alexzhirkevich.compottie.assets.LottieAssetsManager
import io.github.alexzhirkevich.compottie.assets.LottieFontManager
import io.github.alexzhirkevich.compottie.dynamic.LottieDynamicProperties
import io.github.alexzhirkevich.compottie.ioDispatcher
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import kotlin.coroutines.CoroutineContext

@OptIn(InternalCompottieApi::class)
@Suppress("ComposeUnstableReceiver")
@Composable
public fun jsonComposition(
    dir: String,
    name: String,
    readBytes: suspend (path: String) -> ByteArray,
    vararg keys: Any?,
    cache: LottieCompositionCache? = LocalLottieCache.current,
    coroutineContext: CoroutineContext = remember { Compottie.ioDispatcher() },
): LottieComposition? {
    val animFromJsonRes by rememberLottieComposition(
        *keys,
        cache = cache,
        coroutineContext = coroutineContext,
    ) {
        LottieCompositionSpec.JsonString(
            readBytes("files/anim/$dir/$name.json").decodeToString(),
        )
    }
    return animFromJsonRes
}

@OptIn(InternalCompottieApi::class)
@Suppress("ComposeUnstableReceiver")
@Composable
public fun filledJsonComposition(
    name: String,
    readBytes: suspend (path: String) -> ByteArray,
    vararg keys: Any?,
    cache: LottieCompositionCache? = LocalLottieCache.current,
    coroutineContext: CoroutineContext = remember { Compottie.ioDispatcher() },
): LottieComposition? = jsonComposition(
    "filled",
    name,
    readBytes,
    *keys,
    cache = cache,
    coroutineContext = coroutineContext,
)

@OptIn(InternalCompottieApi::class)
@Suppress("ComposeUnstableReceiver")
@Composable
public fun outlinedJsonComposition(
    name: String,
    readBytes: suspend (path: String) -> ByteArray,
    vararg keys: Any?,
    cache: LottieCompositionCache? = LocalLottieCache.current,
    coroutineContext: CoroutineContext = remember { Compottie.ioDispatcher() },
): LottieComposition? = jsonComposition(
    "outlined",
    name,
    readBytes,
    *keys,
    cache = cache,
    coroutineContext = coroutineContext,
)

@OptIn(InternalCompottieApi::class)
@Suppress("ComposeUnstableReceiver")
@Composable
public fun lottieComposition(
    dir: String,
    name: String,
    readBytes: suspend (path: String) -> ByteArray,
    vararg keys: Any?,
    cache: LottieCompositionCache? = LocalLottieCache.current,
    coroutineContext: CoroutineContext = remember { Compottie.ioDispatcher() },
): LottieComposition? {
    val animFromArchiveRes by rememberLottieComposition(
        *keys,
        cache = cache,
        coroutineContext = coroutineContext,
    ) {
        LottieCompositionSpec.DotLottie(
            readBytes("files/anim/$dir/$name.lottie"),
        )
    }
    return animFromArchiveRes
}

@OptIn(InternalCompottieApi::class)
@Suppress("ComposeUnstableReceiver")
@Composable
public fun filledLottieComposition(
    name: String,
    readBytes: suspend (path: String) -> ByteArray,
    vararg keys: Any?,
    cache: LottieCompositionCache? = LocalLottieCache.current,
    coroutineContext: CoroutineContext = remember { Compottie.ioDispatcher() },
): LottieComposition? = lottieComposition(
    "filled",
    name,
    readBytes,
    *keys,
    cache = cache,
    coroutineContext = coroutineContext,
)

@OptIn(InternalCompottieApi::class)
@Suppress("ComposeUnstableReceiver")
@Composable
public fun outlinedLottieComposition(
    name: String,
    readBytes: suspend (path: String) -> ByteArray,
    vararg keys: Any?,
    cache: LottieCompositionCache? = LocalLottieCache.current,
    coroutineContext: CoroutineContext = remember { Compottie.ioDispatcher() },
): LottieComposition? = lottieComposition(
    "outlined",
    name,
    readBytes,
    *keys,
    cache = cache,
    coroutineContext = coroutineContext,
)

@OptIn(InternalCompottieApi::class)
@Suppress("ComposeUnstableReceiver")
@Composable
public fun urlComposition(
    url: String,
    vararg keys: Any?,
    cache: LottieCompositionCache? = LocalLottieCache.current,
    coroutineContext: CoroutineContext = remember { Compottie.ioDispatcher() },
): LottieComposition? {
    val animFromUrl by rememberLottieComposition(
        *keys,
        cache = cache,
        coroutineContext = coroutineContext,
    ) {
        LottieCompositionSpec.Url(url)
    }
    return animFromUrl
}

@Suppress("ComposeParameterOrder")
@Composable
public fun Icon(
    composition: LottieComposition?,
    contentDescription: String? = null,
    modifier: Modifier = Modifier.size(24.dp),
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    assetsManager: LottieAssetsManager? = null,
    fontManager: LottieFontManager? = null,
    dynamicProperties: LottieDynamicProperties? = null,
    isPlaying: Boolean = true,
    restartOnPlay: Boolean = true,
    reverseOnRepeat: Boolean = false,
    applyOpacityToLayers: Boolean = false,
    clipSpec: LottieClipSpec? = null,
    speed: Float = composition?.speed ?: 1f,
    iterations: Int = composition?.iterations ?: 1,
    cancellationBehavior: LottieCancellationBehavior = LottieCancellationBehavior.Immediately,
    useCompositionFrameRate: Boolean = false,
    clipToCompositionBounds: Boolean = true,
    clipTextToBoundingBoxes: Boolean = false,
    enableMergePaths: Boolean = false,
    enableExpressions: Boolean = true,
): Unit = Image(
    rememberLottiePainter(
        composition,
        assetsManager,
        fontManager,
        dynamicProperties,
        isPlaying,
        restartOnPlay,
        reverseOnRepeat,
        applyOpacityToLayers,
        clipSpec,
        speed,
        iterations,
        cancellationBehavior,
        useCompositionFrameRate,
        clipToCompositionBounds,
        clipTextToBoundingBoxes,
        enableMergePaths,
        enableExpressions,
    ),
    contentDescription,
    modifier,
    alignment,
    contentScale,
    alpha,
    colorFilter,
)
