package github.alexzhirkevich.studentbsuby.workers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.work.HiltWorker
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.await
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.repo.HostelRepository
import github.alexzhirkevich.studentbsuby.repo.LoginRepository
import github.alexzhirkevich.studentbsuby.repo.TimetableRepository
import github.alexzhirkevich.studentbsuby.util.NotificationCreator
import github.alexzhirkevich.studentbsuby.util.WorkerManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.onebone.toolbar.ExperimentalToolbarApi
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private const val HostelUpdateNotification = 1000001
private const val TimetableUpdateNotification = 1000092

@ExperimentalToolbarApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@Singleton
class SyncWorkerManager @Inject constructor(
    private val workManager: WorkManager,
                                           ) : WorkerManager
{

    override suspend fun isEnabled(): Boolean
    {
        return workManager.getWorkInfosForUniqueWork(SyncWorker.TAG).await().isNotEmpty()
    }

    override fun run()
    {
        val request = PeriodicWorkRequestBuilder<SyncWorker>(1, TimeUnit.HOURS).setBackoffCriteria(
                BackoffPolicy.LINEAR,
                15,
                TimeUnit.MINUTES
                                                                                                  )
            .setConstraints(
                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                    .setRequiresBatteryNotLow(true).build()
                           ).build()

        workManager.enqueueUniquePeriodicWork(
                SyncWorker.TAG, ExistingPeriodicWorkPolicy.KEEP, request
                                             )

    }

    override fun stop()
    {
        workManager.cancelUniqueWork(SyncWorker.TAG)
    }

}

@ExperimentalToolbarApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@HiltWorker
class SyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted parameters: WorkerParameters,
    private val hostelRepository: HostelRepository,
    private val timetableRepository: TimetableRepository,
    private val loginRepository: LoginRepository,
                                            ) : CoroutineWorker(context, parameters)
{

    companion object
    {
        const val TAG = "SynchronizationWorker"
    }

    override suspend fun doWork(): Result
    {
        return Result.success()
    }
}

@ExperimentalCoroutinesApi
@ExperimentalToolbarApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
class SyncWorkerReceiver : BroadcastReceiver()
{
    override fun onReceive(context: Context, intent: Intent)
    {
    }
}
