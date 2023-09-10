package github.alexzhirkevich.studentbsuby.ui.screens.drawer.about

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.repo.RemoteConfigRepository
import github.alexzhirkevich.studentbsuby.util.BaseSuspendEventHandler
import github.alexzhirkevich.studentbsuby.util.SuspendEventHandler

class AboutEventHandler(
    context: Context, configRepository: RemoteConfigRepository
                       ) : SuspendEventHandler<AboutEvent> by SuspendEventHandler.from(
    TgClickedHandler(context, configRepository), EmailClickedHandler(context, configRepository)
                                                                                      )

private class TgClickedHandler(
    private val context: Context, private val configRepository: RemoteConfigRepository
                              ) : BaseSuspendEventHandler<AboutEvent.TgClicked>(
    AboutEvent.TgClicked::class
                                                                               )
{

    override suspend fun launch()
    {
        kotlin.runCatching {
            configRepository.update()
        }
    }

    override suspend fun handle(event: AboutEvent.TgClicked)
    {

        kotlin.runCatching {
            val uri = Uri.parse(configRepository.telegram())
            Intent(Intent.ACTION_VIEW, uri).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }.let {
                    context.startActivity(it)
                }
        }
    }
}

private class EmailClickedHandler(
    private val context: Context, private val configRepository: RemoteConfigRepository
                                 ) : BaseSuspendEventHandler<AboutEvent.EmailClicked>(
    AboutEvent.EmailClicked::class
                                                                                     )
{

    override suspend fun launch()
    {
        kotlin.runCatching {
            configRepository.update()
        }
    }

    @SuppressLint("IntentReset")
    override suspend fun handle(event: AboutEvent.EmailClicked)
    {
        kotlin.runCatching {
            Intent(Intent.ACTION_SENDTO).apply {
                type = "text/html"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(configRepository.mail()))
                putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
            }.let {
                context.startActivity(it)
            }
        }
    }
}