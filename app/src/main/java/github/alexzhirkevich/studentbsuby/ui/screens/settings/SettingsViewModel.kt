package github.alexzhirkevich.studentbsuby.ui.screens.settings

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.lifecycle.ViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.lifecycle.HiltViewModel
import github.alexzhirkevich.studentbsuby.util.EventHandler
import github.alexzhirkevich.studentbsuby.util.communication.StateCommunication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalFoundationApi
@ExperimentalPagerApi
@HiltViewModel
class SettingsViewModel @Inject constructor(
    val state: StateCommunication<SettingsState>, handler: EventHandler<SettingsEvent>
                                           ) : ViewModel(), EventHandler<SettingsEvent> by handler