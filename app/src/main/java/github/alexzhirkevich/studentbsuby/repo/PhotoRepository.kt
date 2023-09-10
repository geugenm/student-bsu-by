package github.alexzhirkevich.studentbsuby.repo

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.studentbsuby.api.ProfileApi
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class PhotoRepository @Inject constructor(
    private val profileApi: ProfileApi,
    private val usernameProvider: UsernameProvider,
    @ApplicationContext context: Context
                                         ) : CacheWebRepository<Bitmap>()
{

    private val cacheDir = context.cacheDir

    override suspend fun getFromCache(): Bitmap? = kotlin.runCatching {
        usernameProvider.username.takeIf(String::isNotBlank)?.let {
            getCachedPhotoLocation(it).takeIf(File::exists)?.let {
                BitmapFactory.decodeFile(it.path)
            }
        }
    }.getOrNull()

    override suspend fun getFromWeb(): Bitmap?
    {
        profileApi.photo().bytes().let {
            return (BitmapFactory.decodeByteArray(it, 0, it.size))
        }
    }

    override suspend fun saveToCache(value: Bitmap)
    {
        kotlin.runCatching {
            usernameProvider.username.takeIf(String::isNotBlank)?.let { username ->
                FileOutputStream(getCachedPhotoLocation(username)).use {
                    value.compress(Bitmap.CompressFormat.JPEG, 100, it)
                }
            }
        }
    }

    private fun getCachedPhotoLocation(username: String): File
    {
        return File(cacheDir, username.filter(Char::isLetterOrDigit))
    }
}