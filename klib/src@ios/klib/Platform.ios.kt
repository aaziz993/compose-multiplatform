package klib

import platform.UIKit.UIDevice

public actual fun getPlatform(): Platform =
    IosPlatform(UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion)
