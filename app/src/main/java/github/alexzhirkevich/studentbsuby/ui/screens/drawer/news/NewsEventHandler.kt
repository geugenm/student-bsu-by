package github.alexzhirkevich.studentbsuby.ui.screens.drawer.news

import android.content.Intent
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.News
import github.alexzhirkevich.studentbsuby.repo.DataSource
import github.alexzhirkevich.studentbsuby.repo.NewsRepository
import github.alexzhirkevich.studentbsuby.util.BaseSuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.ConnectivityManager
import github.alexzhirkevich.studentbsuby.util.DataState
import github.alexzhirkevich.studentbsuby.util.LoginCookieManager
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.communication.Mapper
import github.alexzhirkevich.studentbsuby.util.communication.StateMapper
import github.alexzhirkevich.studentbsuby.util.dispatchers.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onEmpty

class NewsEventHandler(
    dispatchers: Dispatchers,
    newsRepository: NewsRepository,
    connectivityManager: ConnectivityManager,
    loginCookieManager: LoginCookieManager,
    isUpdatingMapper: Mapper<Boolean>,
    newsMapper: StateMapper<DataState<List<News>>>
                      ) : SuspendEventHandler<NewsEvent> by SuspendEventHandler.from(
    SetupWebViewHandler(
        dispatchers = dispatchers,
        newsRepository = newsRepository,
        loginCookieManager = loginCookieManager
                       ), UpdateRequestedHandler(
        connectivityManager = connectivityManager,
        newsRepository = newsRepository,
        isUpdatingMapper = isUpdatingMapper,
        newsMapper = newsMapper
                                                )
                                                                                    )

private class SetupWebViewHandler(
    private val dispatchers: Dispatchers,
    private val newsRepository: NewsRepository,
    private val loginCookieManager: LoginCookieManager
                                 ) : BaseSuspendEventHandler<NewsEvent.SetupWebView>(
    NewsEvent.SetupWebView::class
                                                                                    )
{
    override suspend fun handle(event: NewsEvent.SetupWebView)
    {

        dispatchers.runOnUI {
            CookieManager.getInstance()
                .setCookie(newsRepository.baseUrl.toString(), loginCookieManager.getCookies())
            event.webview.webViewClient = object : WebViewClient()
            {

                override fun shouldOverrideUrlLoading(
                    view: WebView, request: WebResourceRequest
                                                     ): Boolean
                {
                    view.context.startActivity(
                        Intent(Intent.ACTION_VIEW, request.url)
                                              )
                    return true
                }
            }
        }
        newsRepository.getNewsItem(event.newsId, DataSource.Remote).collectLatest {
            dispatchers.runOnUI {
                event.webview.loadDataWithBaseURL(
                    newsRepository.newsUrl, it.content, "text/html", "UTF-8", null
                                                 )
            }
        }
    }
}

private class UpdateRequestedHandler(
    private val connectivityManager: ConnectivityManager,
    private val newsRepository: NewsRepository,
    private val isUpdatingMapper: Mapper<Boolean>,
    private val newsMapper: StateMapper<DataState<List<News>>>
                                    ) : BaseSuspendEventHandler<NewsEvent.UpdateRequested>(
    NewsEvent.UpdateRequested::class
                                                                                          )
{

    override suspend fun launch()
    {
        isUpdatingMapper.map(false)
        newsMapper.map(DataState.Loading)
        update(DataSource.All)
        connectivityManager.isNetworkConnected.collect {
            if (it)
            {
                update(DataSource.Remote)
            }
        }
    }

    override suspend fun handle(event: NewsEvent.UpdateRequested)
    {
        isUpdatingMapper.map(true)
        update(DataSource.Remote)
        isUpdatingMapper.map(false)
    }

    private suspend fun update(dataSource: DataSource)
    {
        newsRepository.get(dataSource).onEach {
                newsMapper.map(DataState.Success(it))
            }.onEmpty {
                newsMapper.map(DataState.Empty)
            }.catch {
                if (newsMapper.current !is DataState.Success)
                {
                    newsMapper.map(
                        DataState.Error(
                            R.string.error_load_news, it
                                       )
                                  )
                }
            }.collect()
    }
}