package github.alexzhirkevich.studentbsuby.ui.screens.drawer

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

object DrawerShape : Shape
{

    const val width = 350

    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
                              ): Outline
    {

        return Outline.Rounded(
            RoundRect(
                0f,
                0f,
                minOf(size.width, width * density.density),
                size.height,
                bottomRightCornerRadius = CornerRadius(x = size.width / 2, y = size.height / 2)
                     )
                              )
    }

}
