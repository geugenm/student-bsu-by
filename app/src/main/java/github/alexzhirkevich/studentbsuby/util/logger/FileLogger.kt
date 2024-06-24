package github.alexzhirkevich.studentbsuby.util.logger

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import android.os.Build
import android.util.Log
import androidx.core.content.FileProvider
import github.alexzhirkevich.studentbsuby.BuildConfig
import github.alexzhirkevich.studentbsuby.R
import java.io.File
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

abstract class DefaultLogger : Logger
{

    override fun log(msg: String, tag: String, logLevel: Logger.LogLevel, cause: Throwable?)
    {
        when (logLevel)
        {
            Logger.LogLevel.Warning -> Log.w(tag, msg, cause)
            Logger.LogLevel.Error   -> Log.e(tag, msg, cause)
        }
    }
}

class FileLogger(
    context: Context
                ) : DefaultLogger()
{

    private val logDir = File(context.getExternalFilesDir(null), "Debug")
    private val logFile = File(logDir, "Logs.txt")

    private val timestampDateFormat: DateFormat = SimpleDateFormat(
        "[yyyy-mm-dd hh:mm:ss]", Locale.getDefault()
                                                                  )

    init
    {
        synchronized(FileLogger::class) {
            tryInit()
        }
    }

    override fun log(msg: String, tag: String, logLevel: Logger.LogLevel, cause: Throwable?)
    {

        if (BuildConfig.DEBUG)
        {
            super.log(msg, tag, logLevel, cause)
        }

        synchronized(FileLogger::class) {
            tryInit()
            kotlin.runCatching {
                logFile.appendText(buildString {
                    append(timestampDateFormat.format(Date()))
                    append(" \\\\ ${logLevel.name.take(1)}\n")
                    append("$tag: $msg\n")
                    if (cause != null && logLevel == Logger.LogLevel.Error) append(cause.stackTraceToString())
                    append("\n")
                })
            }
        }
    }

    private fun tryInit()
    {
        kotlin.runCatching {
            logDir.mkdirs()
            if (!logFile.exists())
            {
                logFile.createNewFile()
                logFile.appendText(buildString {
                    append("Device: ${Build.DEVICE}\n")
                    append("Brand: ${Build.BRAND}\n")
                    append("Model: ${Build.MODEL}\n")
                    append("Manufacturer: ${Build.MANUFACTURER}\n")
                    append("Display: ${Build.DISPLAY}\n")
                    append("API: ${Build.VERSION.SDK_INT}\n")
                    append("vc: ${BuildConfig.VERSION_CODE}\n")
                    append("v: ${BuildConfig.VERSION_NAME}\n")
                    append("\n")
                })
            }
        }
    }

    override fun share(context: Context)
    {
        kotlin.runCatching {
            val uri = FileProvider.getUriForFile(
                context, BuildConfig.APPLICATION_ID + ".provider", logFile
                                                )
            Intent(Intent.ACTION_SEND).apply {
                type = "application/txt"
                flags = FLAG_GRANT_READ_URI_PERMISSION
                putExtra(Intent.EXTRA_STREAM, uri)
            }.let {
                context.startActivity(Intent.createChooser(
                    it, context.getString(R.string.share_logs)
                                                          ).apply {
                    flags = FLAG_ACTIVITY_NEW_TASK
                })
            }
        }
    }
}