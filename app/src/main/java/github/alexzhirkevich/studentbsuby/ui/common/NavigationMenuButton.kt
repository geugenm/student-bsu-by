package github.alexzhirkevich.studentbsuby.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun NavigationMenuButton(
    icon: ImageVector = Icons.Default.Menu, contentDescription: String = "Menu", onClick: () -> Unit
                        )
{
    IconButton(
        onClick = onClick, modifier = Modifier.padding(3.dp)
              ) {
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colors.onSecondary,
            contentDescription = contentDescription
            )
    }
}