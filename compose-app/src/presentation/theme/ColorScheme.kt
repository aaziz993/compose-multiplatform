package presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

public val lightColorScheme: ColorScheme = lightColorScheme(
    primary = Color(0xFF1A3E8C),       // Яркий синий
    primaryContainer = Color(0xFF3A5FBF), // Светло-синий для элементов
    secondary = Color(0xFF3498DB),     // Голубой акцент
    secondaryContainer = Color(0xFF85C1E9), // Светло-голубой фон для акцентов
    background = Color(0xFFFFFFFF),    // Белый
    surface = Color(0xFFF0F4FF),       // Очень светлый голубой для карточек/панелей
    onPrimary = Color(0xFFFFFFFF),     // Белый текст на синем
    onSecondary = Color(0xFFFFFFFF),   // Белый текст на голубом
    onBackground = Color(0xFF1A3E8C),  // Темно-синий текст на белом фоне
    onSurface = Color(0xFF1A3E8C),     // Темно-синий текст на светлом
    error = Color(0xFFE74C3C),         // Красный для ошибок
    onError = Color(0xFFFFFFFF),       // Белый текст на красном
)

public val darkColorScheme: ColorScheme = darkColorScheme(
    primary = Color(0xFF3A5FBF),       // Яркий синий для элементов
    primaryContainer = Color(0xFF1A3E8C), // Темно-синий фон
    secondary = Color(0xFF3498DB),     // Голубой акцент
    secondaryContainer = Color(0xFF2A5C90), // Тёмный голубой для панелей
    background = Color(0xFF0D1B3A),    // Очень тёмный синий
    surface = Color(0xFF1A2A5B),       // Тёмно-синий для карточек
    onPrimary = Color(0xFFFFFFFF),     // Белый текст на синем
    onSecondary = Color(0xFFFFFFFF),   // Белый текст на голубом
    onBackground = Color(0xFFE0EFFF),  // Светлый текст на тёмном фоне
    onSurface = Color(0xFFE0EFFF),     // Светлый текст на поверхности
    error = Color(0xFFE74C3C),         // Красный для ошибок
    onError = Color(0xFFFFFFFF),       // Белый текст на красном
)
