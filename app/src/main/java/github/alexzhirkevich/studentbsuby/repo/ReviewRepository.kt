package github.alexzhirkevich.studentbsuby.repo

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.android.play.core.ktx.launchReview
import com.google.android.play.core.ktx.requestReview
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.qualifiers.ApplicationContext
import github.alexzhirkevich.studentbsuby.util.sharedPreferences
import javax.inject.Inject

private const val REVIEW_DELAY = 3 * 24 * 60 * 60 * 1000

class ReviewRepository @Inject constructor(
    preferences: SharedPreferences, @ApplicationContext context: Context
                                          )
{

    private val firstInitializing by sharedPreferences(
        preferences, System.currentTimeMillis()
                                                      )

    private val reviewManager by lazy {
        ReviewManagerFactory.create(context)
    }

    suspend fun tryShowReviewDialog(activity: Activity)
    {
        if (System.currentTimeMillis() - firstInitializing > REVIEW_DELAY)
        {
            kotlin.runCatching {
                val info = reviewManager.requestReview()
                reviewManager.launchReview(activity, info)
            }
        }
    }
}