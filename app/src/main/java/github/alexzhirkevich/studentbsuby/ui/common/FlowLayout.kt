package github.alexzhirkevich.studentbsuby.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.constrainHeight
import kotlin.math.roundToInt

@Composable
fun FlowBox(
    elementWidth: Dp, modifier: Modifier = Modifier, content: @Composable () -> Unit
           )
{
    Layout(
        modifier = modifier,
        content = content,
          ) { measurables, constraints ->

        val columns = (constraints.maxWidth / elementWidth.roundToPx())
        val widthPx = (constraints.maxWidth / columns.toFloat()).roundToInt()
        val itemConstraints = constraints.copy(
            minHeight = 0, minWidth = widthPx, maxWidth = widthPx
                                              )

        val placeables = measurables.map {
            it.measure(itemConstraints)
        }
        val columnsHeight = MutableList(columns) { 0 }
        val offsets = MutableList(placeables.size) { IntOffset(0, 0) }

        placeables.forEachIndexed { placeableIndex, placeable ->
            val minHeight = columnsHeight.minOrNull()!!
            val index = columnsHeight.indexOf(minHeight)

            offsets[placeableIndex] = IntOffset((widthPx) * index, minHeight)
            columnsHeight[index] += placeable.height
        }
        layout(constraints.maxWidth, columnsHeight.maxOrNull()?.let {
            constraints.constrainHeight(it)
        } ?: constraints.maxHeight) {
            placeables.forEachIndexed { index, placeable ->
                placeable.place(offsets[index])
            }
        }
    }
}