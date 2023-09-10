package github.alexzhirkevich.studentbsuby.ui.screens.drawer

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

class ProfileRoundShape : Shape
{
    override fun createOutline(
        size: Size, layoutDirection: LayoutDirection, density: Density
                              ): Outline
    {
        val r = 20 * density.density
        return Outline.Rounded(
            RoundRect(
                0f, 0f, size.width, size.height,
                bottomRightCornerRadius = CornerRadius(r, r),
                bottomLeftCornerRadius = CornerRadius(r, r),
                     )
                              )
    }

}
