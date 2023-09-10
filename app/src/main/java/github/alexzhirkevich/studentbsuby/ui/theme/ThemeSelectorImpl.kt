package github.alexzhirkevich.studentbsuby.ui.theme

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.preference.PreferenceManager
import github.alexzhirkevich.studentbsuby.ui.theme.Theme.Dark

private const val PREF_SETTINGS_isInSystemTheme = "PREF_SETTINGS_isInSystemTheme"
private const val PREF_SETTINGS_isInDarkThemeForced = "PREF_SETTINGS_isInDarkThemeForced"

class ThemeSelectorImpl private constructor(
    private val preferences: SharedPreferences, initialTheme: Theme
                                           ) : ThemeSelector
{

    override val currentTheme = mutableStateOf(initialTheme)


    override fun setTheme(theme: Theme)
    {
        setInSystemTheme(preferences, theme == Theme.System)
        currentTheme.value = theme

        if (theme == Dark)
        {
            setInDarkTheme(preferences, true)
        }
        if (theme == Theme.Light)
        {
            setInDarkTheme(preferences, false)
        }
    }

    companion object
    {

        fun create(context: Context): ThemeSelectorImpl
        {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val inSystem = isInSystemTheme(prefs)
            val theme = when
            {
                inSystem                                                   -> Theme.System
                prefs.getBoolean(PREF_SETTINGS_isInDarkThemeForced, false) -> Dark
                else                                                       -> Theme.Light
            }
            return ThemeSelectorImpl(prefs, theme)
        }

        private fun isInSystemTheme(prefs: SharedPreferences): Boolean =
            prefs.getBoolean(PREF_SETTINGS_isInSystemTheme, true)

        private fun setInSystemTheme(preferences: SharedPreferences, inSystemTheme: Boolean)
        {
            preferences.edit().putBoolean(PREF_SETTINGS_isInSystemTheme, inSystemTheme).apply()
        }

        private fun setInDarkTheme(preferences: SharedPreferences, isInDarkTheme: Boolean)
        {
            if (isInSystemTheme(preferences))
            {
                setInSystemTheme(preferences, false)
            }
            preferences.edit().putBoolean(PREF_SETTINGS_isInDarkThemeForced, isInDarkTheme).apply()
        }
    }

}

@Composable
fun rememberThemeSelector(): ThemeSelector
{
    val context = LocalContext.current
    return remember {
        ThemeSelectorImpl.create(context)
    }
}