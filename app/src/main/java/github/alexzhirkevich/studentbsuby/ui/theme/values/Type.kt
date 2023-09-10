package github.alexzhirkevich.studentbsuby.ui.theme.values

import androidx.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import github.alexzhirkevich.studentbsuby.R

val UbuntuFontFamily = FontFamily(
    Font(R.font.ubuntu_regular),
    Font(R.font.ubuntu_italic, style = FontStyle.Italic),
    Font(R.font.ubuntu_bold, weight = FontWeight.Bold),
    Font(R.font.ubuntu_bold_italic, weight = FontWeight.Bold, style = FontStyle.Italic),
    Font(R.font.ubuntu_medium, weight = FontWeight.Medium),
    Font(R.font.ubuntu_medium_italic, weight = FontWeight.Medium, style = FontStyle.Italic),
    Font(R.font.ubuntu_light, weight = FontWeight.Light),
    Font(R.font.ubuntu_light_italic, weight = FontWeight.Light, style = FontStyle.Italic),
                                 )


fun typography(color: Color) = Typography(
    defaultFontFamily = UbuntuFontFamily,
    body1 = TextStyle(
        fontSize = 16.sp, color = color
                     ),
    body2 = TextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Colors.Gray,
                     ),
    caption = TextStyle(
        fontSize = 13.sp, fontWeight = FontWeight.Light, color = Colors.Gray
                       ),
    subtitle1 = TextStyle(
        fontWeight = FontWeight.Medium, fontSize = 20.sp, color = color
                         ),
    h1 = TextStyle(
        fontWeight = FontWeight.Bold, fontSize = 70.sp, color = color
                  ),
    h2 = TextStyle(
        fontWeight = FontWeight.Bold, fontSize = 32.sp, color = color
                  )/* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
                                         )