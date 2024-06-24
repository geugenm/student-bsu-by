package github.alexzhirkevich.studentbsuby

import android.app.Application
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import ru.mintrocket.lib.mintpermissions.ext.initMintPermissions

/**
 * Main Application class for the StudentBSUBy application.
 *
 * This class is responsible for initializing the application, including:
 * - Setting up Hilt dependency injection
 * - Initializing MintPermissions for runtime permissions handling
 * - Configuring WorkManager for background tasks
 *
 * It utilizes several experimental APIs from Jetpack Compose,
 * which are subject to change in future releases.
 */
@HiltAndroidApp
@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class MainApplication : Application(), Configuration.Provider
{

    /**
     * Called when the application is starting.
     * Initializes MintPermissions for runtime permissions handling.
     */
    override fun onCreate()
    {
        super.onCreate()
        initMintPermissions()
    }

    /**
     * Entry point for accessing the Hilt-provided WorkerFactory.
     * This interface allows retrieving the HiltWorkerFactory instance,
     * which is used by WorkManager for creating Workers with dependency injection.
     */
    @InstallIn(SingletonComponent::class)
    @EntryPoint
    interface WorkManagerInitializerEntryPoint
    {
        fun hiltWorkerFactory(): HiltWorkerFactory
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(
                EntryPointAccessors.fromApplication(
                    this, WorkManagerInitializerEntryPoint::class.java
                                                   ).hiltWorkerFactory()
                                                        ).build()
}