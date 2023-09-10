package github.alexzhirkevich.studentbsuby.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun DefaultTextInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textInputModifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: TextStyle = LocalTextStyle.current,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions(),
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
                    )
{
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colors.background),
        verticalAlignment = Alignment.CenterVertically
       ) {

        leadingIcon?.let {
            Box(
                modifier = Modifier.padding(
                    top = 10.dp,
                    start = 15.dp,
                    bottom = 10.dp,
                                           )
               ) {
                it.invoke()
            }
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(10.dp)
           ) {

            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                enabled = enabled,
                readOnly = readOnly,
                singleLine = singleLine,
                textStyle = textStyle,
                visualTransformation = visualTransformation,
                keyboardActions = keyboardActions,
                keyboardOptions = keyboardOptions,
                maxLines = maxLines,
                interactionSource = interactionSource,
                modifier = textInputModifier.fillMaxWidth(),
                cursorBrush = SolidColor(MaterialTheme.colors.onBackground)
                          )

            if (value.isEmpty())
            {
                placeholder?.invoke()
            }
        }
        trailingIcon?.let {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(
                        top = 10.dp, bottom = 10.dp, end = 15.dp
                            )

               ) {

                it.invoke()
            }
        }
    }
}