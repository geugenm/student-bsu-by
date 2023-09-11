package github.alexzhirkevich.studentbsuby.ui.screens.drawer.subjects

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
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
    subject: Subject,
    modifier: Modifier = Modifier,
    isOpened: Boolean = false,
    onClick: () -> Unit = {}
                 )
{

    var animationEnabled by rememberSaveable {
        mutableStateOf(false)
    }

    Card(modifier = modifier,
        elevation = 3.dp,
        backgroundColor = MaterialTheme.colors.secondary,
        onClick = {
            animationEnabled = true
            onClick()
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
                    .animateContentSize()
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
                    if (subject.hasCredit)
                    {
                        Row(
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                           ) {

                            Text(
                                text = stringResource(R.string.zachet),
                                )

                            val (icon, color) = when
                            {
                                subject.creditPassed != null -> if (subject.creditPassed) Icons.Default.TaskAlt to Colors.Green
                                else Icons.Default.HighlightOff to MaterialTheme.colors.error

                                else                         -> Icons.Default.Schedule to MaterialTheme.colors.onSecondary
                            }

                            Row(verticalAlignment = CenterVertically) {

                                if (subject.creditRetakes != 0)
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
                                            text = subject.creditRetakes.toString(),
                                            style = MaterialTheme.typography.caption,
                                            color = MaterialTheme.colors.error,
                                            modifier = Modifier.align(Center)
                                            )
                                    }
                                }

                                Spacer(modifier = Modifier.width(5.dp))


                                if (subject.creditMark == null)
                                {
                                    Icon(imageVector = icon, tint = color, contentDescription = "")
                                } else
                                {
                                    Text(
                                        text = subject.creditMark.toString(),
                                        style = MaterialTheme.typography.subtitle1,
                                        color = when
                                        {
                                            subject.creditMark in 1..3 -> MaterialTheme.colors.error
                                            subject.creditMark > 3     -> Colors.Green
                                            else                       -> MaterialTheme.colors.onSecondary
                                        },
                                        modifier = Modifier.padding(end = 5.dp)
                                        )
                                }

                            }
                        }
                    }

                    if (subject.hasExam)
                    {
                        Row(
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                           ) {

                            Text(
                                text = stringResource(R.string.exam),
                                style = MaterialTheme.typography.body1
                                )

                            Spacer(modifier = Modifier.width(5.dp))

                            Row(verticalAlignment = CenterVertically) {

                                if (subject.examRetakes != 0)
                                {
                                    Box {
                                        Icon(
                                            imageVector = Icons.Default.Repeat,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .align(Center)
                                                .size(28.dp)
                                            )
                                        Text(
                                            text = subject.examMark.toString(),
                                            style = MaterialTheme.typography.caption,
                                            color = MaterialTheme.typography.body1.color,
                                            modifier = Modifier.align(Center)
                                            )
                                    }
                                }

                                if (subject.examMark != null)
                                {
                                    Text(
                                        text = subject.examMark.toString(),
                                        style = MaterialTheme.typography.subtitle1,
                                        color = when
                                        {
                                            subject.examMark in 1..3 -> MaterialTheme.colors.error
                                            subject.examMark > 3     -> Colors.Green
                                            else                     -> MaterialTheme.colors.onSecondary
                                        },
                                        modifier = Modifier.padding(end = 5.dp)
                                        )
                                } else
                                {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        tint = MaterialTheme.colors.onSecondary,
                                        contentDescription = ""
                                        )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HoursPanel(subject: Subject, modifier: Modifier = Modifier)
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