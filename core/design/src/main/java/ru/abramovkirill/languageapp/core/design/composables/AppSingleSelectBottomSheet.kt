@file:OptIn(ExperimentalMaterial3Api::class)

package ru.abramovkirill.languageapp.core.design.composables

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import ru.abramovkirill.languageapp.core.design.composables.button.AppButton
import ru.abramovkirill.languageapp.core.design.styles.AppTheme

@Composable
fun AppSingleSelectBottomSheet(
    isShown: Boolean,
    title: String,
    items: ImmutableList<String>,
    onClick: (index: Int) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    if (isShown) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = AppTheme.colors.background,
            contentColor = AppTheme.colors.onBackground,
            windowInsets = WindowInsets.navigationBars,
            modifier = modifier,
        ) {
            BottomSheetContent(
                title = title,
                items = items,
                onClick = onClick,
            )
        }
    }
}

@Composable
private fun BottomSheetContent(
    title: String,
    items: ImmutableList<String>,
    onClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        BottomSheetTitle(
            title = title,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        BottomSheetItems(
            items = items,
            onClick = onClick
        )
    }
}

@Composable
private fun BottomSheetTitle(
    title: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = title,
        textAlign = TextAlign.Center,
        style = AppTheme.typography.titleLarge,
        fontWeight = FontWeight.Medium,
        color = AppTheme.colors.onBackground,
        modifier = modifier
            .padding(horizontal = 24.dp),
    )
}

@Composable
private fun BottomSheetItems(
    items: ImmutableList<String>,
    onClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        modifier = modifier,
    ) {
        itemsIndexed(
            items = items,
            key = { index, _ -> index }
        ) { index, item ->
            BottomSheetItem(
                item = item,
                onClick = { onClick(index) },
                includeSpacer = index != items.lastIndex
            )
        }
    }
}

@Composable
private fun BottomSheetItem(
    item: String,
    includeSpacer: Boolean,
    onClick: () -> Unit,
) {
    AppButton(
        text = item,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (includeSpacer) 10.dp else 0.dp)
    )
}

@Composable
private fun AppSingleSelectBottomSheetPreview() {
    AppTheme {
        BottomSheetContent(
            title = "Title",
            items = persistentListOf(
                "Option 1",
                "Option 2",
                "Option 3",
            ),
            onClick = {},
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun AppSingleSelectBottomSheetPreviewLight() {
    AppSingleSelectBottomSheetPreview()
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 0x00000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun AppSingleSelectBottomSheetPreviewDark() {
    AppSingleSelectBottomSheetPreview()
}
