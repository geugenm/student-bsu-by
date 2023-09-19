package github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.data.models.Lesson
import github.alexzhirkevich.studentbsuby.util.applyIf


private const val HorizontalPadding = 10

@ExperimentalMaterialApi
@Composable
fun TimetableWidget(
    list: List<Pair<Lesson, LessonState>>,
    backgroundColor: Color = MaterialTheme.colors.background,
    modifier: Modifier = Modifier
                   )
{

    val lineColor = MaterialTheme.colors.primary.copy(alpha = .5f)
    val density = LocalDensity.current.density

    var timelineStart by rememberSaveable {
        mutableStateOf(0f)
    }
    var timelineEnd by rememberSaveable {
        mutableStateOf(0f)
    }

    val state = rememberLazyListState()

    LazyColumn(state = state, modifier = modifier.applyIf(list.size > 1) {
            it.drawBehind {
                drawLine(
                    color = lineColor,
                    strokeWidth = 1 * density,
                    start = Offset(
                        x = density * (LessonTimeLineOffsetX + HorizontalPadding),
                        y = if (state.firstVisibleItemIndex == 0) timelineStart - state.firstVisibleItemScrollOffset
                        else 0f
                                  ),
                    end = Offset(
                        x = density * (LessonTimeLineOffsetX + HorizontalPadding),
                        y = timelineEnd
                                ),
                        )
            }
        }) {
        items(list.size) { idx ->
            LessonWidget(lesson = list[idx].first,
                state = list[idx].second,
                backgroundColor = backgroundColor,
                modifier = Modifier.padding(
                    horizontal = HorizontalPadding.dp, vertical = 15.dp
                                           ).let {
                    when (idx)
                    {
                        0 -> it.onSizeChanged {
                            timelineStart = it.height.toFloat() / 2
                        }

                        list.size - 1 -> it.onGloballyPositioned {
                            timelineEnd = it.positionInParent().y + it.size.height.toFloat() / 2
                        }

                        else -> it
                    }
                })
        }
        item {
            Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
        }
    }
}