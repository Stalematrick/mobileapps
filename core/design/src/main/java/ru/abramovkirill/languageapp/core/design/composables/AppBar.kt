@file:OptIn(ExperimentalMaterial3Api::class)

package ru.abramovkirill.languageapp.core.design.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import ru.abramovkirill.languageapp.common.utils.PainterResource
import ru.abramovkirill.languageapp.common.utils.StringResource
import ru.abramovkirill.languageapp.common.utils.extract
import ru.abramovkirill.languageapp.common.utils.painterRes
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.R
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.core.design.styles.Sizes

@Composable
fun AppBar(
    title: String,
    modifier: Modifier = Modifier,
    titleGravity: AppBarTitleGravity = AppBarTitleGravity.START,
    actions: List<AppBarActionItem> = emptyList(),
    containerColor: Color = AppTheme.colors.primary,
    contentColor: Color = AppTheme.colors.onPrimary,
    onGoBackClick: (() -> Unit)? = null,
) {
    when (titleGravity) {
        AppBarTitleGravity.START -> {
            StartAlignedAppBar(
                title = title,
                actions = actions,
                containerColor = containerColor,
                contentColor = contentColor,
                onGoBackClick = onGoBackClick,
                modifier = modifier,
            )
        }
        AppBarTitleGravity.CENTER -> {
            CenterAlignedAppBar(
                title = title,
                actions = actions,
                containerColor = containerColor,
                contentColor = contentColor,
                onGoBackClick = onGoBackClick,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun CenterAlignedAppBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: List<AppBarActionItem> = emptyList(),
    containerColor: Color = AppTheme.colors.primary,
    contentColor: Color = AppTheme.colors.onPrimary,
    onGoBackClick: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = {
            AppBarTitle(
                title = title,
                onGoBackClick = onGoBackClick,
                modifier = Modifier
                    .wrapContentSize()
            )
        },
        navigationIcon = {
            AppBarNavigationIcon(
                onGoBackClick = onGoBackClick,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor,
        ),
        actions = {
            AppBarActions(actions = actions)
        },
        modifier = modifier
    )
}

@Composable
private fun StartAlignedAppBar(
    title: String,
    modifier: Modifier = Modifier,
    actions: List<AppBarActionItem> = emptyList(),
    containerColor: Color = AppTheme.colors.primary,
    contentColor: Color = AppTheme.colors.onPrimary,
    onGoBackClick: (() -> Unit)? = null,
) {
    TopAppBar(
        title = {
            AppBarTitle(
                title = title,
                onGoBackClick = onGoBackClick,
                modifier = Modifier
                    .wrapContentSize()
            )
        },
        navigationIcon = {
            AppBarNavigationIcon(
                onGoBackClick = onGoBackClick,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor,
            navigationIconContentColor = contentColor,
            titleContentColor = contentColor,
            actionIconContentColor = contentColor,
        ),
        actions = {
            AppBarActions(actions = actions)
        },
        modifier = modifier
    )
}

@Composable
private fun AppBarTitle(
    title: String,
    onGoBackClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        if (onGoBackClick != null) {
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = AppTheme.typography.titleMedium,
            modifier = Modifier
        )
    }
}

@Composable
private fun AppBarNavigationIcon(
    onGoBackClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    if (onGoBackClick != null) {
        IconButton(
            onClick = { onGoBackClick.invoke() },
            modifier = modifier
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_left_20),
                contentDescription = stringResource(R.string.go_back_icon_content_description),
                modifier = Modifier
                    .width(Sizes.AppBarIcon)
            )
        }
    }
}

@Composable
private fun AppBarActions(
    actions: List<AppBarActionItem>,
) {
    actions.forEach { action ->
        IconButton(
            onClick = action.onClick
        ) {
            Icon(
                painter = action.icon.extract(),
                contentDescription = action.contentDescription.extract(),
                modifier = Modifier
                    .width(Sizes.AppBarIcon)
            )
        }
    }
}

data class AppBarActionItem(
    val icon: PainterResource,
    val contentDescription: StringResource,
    val onClick: () -> Unit,
)

enum class AppBarTitleGravity {
    START,
    CENTER;
}

private class AppBarTitleGravityProvider : PreviewParameterProvider<AppBarTitleGravity> {
    override val values = sequenceOf(
        AppBarTitleGravity.START,
        AppBarTitleGravity.CENTER
    )
}

@Composable
@Preview
private fun AppBarPreview(
    @PreviewParameter(AppBarTitleGravityProvider::class) titleGravity: AppBarTitleGravity,
) {
    AppTheme {
        AppBar(
            title = "Какой-то экран",
            titleGravity = titleGravity,
            actions = listOf(
                AppBarActionItem(
                    icon = painterRes(R.drawable.ic_logo_28),
                    contentDescription = strRes(""),
                    onClick = {},
                )
            ),
            onGoBackClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}
