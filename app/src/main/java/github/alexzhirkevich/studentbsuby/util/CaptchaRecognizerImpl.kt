package github.alexzhirkevich.studentbsuby.util

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CaptchaRecognizerImpl : CaptchaRecognizer
{

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override suspend fun recognize(bitmap: Bitmap): String
    {
        return kotlin.runCatching {
            val image = InputImage.fromBitmap(bitmap, 0)
            suspendCoroutine<String> { cont ->

                recognizer.process(image).addOnSuccessListener {
                    cont.resume(
                        it.textBlocks.firstOrNull()?.text.orEmpty()
                               )
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
            }
        }.getOrDefault("")
    }
}