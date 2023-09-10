package github.alexzhirkevich.studentbsuby.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.annotation.ArrayRes
import androidx.annotation.BoolRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.FontRes
import androidx.annotation.IntegerRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

interface ResourceManager
{

    fun string(@StringRes id: Int, vararg args: Any): String
    fun integer(@IntegerRes id: Int): Int
    fun raw(@RawRes id: Int): ByteArray
    fun boolean(@BoolRes id: Int): Boolean
    fun dimension(@DimenRes id: Int): Float
    fun stringArray(@ArrayRes id: Int): Array<String>
    fun intArray(@ArrayRes id: Int): IntArray
    fun color(@ColorRes id: Int): Int
    fun drawable(@DrawableRes id: Int): Drawable?
    fun bitmap(@DrawableRes id: Int): Bitmap
    fun font(@FontRes id: Int): Typeface?

    class Base(private val context: Context) : ResourceManager
    {
        override fun string(id: Int, vararg args: Any): String =
            context.resources.getString(id, args)

        override fun integer(id: Int): Int = context.resources.getInteger(id)

        override fun raw(id: Int): ByteArray = context.resources.openRawResource(id).readBytes()

        override fun boolean(id: Int): Boolean = context.resources.getBoolean(id)

        override fun stringArray(id: Int): Array<String> = context.resources.getStringArray(id)

        override fun intArray(id: Int): IntArray = context.resources.getIntArray(id)

        override fun dimension(id: Int): Float = context.resources.getDimension(id)

        override fun color(id: Int): Int = ContextCompat.getColor(context, id)

        override fun drawable(id: Int): Drawable? = ContextCompat.getDrawable(context, id)

        override fun bitmap(id: Int): Bitmap = BitmapFactory.decodeResource(context.resources, id)

        override fun font(id: Int): Typeface? = ResourcesCompat.getFont(context, id)
    }
}