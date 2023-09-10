package github.alexzhirkevich.studentbsuby.ui.common

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefreshState
import github.alexzhirkevich.studentbsuby.R

@Composable
fun BsuProgressBar(
    modifier: Modifier = Modifier,
    size: Dp = 50.dp,
    enabled: Boolean = true,
    tint: Color = Color.Unspecified
                  )
{
    val transition = rememberInfiniteTransition()
    val angle = if (enabled) transition.animateFloat(
        initialValue = 0F, targetValue = 360F, animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutLinearInEasing)
                                                                                 )
                                                    ).value else 0f

    Icon(painter = painterResource(id = R.drawable.logo),
        contentDescription = "Loading",
        tint = tint,
        modifier = modifier
            .size(size)
            .graphicsLayer {
                rotationY = angle
            })
}

@Composable
fun BsuProgressBarSwipeRefreshIndicator(state: SwipeRefreshState, trigger: Dp)
{
    val size = 35.dp
    val density = LocalDensity.current.density

    var offsetY by rememberSaveable {
        mutableStateOf(0f)
    }
    val animatedOffsetY by animateFloatAsState(targetValue = offsetY)

    LaunchedEffect(state.isRefreshing) {
        offsetY = (if (state.isRefreshing) (trigger.value * density)
        else (state.indicatorOffset)) - (size.value + 10) * density
    }

    LaunchedEffect(state.indicatorOffset) {
        if (!state.isRefreshing)
        {
            offsetY = state.indicatorOffset - (size.value + 10) * density
        }
    }


    Card(
        backgroundColor = MaterialTheme.colors.secondary,
        shape = CircleShape,
        elevation = 5.dp,
        modifier = Modifier.graphicsLayer {
                translationY = if (state.isSwipeInProgress) offsetY else animatedOffsetY
            },
        ) {

        val progress = minOf(
            1f, if (state.isRefreshing) 1f
            else (state.indicatorOffset / (trigger.value * density))
                            )

        BsuProgressBar(
            modifier = Modifier
                .rotate(360 * progress)
                .alpha(progress)
                .padding(3.dp),
            size = size,
            tint = MaterialTheme.colors.primary,
            enabled = state.isRefreshing
                      )
    }
}