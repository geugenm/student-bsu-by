package github.alexzhirkevich.studentbsuby.repo

import android.content.SharedPreferences
import com.google.android.gms.tasks.Task
import github.alexzhirkevich.studentbsuby.BuildConfig
import github.alexzhirkevich.studentbsuby.util.sharedPreferences
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

private const val STABLE_VER = "stable_version"
private const val LATEST_VER = "latest_version"

private const val UPDATE_PROP_DELAY = 3 * 24 * 60 * 60 * 1000L

data class ApplicationVersion(
    val code: Int,
    val name: String,
    val desc: String
)

class RemoteConfigRepository @Inject constructor(
    preferences: SharedPreferences
) {

    private var lastUpdateProp by sharedPreferences(preferences, 0L)

    init {

    }

    fun update() {

    }

    private suspend fun getVersion(name: String): ApplicationVersion? = kotlin.runCatching {
        null
    }.getOrNull()

    suspend fun getLatestVersionIfNeeded(): ApplicationVersion? =
        getVersion(LATEST_VER)?.takeIf {
            BuildConfig.VERSION_CODE < it.code &&
                    System.currentTimeMillis() - lastUpdateProp >= UPDATE_PROP_DELAY
        }.also { lastUpdateProp = System.currentTimeMillis() }

    suspend fun getMinimumStableVersionIfNeeded(): ApplicationVersion? =
        getVersion(STABLE_VER)?.takeIf { BuildConfig.VERSION_CODE < it.code }

    fun telegram(): String = kotlin.runCatching {
        null
    }.getOrNull().orEmpty()

    fun mail(): String = kotlin.runCatching {
        null
    }.getOrNull().orEmpty()

}

suspend fun <T> Task<T>.await(): T = suspendCancellableCoroutine { cont ->
    addOnCanceledListener {
        cont.cancel()
    }.addOnSuccessListener {
        if (it != null)
            cont.resume(it)
        else cont.cancel()
    }.addOnFailureListener {
        cont.resumeWithException(it)
    }
}