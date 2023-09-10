package github.alexzhirkevich.studentbsuby.repo

import android.app.Activity
import android.content.Context
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.ActivityResult
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed
import com.google.android.play.core.ktx.requestAppUpdateInfo
import com.google.android.play.core.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.studentbsuby.R
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UpdateRepository @Inject constructor(
    @ApplicationContext context: Context,
                                          )
{
    private val appUpdateManager = AppUpdateManagerFactory.create(context)

    suspend fun tryUpdate(
        activity: Activity,
        immediate: Boolean,
        onFailedToInAppUpdate: () -> Unit,
                         ) = kotlin.runCatching {
        val info = appUpdateManager.requestAppUpdateInfo()
        if (info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE)
        {
            val listener = newUpdateListener(activity)
            appUpdateManager.registerListener(listener)
            while (!tryUpdateInternal(
                    activity = activity,
                    immediate = immediate || info.updatePriority() == 5,
                    info = info,
                    onFailedToInAppUpdate = onFailedToInAppUpdate
                                     )
            ) appUpdateManager.unregisterListener(listener)
        }
    }

    private suspend fun tryUpdateInternal(
        activity: Activity,
        immediate: Boolean,
        info: AppUpdateInfo,
        onFailedToInAppUpdate: () -> Unit
                                         ): Boolean = when
    {
        info.isImmediateUpdateAllowed && immediate -> AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE)
            .setAllowAssetPackDeletion(true).build()

        info.isFlexibleUpdateAllowed               -> AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
            .setAllowAssetPackDeletion(true).build()

        else                                       ->
        {
            onFailedToInAppUpdate()
            null
        }
    }?.let {
        kotlin.runCatching {
            appUpdateManager.startUpdateFlow(info, activity, it).await()
        }.getOrDefault(ActivityResult.RESULT_IN_APP_UPDATE_FAILED)
    }?.let { it == Activity.RESULT_OK } ?: true

    private fun newUpdateListener(activity: Activity) = InstallStateUpdatedListener { state ->
        if (state.installStatus() == InstallStatus.DOWNLOADED)
        {
            Snackbar.make(
                activity.window.decorView,
                activity.getString(R.string.update_downloaded),
                Snackbar.LENGTH_INDEFINITE
                         ).apply {
                setActionTextColor(activity.getColor(R.color.blue))
                setAction(activity.getString(R.string.restart)) {
                    appUpdateManager.completeUpdate()
                }
                show()
            }
        }
    }
}

suspend fun <T> Task<T>.await() = suspendCoroutine<T> { cont ->
    addOnSuccessListener {
        cont.resume(it)
    }.addOnFailureListener {
        cont.resumeWithException(it)
    }
}