package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import androidx.annotation.StringRes
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import de.charlex.compose.HtmlText
import github.alexzhirkevich.studentbsuby.R
import github.alexzhirkevich.studentbsuby.util.communication.collectAsState
import github.alexzhirkevich.studentbsuby.util.valueOrNull

@Composable
internal fun InfoPage(
    viewModel: PaidServicesViewModel
                     )
{


    @Composable
    fun ColumnScope.InfoBlock(
        @StringRes title: Int,
        @StringRes text: Int,
                             )
    {

        var visible by rememberSaveable {
            mutableStateOf(true)
        }
        Column {

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        visible = !visible
                    }


               ) {
                Text(
                    modifier = Modifier.padding(
                        horizontal = 5.dp, vertical = 3.dp
                                               ),
                    text = stringResource(id = title),
                    style = MaterialTheme.typography.subtitle1,
                    )
                Icon(
                    imageVector = if (visible) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "More",
                    )
            }
            if (visible)
            {
                Spacer(modifier = Modifier.height(5.dp))
                HtmlText(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    textId = text,
                    style = MaterialTheme.typography.body1,
                    urlSpanStyle = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline
                                            )
                        )
            }
        }
    }

    val blocks = listOf(
        R.string.paidservices_phones to R.string.paidservices_phones_text,
        R.string.paidservices_requisites to R.string.paidservices_requisites_text,
        R.string.paidservices_fine to R.string.paidservices_fine_text,
                       )


    val scrollstate = rememberScrollState()

    SelectionContainer {

        Box(
            Modifier
                .fillMaxSize()
                .verticalScroll(scrollstate)
           ) {

            Column(
                Modifier
                    .padding(10.dp)
                    .animateContentSize()
                  ) {

                val paidInfo by viewModel.paidInfoCommunication.collectAsState()

                paidInfo.valueOrNull()?.let {

                    listOf(
                        R.string.contract_number to it.contractNumber,
                        R.string.debt to it.debt,
                        R.string.fine to it.fine
                          ).forEach {
                        val title = stringResource(it.first)
                        val value = it.second.toString()
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = AnnotatedString(
                                text = "$title: $value", spanStyles = listOf(
                                    AnnotatedString.Range(
                                        item = SpanStyle(fontWeight = FontWeight.SemiBold),
                                        start = 0,
                                        end = title.length + 1
                                                         )
                                                                            )
                                                  ),
                            style = MaterialTheme.typography.subtitle1.copy(fontWeight = FontWeight.Normal),
                            )
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    Spacer(
                        modifier = Modifier
                            .padding(5.dp)
                            .height(.5.dp)
                            .fillMaxWidth()
                            .background(MaterialTheme.colors.onBackground)
                          )
                }

                blocks.forEach {
                    InfoBlock(
                        title = it.first, text = it.second
                             )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                Box(Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.handle(PaidServicesEvent.EripHelpClicked)
                        }, modifier = Modifier.align(Alignment.Center)
                          ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                tint = MaterialTheme.colors.onPrimary,
                                contentDescription = "Payment help"
                                )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = stringResource(id = R.string.erip_help),
                                color = MaterialTheme.colors.onPrimary
                                )
                        }
                    }
                }

                Spacer(modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding())
            }
        }
    }
}
