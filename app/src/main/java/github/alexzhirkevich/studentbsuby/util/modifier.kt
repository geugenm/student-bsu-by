package github.alexzhirkevich.studentbsuby.util

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import kotlin.random.Random

private const val bsuPatternUnit = 20
private const val bsuPatternDelta = 3
private const val bsuPatternStrokeWidth = 1f

fun Modifier.applyIf(boolean: Boolean, action: (Modifier) -> Modifier) = composed {
    if (boolean) action(this)
    else this
}

fun Modifier.bsuBackgroundPattern(
    color: Color,
    clip: Boolean = true,
    scale: Float = 1f,
    horizontalCycles: Float = 1f,
    verticalCycles: Float = 1f,
                                 ): Modifier = composed {


    val sizeUnit = LocalDensity.current.density * bsuPatternUnit * scale
    val delta = LocalDensity.current.density * bsuPatternDelta * scale
    val stroke = LocalDensity.current.density * bsuPatternStrokeWidth * scale

    fun DrawScope.drawParralelepiped1(
        color: Color, sizeUnit: Float, delta: Float, strokeWidth: Float
                                     )
    {
        val start = Offset(0f, 2 * sizeUnit + delta)
        val end1 = Offset(2 * sizeUnit, 2 * sizeUnit + delta)
        drawLine(
            color = color, start = start, end = end1, strokeWidth = strokeWidth
                )
        val end2 = Offset(end1.x + sizeUnit, end1.y + sizeUnit)
        drawLine(
            color = color, start = end1, end = end2, strokeWidth = strokeWidth
                )
        val end3 = Offset(end2.x - sizeUnit * 2, end2.y)
        drawLine(
            color = color, start = end2, end = end3, strokeWidth = strokeWidth
                )
        drawLine(
            color = color, start = end3, end = start, strokeWidth = strokeWidth
                )
    }

    fun DrawScope.drawParralelepiped2(
        color: Color, sizeUnit: Float, delta: Float, strokeWidth: Float
                                     )
    {
        val start = Offset(2 * sizeUnit + delta, 0f)
        val end1 = Offset(start.x, start.y + 2 * sizeUnit)
        drawLine(
            color = color, start = start, end = end1, strokeWidth = strokeWidth
                )
        val end2 = Offset(end1.x + sizeUnit, end1.y + sizeUnit)
        drawLine(
            color = color, start = end1, end = end2, strokeWidth = strokeWidth
                )
        val end3 = Offset(end2.x, end2.y - 2 * sizeUnit)
        drawLine(
            color = color, start = end2, end = end3, strokeWidth = strokeWidth
                )
        drawLine(
            color = color, start = end3, end = start, strokeWidth = strokeWidth
                )
    }

    fun DrawScope.drawPattern(
        color: Color, sizeUnit: Float, delta: Float, strokeWidth: Float
                             )
    {
        drawParralelepiped1(color, sizeUnit, delta, strokeWidth)
        drawRect(
            color = color,
            topLeft = Offset(sizeUnit, sizeUnit),
            size = Size(sizeUnit, sizeUnit),
            style = Stroke(width = strokeWidth)
                )
        drawParralelepiped2(color, sizeUnit, delta, strokeWidth)
    }

    fun DrawScope.drawAll(
        color: Color, sizeUnit: Float, delta: Float, strokeWidth: Float
                         )
    {
        translate(-2 * (sizeUnit + delta) + delta, -4 * (sizeUnit + delta) + delta) {

            val sizeWithOffset = 4 * (sizeUnit + delta)
            val deltaOffcet = sizeWithOffset / 2
            repeat(2) {
                val offset = if (it == 1) deltaOffcet else 0f
                translate(offset, offset) {
                    for (i in 0..(size.width / sizeWithOffset * horizontalCycles).toInt() + 1)
                    {
                        for (j in 0..(size.height / sizeWithOffset * verticalCycles).toInt() + 1)
                        {
                            translate(
                                sizeWithOffset * i, sizeWithOffset * j
                                     ) {
                                drawPattern(
                                    color, sizeUnit, delta, strokeWidth
                                           )
                            }
                        }
                    }
                }
            }
        }
    }

    drawBehind {
        if (clip)
        {
            clipRect {
                drawAll(color, sizeUnit, delta, stroke)
            }
        } else
        {
            drawAll(color, sizeUnit, delta, stroke)
        }
    }
}


fun Modifier.animatedSquaresBackground(
    color: Color, count: Int, size: Dp = 100.dp
                                      ) = composed {
    val transition = rememberInfiniteTransition()

    val startPoints = rememberSaveable {
        val random = Random(System.currentTimeMillis())
        List(count) {
            random.nextFloat() to random.nextFloat()
        }
    }


    val angles = rememberSaveable {
        val random = Random(System.currentTimeMillis())
        List(count) {
            360 * random.nextFloat()
        }
    }
    val velocities = rememberSaveable {
        val random = Random(System.currentTimeMillis())
        List(count) {
            random.nextInt(5, 15).toFloat()
        }
    }

    val directions = rememberSaveable {
        val random = Random(System.currentTimeMillis())
        List(count) {
            if (random.nextBoolean()) 1f else -1f
        }
    }

    val rotations = (0 until count).map {
        transition.animateFloat(
            initialValue = angles[it] * directions[it],
            targetValue = (360 + angles[it]) * directions[it],
            animationSpec = infiniteRepeatable(
                tween(
                    durationMillis = (10000 * velocities[it]).roundToInt(), easing = LinearEasing
                     )
                                              )
                               )
    }
    val actualSize = size.value * LocalDensity.current.density
    drawBehind {
        clipRect {
            repeat(count) {
                translate(
                    left = this.size.width * startPoints[it].first - actualSize / 2,
                    top = this.size.height * startPoints[it].second - actualSize / 2
                         ) {
                    rotate(
                        rotations[it].value, pivot = Offset(actualSize / 2, actualSize / 2)
                          ) {
                        drawRect(color = color, size = Size(actualSize, actualSize))
                    }
                }
            }
        }
    }
}