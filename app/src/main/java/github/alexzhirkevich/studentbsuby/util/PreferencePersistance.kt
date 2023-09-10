package github.alexzhirkevich.studentbsuby.util

import android.content.SharedPreferences
import androidx.annotation.CallSuper
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

inline fun <reified T> sharedPreferences(
    preferences: SharedPreferences,
    default: T,
    noinline onChanged: (T) -> Unit = {},
                                        ): PreferencePersistence<T>
{
    return sharedPreferences(T::class, preferences, default, onChanged)
}

@Suppress("unchecked_cast")
fun <T> sharedPreferences(
    clazz: KClass<*>,
    preferences: SharedPreferences,
    default: T,
    onChanged: (T) -> Unit = {},
                         ): PreferencePersistence<T>
{
    return when (clazz)
    {
        Boolean::class -> BooleanPreferencePersistence(
            preferences = preferences,
            onChanged = onChanged as (Boolean) -> Unit,
            default = default as Boolean
                                                      ) as PreferencePersistence<T>

        String::class  -> StringPreferencePersistence(
            preferences = preferences,
            onChanged = onChanged as (String) -> Unit,
            default = default as String
                                                     ) as PreferencePersistence<T>

        Int::class     -> IntPreferencePersistence(
            preferences = preferences,
            onChanged = onChanged as (Int) -> Unit,
            default = default as Int
                                                  ) as PreferencePersistence<T>

        Long::class    -> LongPreferencePersistence(
            preferences = preferences,
            onChanged = onChanged as (Long) -> Unit,
            default = default as Long
                                                   ) as PreferencePersistence<T>

        Float::class   -> FloatPreferencePersistence(
            preferences = preferences,
            onChanged = onChanged as (Float) -> Unit,
            default = default as Float
                                                    ) as PreferencePersistence<T>

        else           -> try
        {
            StringSetPreferencePersistence(
                preferences = preferences,
                onChanged = onChanged as (Set<String>) -> Unit,
                default = default as Set<String>
                                          ) as PreferencePersistence<T>
        } catch (c: ClassCastException)
        {
            throw IllegalArgumentException("$clazz cannot be stored in shared preferences")
        }
    }
}


@Suppress("unchecked_cast")
sealed class PreferencePersistence<T>(
    val preferences: SharedPreferences,
    val default: T,
    val onChanged: (T) -> Unit,
                                     )
{
    operator fun getValue(self: Any?, property: KProperty<*>): T
    {
        return preferences.all[property.name] as? T ?: default.also {
            setValue(self, property, it)
        }
    }

    abstract operator fun setValue(self: Any?, property: KProperty<*>, t: T)

}

class BooleanPreferencePersistence(
    preferences: SharedPreferences,
    default: Boolean,
    onChanged: (Boolean) -> Unit,
                                  ) :
    PreferencePersistence<Boolean>(preferences, default, onChanged)
{

    @CallSuper
    override fun setValue(self: Any?, property: KProperty<*>, t: Boolean)
    {
        preferences.edit().putBoolean(property.name, t).apply()
        onChanged(t)
    }
}

class StringPreferencePersistence(
    preferences: SharedPreferences,
    default: String,
    onChanged: (String) -> Unit,
                                 ) : PreferencePersistence<String>(preferences, default, onChanged)
{
    override fun setValue(
        self: Any?, property: KProperty<*>, t: String
                         )
    {
        preferences.edit().putString(property.name, t).apply()
        onChanged(t)
    }
}

class IntPreferencePersistence(
    preferences: SharedPreferences,
    default: Int,
    onChanged: (Int) -> Unit,
                              ) : PreferencePersistence<Int>(preferences, default, onChanged)
{
    override fun setValue(
        self: Any?, property: KProperty<*>, t: Int
                         )
    {
        preferences.edit().putInt(property.name, t).apply()
        onChanged(t)
    }
}

class LongPreferencePersistence(
    preferences: SharedPreferences,
    default: Long,
    onChanged: (Long) -> Unit,
                               ) : PreferencePersistence<Long>(preferences, default, onChanged)
{
    override fun setValue(
        self: Any?, property: KProperty<*>, t: Long
                         )
    {
        preferences.edit().putLong(property.name, t).apply()
        onChanged(t)
    }
}

class FloatPreferencePersistence(
    preferences: SharedPreferences,
    default: Float,
    onChanged: (Float) -> Unit,
                                ) : PreferencePersistence<Float>(preferences, default, onChanged)
{
    override fun setValue(
        self: Any?, property: KProperty<*>, t: Float
                         )
    {
        preferences.edit().putFloat(property.name, t).apply()
        onChanged(t)
    }
}

class StringSetPreferencePersistence(
    preferences: SharedPreferences,
    default: Set<String>,
    onChanged: (Set<String>) -> Unit,
                                    ) :
    PreferencePersistence<Set<String>>(preferences, default, onChanged)
{
    override fun setValue(
        self: Any?, property: KProperty<*>, t: Set<String>
                         )
    {
        preferences.edit().putStringSet(property.name, t).apply()
        onChanged(t)
    }
}
