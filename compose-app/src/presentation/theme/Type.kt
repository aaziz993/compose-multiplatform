package presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

//    val bonaNovaSC = FontFamily(
//        Font(Res.font.BonaNovaSC_Bold, FontWeight.Bold),
//        Font(Res.font.RobotoFlex_VariableFont, FontWeight.Normal),
//    )
//
//    val dynaPuff = FontFamily(
//        Font(Res.font.DynaPuff_Bold, FontWeight.Bold),
//        Font(Res.font.DynaPuff_Regular, FontWeight.Normal),
//        Font(Res.font.DynaPuff_Medium, FontWeight.Medium),
//        Font(Res.font.DynaPuff_SemiBold, FontWeight.SemiBold),
//    )

// Set of Material typography styles to start with
public val Typography: Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp,
    ),
//    displaySmall = TextStyle(
//        fontFamily = dynaPuff,
//        fontWeight = FontWeight.Medium,
//        fontSize = 14.sp,
//    ),
)
