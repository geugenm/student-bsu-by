package github.alexzhirkevich.studentbsuby.repo

import android.content.SharedPreferences
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import com.google.accompanist.pager.ExperimentalPagerApi
import github.alexzhirkevich.studentbsuby.util.sharedPreferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
class SettingsRepository @Inject constructor(
    preferences: SharedPreferences,
) {
    var synchronizationEnabled by sharedPreferences(preferences, true) {
    }

    var collectStatistics by sharedPreferences(preferences, true) {
    }

    var collectCrashlytics by sharedPreferences(preferences, true) {
    }
}