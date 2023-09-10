package github.alexzhirkevich.studentbsuby.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.os.Build
import android.provider.Settings
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.core.app.NotificationCompat
import com.google.accompanist.pager.ExperimentalPagerApi
import github.alexzhirkevich.studentbsuby.MainActivity
import github.alexzhirkevich.studentbsuby.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import me.onebone.toolbar.ExperimentalToolbarApi

@ExperimentalToolbarApi
@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
class NotificationCreator(
    private val context: Context,
    private val channelId: String,
    private val channelName: String,
    private val channelDescription: String
                         )
{
    private val manager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    private val intent by lazy {
        PendingIntent.getActivity(
            context, 0, Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }, 0
                                 )
    }


    fun sendNotification(
        id: Int,
        sub: String,
        title: String,
        text: String,
                        )
    {
        kotlin.runCatching {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                manager.createNotificationChannel(NotificationChannel(
                    channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT
                                                                     ).apply {
                    enableVibration(true)
                    enableLights(true)
                    shouldShowLights()
                    shouldVibrate()
                    description = channelDescription
                    setSound(
                        Settings.System.DEFAULT_NOTIFICATION_URI,
                        AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
                            )
                })
            }
            Settings.System.DEFAULT_NOTIFICATION_URI
            val notification =
                NotificationCompat.Builder(context, channelId).setContentIntent(intent)
                    .setDefaults(Notification.DEFAULT_ALL).setSmallIcon(R.drawable.logo)
                    .setSubText(sub).setContentTitle(title).setContentText(text).build()
            manager.notify(id, notification)
        }
    }
}