package github.alexzhirkevich.studentbsuby.di

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import github.alexzhirkevich.studentbsuby.BuildConfig
import github.alexzhirkevich.studentbsuby.repo.UsernameProvider
import github.alexzhirkevich.studentbsuby.repo.UsernameProviderImpl
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.ConnectivityUi
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.ConnectivityUiSerializer
import github.alexzhirkevich.studentbsuby.util.*
import github.alexzhirkevich.studentbsuby.util.communication.BroadcastMapper
import github.alexzhirkevich.studentbsuby.util.communication.BroadcastReceiverMapper
import github.alexzhirkevich.studentbsuby.util.communication.StateFlowCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.CoroutineJobManagerImpl
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import github.alexzhirkevich.studentbsuby.util.dispatchers.DispatchersImpl
import github.alexzhirkevich.studentbsuby.util.logger.FileLogger
import github.alexzhirkevich.studentbsuby.util.logger.Logger
import ru.mintrocket.lib.mintpermissions.MintPermissions
import ru.mintrocket.lib.mintpermissions.MintPermissionsController
import ru.mintrocket.lib.mintpermissions.MintPermissionsManager
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
annotation class CredentialsPrefsQualifier

@Qualifier
annotation class CookiesPrefsQualifier

@Module
@InstallIn(SingletonComponent::class)
class AppModule
{

    @Provides
    fun provideCalendar(): Calendar = Calendar.Base()

    @Provides
    fun providePermController(): MintPermissionsController = MintPermissions.controller

    @Provides
    fun providePermManager(): MintPermissionsManager = MintPermissions.createManager()

    @Provides
    fun provideConnectivityManager(
        @ApplicationContext context: Context
                                  ): ConnectivityManager =
        ConnectivityManager.Internet(context, StateFlowCommunication(false))

    @Provides
    fun provideConnectivityMapper(
        @ApplicationContext context: Context
                                 ): BroadcastMapper<ConnectivityUi> = BroadcastReceiverMapper(
        context, ConnectivityUi::class.java.simpleName, ConnectivityUiSerializer
                                                                                             )

    @Provides
    @Singleton
    fun provideCaptureRecognizer(): CaptchaRecognizer = CaptchaRecognizerImpl()

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences
    {
        return PreferenceManager.getDefaultSharedPreferences(context).also {
            kotlin.runCatching {
                if (it.contains("username") || it.contains("password"))
                {
                    it.edit().remove("username").remove("password").apply()
                }
            }
        }
    }

    private fun createEncryptedPrefs(name: String, context: Context) =
        EncryptedSharedPreferences.create(
            context,
            name,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .setUserAuthenticationRequired(false).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                                         )

    @Provides
    @CredentialsPrefsQualifier
    fun provideEncryptedSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        kotlin.runCatching {
            createEncryptedPrefs(BuildConfig.APPLICATION_ID + "_credentials", context)
        }.getOrElse {
            provideSharedPreferences(context)
        }

    @Provides
    @CookiesPrefsQualifier
    fun provideCookiesPreferencesSharedPreferences(
        @ApplicationContext context: Context
                                                  ): SharedPreferences = kotlin.runCatching {
        createEncryptedPrefs(BuildConfig.APPLICATION_ID + "_cookies", context)
    }.getOrElse {
        provideSharedPreferences(context)
    }

    @Provides
    @Singleton
    fun provideLogger(@ApplicationContext context: Context): Logger = FileLogger(context)

    @Provides
    fun provideResources(@ApplicationContext context: Context): Resources = context.resources

    @Provides
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager =
        ResourceManager.Base(context)

    @Provides
    fun provideDispatchers(): Dispatchers = DispatchersImpl(
        CoroutineJobManagerImpl()
                                                           )

    @Provides
    fun provideErrorHandler(logger: Logger): ErrorHandler = ErrorHandler.Log(logger)

    @Provides
    fun provideWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

    @Provides
    fun provideUsernameProvider(
        @CredentialsPrefsQualifier preferences: SharedPreferences
                               ): UsernameProvider = UsernameProviderImpl(preferences)
}
