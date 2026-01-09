package clib.presentation.theme

import com.materialkolor.dynamiccolor.ContrastCurve
import com.materialkolor.scheme.DynamicScheme.Platform

public interface Contrast {

    public fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve?
}

public object PrimaryContainer : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE && contrastLevel > 0) getContrastCurve(1.5)
        else null
}

public object OnPrimaryContainer : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(6.0)
        else getContrastCurve(7.0)
}

public object InversePrimary : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(6.0)
        else getContrastCurve(7.0)
}

public object Secondary : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(4.5)
        else getContrastCurve(7.0)
}

public object SecondaryDim : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(4.5)
}

public object OnSecondary : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(6.0)
        else getContrastCurve(7.0)
}

public object SecondaryContainer : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE && contrastLevel > 0) getContrastCurve(1.5)
        else null
}

public object OnSecondaryContainer : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(6.0)
        else getContrastCurve(7.0)
}

public object Tertiary : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(4.5)
        else getContrastCurve(7.0)
}

public object TertiaryDim : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(4.5)
}

public object OnTertiary : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(6.0)
        else getContrastCurve(7.0)
}

public object TertiaryContainer : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE && contrastLevel > 0) getContrastCurve(1.5)
        else null
}

public object OnTertiaryContainer : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(6.0)
        else getContrastCurve(7.0)
}

public object Error : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(4.5)
        else getContrastCurve(7.0)
}

public object ErrorDim : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(4.5)
}

public object OnError : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(6.0)
        else getContrastCurve(7.0)
}

public object ErrorContainer : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE && contrastLevel > 0) getContrastCurve(1.5)
        else null
}

public object OnErrorContainer : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform === Platform.PHONE) getContrastCurve(4.5)
        else getContrastCurve(7.0)
}

public object PrimaryFixed : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform == Platform.PHONE && contrastLevel > 0) getContrastCurve(1.5) else null
}

public object PrimaryFixedDim : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? = null
}

public object OnPrimaryFixed : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(7.0)
}

public object OnPrimaryFixedVariant : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(4.5)
}

public object SecondaryFixed : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform == Platform.PHONE && contrastLevel > 0) getContrastCurve(1.5) else null
}

public object SecondaryFixedDim : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? = null
}

public object OnSecondaryFixed : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(7.0)
}

public object OnSecondaryFixedVariant : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(4.5)
}

public object TertiaryFixed : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        if (platform == Platform.PHONE && contrastLevel > 0) getContrastCurve(1.5) else null
}

public object TertiaryFixedDim : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? = null
}

public object OnTertiaryFixed : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(7.0)
}

public object OnTertiaryFixedVariant : Contrast {

    override fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve? =
        getContrastCurve(4.5)
}

private fun getContrastCurve(defaultContrast: Double): ContrastCurve =
    when (defaultContrast) {
        1.5 -> ContrastCurve(1.5, 1.5, 3.0, 4.5)
        3.0 -> ContrastCurve(3.0, 3.0, 4.5, 7.0)
        4.5 -> ContrastCurve(4.5, 4.5, 7.0, 11.0)
        6.0 -> ContrastCurve(6.0, 6.0, 7.0, 11.0)
        7.0 -> ContrastCurve(7.0, 7.0, 11.0, 21.0)
        9.0 -> ContrastCurve(9.0, 9.0, 11.0, 21.0)
        11.0 -> ContrastCurve(11.0, 11.0, 21.0, 21.0)
        21.0 -> ContrastCurve(21.0, 21.0, 21.0, 21.0)
        else -> ContrastCurve(defaultContrast, defaultContrast, 7.0, 21.0)
    }
