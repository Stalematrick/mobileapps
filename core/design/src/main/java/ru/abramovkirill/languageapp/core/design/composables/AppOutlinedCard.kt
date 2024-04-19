@file:OptIn(ExperimentalFoundationApi::class)

package ru.abramovkirill.languageapp.core.design.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.styles.Sizes

@Composable
fun AppOutlinedCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    color: Color = AppTheme.colors.primary,
    containerColor: Color = Color.Transparent,
    content: @Composable (ColumnScope.() -> Unit),
) {
    val borderWidth = Sizes.MenuItemBorderWidth
    
    OutlinedCard(
        colors = CardDefaults.outlinedCardColors(
            containerColor = containerColor,
            contentColor = color,
        ),
        border = BorderStroke(
            width = borderWidth,
            color = color,
        ),
        modifier = modifier
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick
            ),
        content = content,
    )
}

@Composable
@Preview
private fun AppOutlinedCardPreview() {
    Box(
        modifier = Modifier
            .padding(50.dp)
    ) {
        AppOutlinedCard(
            onClick = {  },
            modifier = Modifier
                .size(200.dp, 300.dp)
        ) {

        }
    }
}
