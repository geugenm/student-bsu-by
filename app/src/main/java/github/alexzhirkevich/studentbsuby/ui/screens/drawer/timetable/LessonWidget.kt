package github.alexzhirkevich.studentbsuby.ui.screens.drawer.timetable

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.Lesson

const val LessonTimeWidth = 60
const val LessonTimeLineOffsetX = LessonTimeWidth + 12

@ExperimentalMaterialApi
@Composable
fun LessonWidget(
    modifier: Modifier = Modifier,
    lesson: Lesson,
    state: LessonState,
    backgroundColor: Color = MaterialTheme.colors.background
                )
{

    var expanded by rememberSaveable {
        mutableStateOf(state == LessonState.RUNNING)
    }

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "${lesson.starts}\n-\n${lesson.ends}",
            fontWeight = FontWeight.Medium,
            style = MaterialTheme.typography.body1,
            modifier = Modifier
                .width(LessonTimeWidth.dp)
                .padding(vertical = 3.dp),
            textAlign = TextAlign.Center
            )

        Icon(
            contentDescription = "Lesson state",
            tint = MaterialTheme.colors.primary,
            imageVector = when (state)
            {
                LessonState.INCOMING -> Icons.Default.Schedule
                LessonState.RUNNING  -> Icons.Default.LocalFireDepartment
                LessonState.PASSED   -> Icons.Default.TaskAlt
            },
            modifier = Modifier
                .clip(CircleShape)
                .background(backgroundColor)
                .padding(vertical = 5.dp)
            )
        Spacer(modifier = Modifier.width(10.dp))

        Card(
            modifier = Modifier.weight(1f),
            elevation = 5.dp,
            backgroundColor = MaterialTheme.colors.secondary,
            onClick = { expanded = !expanded },
            ) {
            Row(
                Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
               ) {
                Column(
                    Modifier
                        .weight(1f)
                        .animateContentSize()
                      ) {
                    Text(
                        text = lesson.name,
                        style = MaterialTheme.typography.subtitle1,
                        color = MaterialTheme.colors.onSecondary,
                        )

                    if (expanded)
                    {

                        val audienceText = "${stringResource(R.string.audience)}: ${lesson.place}"
                        val typeText = "${stringResource(R.string.type)}: ${lesson.type}"
                        val teacherText = "${stringResource(R.string.teacher)}: ${lesson.teacher}"

                        Spacer(modifier = Modifier.height(5.dp))

                        listOf(
                            audienceText to lesson.place,
                            typeText to lesson.type,
                            teacherText to lesson.teacher
                              ).forEach {
                            Text(
                                text = AnnotatedString(
                                    it.first, spanStyles = listOf(
                                        AnnotatedString.Range(
                                            SpanStyle(fontWeight = FontWeight.Medium),
                                            start = 0,
                                            end = it.first.length - it.second.length - 1
                                                             )
                                                                 )
                                                      ), style = MaterialTheme.typography.body2
                                )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand"
                    )
            }
        }
    }
}