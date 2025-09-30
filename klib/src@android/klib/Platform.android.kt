package klib

import android.os.Build

public actual fun getPlatform(): Platform = AndroidPlatform("Android ${Build.VERSION.SDK_INT}")
