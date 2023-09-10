package github.alexzhirkevich.studentbsuby.ui.theme

import androidx.compose.runtime.State

interface ThemeSelector
{

    val currentTheme: State<Theme>

    fun setTheme(theme: Theme)
}