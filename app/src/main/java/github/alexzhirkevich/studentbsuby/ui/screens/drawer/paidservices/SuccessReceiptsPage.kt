package github.alexzhirkevich.studentbsuby.ui.screens.drawer.paidservices

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import github.alexzhirkevich.studentbsuby.R

@Composable
fun LazyItemScope.Header(text: String)
{
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
       ) {

        Box(
            Modifier
                .align(Alignment.Center)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colors.background.copy(alpha = .9f))
                .padding(vertical = 5.dp, horizontal = 10.dp)
           ) {
            Text(
                text = text,
                style = MaterialTheme.typography.body1,
//                fontWeight = FontWeight.,
                )
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun <T> SuccessReceiptsPage(
    receipts: List<T>,
    header: ((T) -> String)? = null,
    complete: (T) -> Boolean,
    widget: @Composable (T) -> Unit
                           )
{
    SelectionContainer {

        fun LazyListScope.Items(items: List<T>)
        {
            header?.let {
                items.groupBy(header).forEach {
                    stickyHeader {
                        Header(text = it.key)
                    }
                    items(it.value.size) { idx ->
                        widget(it.value[idx])
                    }
                }
            } ?: items(items.size) { idx ->
                widget(items[idx])
            }
        }

        LazyColumn(
            Modifier.fillMaxSize()
                  ) {

            receipts.groupBy(complete).let {
                it[false]?.let { receipts ->
                    stickyHeader {
                        Header(text = stringResource(id = R.string.bills))

                    }
                    Items(receipts)
                }

                it[true]?.let { bills ->
                    stickyHeader {
                        Header(text = stringResource(id = R.string.history))
                    }
                    Items(bills)
                    item {
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                }


            }
            item { Spacer(modifier = Modifier.navigationBarsPadding().imePadding()) }
        }
    }
}