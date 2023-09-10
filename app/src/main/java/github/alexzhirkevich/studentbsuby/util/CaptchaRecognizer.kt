package github.alexzhirkevich.studentbsuby.util

import android.graphics.Bitmap

interface CaptchaRecognizer
{
    suspend fun recognize(bitmap: Bitmap): String
}