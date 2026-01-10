package clib.presentation.theme

import com.materialkolor.dynamiccolor.ContrastCurve
import com.materialkolor.scheme.DynamicScheme.Platform

public interface ContrastCurveProvider {

    public fun getCurve(contrastLevel: Double, platform: Platform): ContrastCurve?
}
