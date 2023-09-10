package github.alexzhirkevich.studentbsuby.ui.screens.drawer.news

import android.webkit.WebView
import github.alexzhirkevich.studentbsuby.util.Event

sealed interface NewsEvent : Event
{
    class SetupWebView(val newsId: Int, val webview: WebView) : NewsEvent

    object UpdateRequested : NewsEvent
}