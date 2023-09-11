package github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.SpaceEvenly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HighlightOff
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.data.models.Subject
import github.alexzhirkevich.studentbsuby.ui.theme.values.Colors
import github.alexzhirkevich.studentbsuby.util.bsuBackgroundPattern

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun SubjectWidget(
    modifier: Modifier = Modifier, subject: Subject
                 )
{
    var isOpened by rememberSaveable {
        mutableStateOf(false)
    }

    Card(modifier = modifier,
        elevation = 3.dp,
        backgroundColor = MaterialTheme.colors.secondary,
        onClick = {
            isOpened = !isOpened
        }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
              ) {
            Box(
                Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colors.primaryVariant)
                    .bsuBackgroundPattern(
                        color = MaterialTheme.colors.background.copy(alpha = .05f), clip = true
                                         )
                    .padding(vertical = 10.dp, horizontal = 5.dp)
                    .zIndex(2f)
               ) {

                Text(
                    text = subject.name,
                    color = MaterialTheme.colors.surface,
                    style = MaterialTheme.typography.body1,
                    textAlign = TextAlign.Center,
                    maxLines = if (isOpened) Int.MAX_VALUE else 3,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                    )
            }


            AnimatedVisibility(
                modifier = Modifier.zIndex(1f),
                visible = isOpened,
                              ) {
                HoursPanel(
                    subject = subject, modifier = Modifier
                        .fillMaxWidth()
                        .animateEnterExit(
                            enter = if (animationEnabled) slideInVertically() else EnterTransition.None,
                            exit = if (animationEnabled) slideOutVertically() else ExitTransition.None
                                         )
                          )
            }

            if (subject.hasCredit || subject.hasExam)
            {
                Column(
                    Modifier.padding(10.dp)
                      ) {
                    if (subject.hasCredit) ExamRow(
                        title = stringResource(id = R.string.zachet),
                        isPassed = subject.creditPassed,
                        retakes = subject.creditRetakes,
                        mark = subject.creditMark
                                                  )
                    if (subject.hasExam) ExamRow(
                        title = stringResource(id = R.string.exam),
                        retakes = subject.examRetakes,
                        mark = subject.examMark
                                                )
                }
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
@Preview
@Composable
fun SubjectWidgetPreview()
{
    val subject = testSubject
    SubjectWidget(subject = subject)
}

@Composable
fun ExamRow(
    title: String, isPassed: Boolean? = false, retakes: Int, mark: Int?
           )
{
    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
       ) {

        Text(text = title)

        val (icon, color) = when
        {
            (isPassed == true) -> Icons.Default.TaskAlt to Colors.Green
            (mark == null)     -> Icons.Default.HighlightOff to MaterialTheme.colors.error
            else               -> Icons.Default.Schedule to MaterialTheme.colors.onSecondary
        }

        Row(verticalAlignment = CenterVertically) {
            if (retakes != 0)
            {
                Box {
                    Icon(
                        imageVector = Icons.Default.Repeat,
                        contentDescription = "",
                        tint = MaterialTheme.colors.error,
                        modifier = Modifier
                            .align(Center)
                            .size(28.dp)
                        )
                    Text(
                        text = retakes.toString(),
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.error,
                        modifier = Modifier.align(Center)
                        )
                }
            }

            Spacer(modifier = Modifier.width(5.dp))

            if (mark == null)
            {
                Icon(imageVector = icon, tint = color, contentDescription = "")
            } else
            {
                Text(
                    text = mark.toString(),
                    style = MaterialTheme.typography.subtitle1,
                    color = when (mark)
                    {
                        in 1..3 -> MaterialTheme.colors.error
                        else    -> Colors.Green
                    },
                    modifier = Modifier.padding(end = 5.dp)
                    )
            }
        }
    }
}

@Composable
private fun HoursPanel(
    modifier: Modifier = Modifier,
    subject: Subject
                      )
{
    Column(
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
          ) {
        val hours = listOf(
            stringResource(R.string.lectures) to subject.lectures,
            stringResource(R.string.pract) to subject.practice,
            stringResource(R.string.labs) to subject.labs,
            stringResource(R.string.seminars) to subject.seminars,
            stringResource(R.string.facults) to subject.facults,
            stringResource(R.string.ksr) to subject.ksr,
                          ).filter { it.second > 0 }

        val chunkedHours = when
        {
            hours.size <= 3 -> listOf(hours)
            hours.size == 4 -> hours.chunked(2)
            else            -> hours.chunked(3)
        }
        chunkedHours.forEach { group ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                verticalAlignment = CenterVertically,
                horizontalArrangement = SpaceEvenly,
               ) {
                group.forEach {
                    HoursItem(name = it.first, hrs = it.second)
                }
            }
        }
    }
}

@Preview
@Composable
private fun HoursPanelPreview()
{
    HoursPanel(
        subject = testSubject
              )
}

@Composable
private fun HoursItem(name: String, hrs: Int)
{

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
          ) {
        Card(
            shape = CircleShape, backgroundColor = MaterialTheme.colors.background, elevation = 5.dp
            ) {
            Box(modifier = Modifier.size(with(LocalDensity.current) {
                MaterialTheme.typography.body1.fontSize.toDp() * 2.5f
            })) {

                Text(
                    text = hrs.toString(),
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.align(Center)
                    )
            }

        }
        Text(text = name, style = MaterialTheme.typography.caption)
    }
}

@Preview
@Composable
private fun HoursItemPreview()
{
    HoursItem(name = "Serena Hawkins", hrs = 9100)
}


val testSubject = Subject(
    id = 2178,
    owner = "facilis",
    name = "Jeremiah Shields Jeremiah Shields Jeremiah Shields Jeremiah Shields Jeremiah Shields Jeremiah Shields Jeremiah Shields Jeremiah Shields",
    lectures = 30,
    practice = 15,
    labs = 30,
    seminars = 10,
    facults = 62,
    ksr = 8,
    hasCredit = true,
    creditPassed = null,
    creditMark = null,
    creditRetakes = 2,
    hasExam = true,
    examMark = null,
    examRetakes = 0,
    semester = 6931
                         )