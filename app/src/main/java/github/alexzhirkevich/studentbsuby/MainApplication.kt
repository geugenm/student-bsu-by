package github.alexzhirkevich.studentbsuby

import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mintrocket.lib.mintpermissions.ext.initMintPermissions

@HiltAndroidApp
@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class MainApplication : Application(), Configuration.Provider
{

    override fun onCreate()
    {
        super.onCreate()
        initMintPermissions()
    }

    override fun getWorkManagerConfiguration() = Configuration.Builder().setWorkerFactory(
            EntryPointAccessors.fromApplication(this, WorkManagerInitializerEntryPoint::class.java)
                .hiltWorkerFactory()
                                                                                         ).build()

    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface WorkManagerInitializerEntryPoint
    {
        fun hiltWorkerFactory(): HiltWorkerFactory
    }
}