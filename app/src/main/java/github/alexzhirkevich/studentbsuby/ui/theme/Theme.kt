package github.alexzhirkevich.studentbsuby.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf

enum class Theme
{
    Light,
    Dark,
    System
}

val LocalThemeSelector = staticCompositionLocalOf<ThemeSelector> {
    error("Theme is unset")
}