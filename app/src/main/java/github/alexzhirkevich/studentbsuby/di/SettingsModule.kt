package github.alexzhirkevich.studentbsuby.di

import android.content.Context
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.studentbsuby.repo.SettingsRepository
import github.alexzhirkevich.studentbsuby.ui.screens.settings.SettingsEvent
import github.alexzhirkevich.studentbsuby.ui.screens.settings.SettingsEventHandler
import github.alexzhirkevich.studentbsuby.ui.screens.settings.SettingsState
import github.alexzhirkevich.studentbsuby.util.EventHandler
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.communication.StateFlowCommunication
import github.alexzhirkevich.studentbsuby.util.communication.StateMapper
import github.alexzhirkevich.studentbsuby.util.logger.Logger
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Module
@InstallIn(ViewModelComponent::class)
class SettingsModule
{

    private var communication: StateFlowCommunication<SettingsState>? = null

    private fun communication(repo: SettingsRepository): StateFlowCommunication<SettingsState>
    {
        return communication ?: StateFlowCommunication(
            initial = SettingsState(
                notificationsEnabled = repo.synchronizationEnabled,
                collectStatistic = repo.collectCrashlytics,
                collectCrashlytics = repo.collectCrashlytics
                                   )
                                                      ).also {
            communication = it
        }
    }

    @Provides
    fun provideCommunication(
        repo: SettingsRepository
                            ): StateCommunication<SettingsState> = communication(repo)

    @Provides
    fun provideMapper(
        repo: SettingsRepository
                     ): StateMapper<SettingsState> = communication(repo)

    @Provides
    fun provideEventHandler(
        @ApplicationContext context: Context,
        repo: SettingsRepository,
        mapper: StateMapper<SettingsState>,
        logger: Logger
                           ): EventHandler<SettingsEvent> = SettingsEventHandler(
        settingsRepository = repo, mapper = mapper, logger = logger, context = context
                                                                                )
}