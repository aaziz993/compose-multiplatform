package klib.data.mouse

public actual fun nativeMouseHandlerForPlatform(): NativeMouseHandler = DummyNativeMouseHandler
