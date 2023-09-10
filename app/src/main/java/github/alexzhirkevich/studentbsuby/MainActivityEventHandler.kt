package github.alexzhirkevich.studentbsuby

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import github.alexzhirkevich.studentbsuby.repo.RemoteConfigRepository
import github.alexzhirkevich.studentbsuby.repo.ReviewRepository
import github.alexzhirkevich.studentbsuby.repo.UpdateRepository
import github.alexzhirkevich.studentbsuby.util.BaseSuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.Mapper
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.mintrocket.lib.mintpermissions.MintPermissionsController
import ru.mintrocket.lib.mintpermissions.ext.isDenied

class MainActivityEventHandler(
    private val dispatchers: Dispatchers,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val updateRepository: UpdateRepository,
    private val reviewRepository: ReviewRepository,
    private val showUpdateRequired: Mapper<Boolean>,
    private val mintPermissionsController: MintPermissionsController
                              ) :
    SuspendEventHandler<MainActivityEvent> by SuspendEventHandler.from(
        InitializedHandler(
            dispatchers = dispatchers,
            remoteConfigRepository = remoteConfigRepository,
            updateRepository = updateRepository,
            reviewRepository = reviewRepository,
            showUpdateRequired = showUpdateRequired,
            mintPermissionsController = mintPermissionsController
                          ), ExitClickedHandler(), UpdateClickedHandler()
                                                                      )


private class ExitClickedHandler : BaseSuspendEventHandler<MainActivityEvent.ExitClicked>(
    MainActivityEvent.ExitClicked::class
                                                                                         )
{
    override suspend fun handle(event: MainActivityEvent.ExitClicked)
    {
        event.activity.finish()
    }
}

private class UpdateClickedHandler : BaseSuspendEventHandler<MainActivityEvent.UpdateClicked>(
    MainActivityEvent.UpdateClicked::class
                                                                                             )
{
    override suspend fun handle(event: MainActivityEvent.UpdateClicked)
    {
        openGooglePlay(event.activity)
    }
}

private class InitializedHandler(
    private val dispatchers: Dispatchers,
    private val remoteConfigRepository: RemoteConfigRepository,
    private val updateRepository: UpdateRepository,
    private val reviewRepository: ReviewRepository,
    private val showUpdateRequired: Mapper<Boolean>,
    private val mintPermissionsController: MintPermissionsController
                                ) : BaseSuspendEventHandler<MainActivityEvent.Initialized>(
    MainActivityEvent.Initialized::class
                                                                                          )
{

    override suspend fun launch()
    {

        coroutineScope {
            launch {
                kotlin.runCatching {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    {
                        if (mintPermissionsController.get(Manifest.permission.POST_NOTIFICATIONS)
                                .isDenied()
                        )
                        {
                            mintPermissionsController.request(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }
            }

            kotlin.runCatching {
                remoteConfigRepository.update()
            }
            if (remoteConfigRepository.getMinimumStableVersionIfNeeded() != null)
            {
                showUpdateRequired.map(true)
            }
        }
    }

    override suspend fun handle(event: MainActivityEvent.Initialized)
    {
        val immediate = remoteConfigRepository.getMinimumStableVersionIfNeeded() != null
        dispatchers.runOnUI {
            reviewRepository.tryShowReviewDialog(event.activity)
            updateRepository.tryUpdate(event.activity, immediate, onFailedToInAppUpdate = {
                openGooglePlay(event.activity)
            })
        }
    }
}

private fun openGooglePlay(context: Context)
{
    try
    {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW, Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                  )
                             )
    } catch (e: ActivityNotFoundException)
    {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
                  )
                             )
    }
}