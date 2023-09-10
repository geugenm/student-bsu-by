package github.alexzhirkevich.studentbsuby.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import github.alexzhirkevich.studentbsuby.data.models.News
import github.alexzhirkevich.studentbsuby.repo.NewsRepository
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.news.NewsEvent
import github.alexzhirkevich.studentbsuby.ui.screens.drawer.news.NewsEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.LoginCookieManager
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import github.alexzhirkevich.studentbsuby.util.communication.StateFlowCommunication
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import javax.inject.Qualifier

@Qualifier
annotation class IsNewsUpdatingQualifier

@Module
@InstallIn(ViewModelComponent::class)
class NewsModule
{

    private val newsCommunication = StateFlowCommunication<DataState<List<News>>>(
        DataState.Loading
                                                                                 )

    private val isUpdatingCommunication = StateFlowCommunication(false)

    @Provides
    @IsNewsUpdatingQualifier
    fun provideIsUpdatingCommunication(): StateCommunication<Boolean> = isUpdatingCommunication

    @Provides
    fun provideNewsCommunication(): StateCommunication<DataState<List<News>>> = newsCommunication

    @Provides
    fun provideEventHandler(
        dispatchers: Dispatchers,
        newsRepository: NewsRepository,
        connectivityManager: ConnectivityManager,
        loginCookieManager: LoginCookieManager,
                           ): SuspendEventHandler<NewsEvent> = NewsEventHandler(
        newsRepository = newsRepository,
        connectivityManager = connectivityManager,
        loginCookieManager = loginCookieManager,
        isUpdatingMapper = isUpdatingCommunication,
        newsMapper = newsCommunication,
        dispatchers = dispatchers
                                                                               )
}