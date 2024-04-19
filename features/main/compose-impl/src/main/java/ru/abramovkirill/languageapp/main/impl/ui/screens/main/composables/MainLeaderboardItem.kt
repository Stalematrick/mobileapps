package ru.abramovkirill.languageapp.main.impl.ui.screens.main.composables

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.abramovkirill.languageapp.common.utils.extract
import ru.abramovkirill.languageapp.common.utils.painterRes
import ru.abramovkirill.languageapp.common.utils.strRes
import ru.abramovkirill.languageapp.core.design.composables.AppPainterWrapper
import ru.abramovkirill.languageapp.core.design.composables.shimmerEffect
import ru.abramovkirill.languageapp.core.design.styles.AppTheme
import ru.abramovkirill.languageapp.main.impl.R
import ru.abramovkirill.languageapp.main.impl.ui.screens.main.MainScreenContract
import ru.abramovkirill.languageapp.core.design.R as DesignR

@Composable
fun MainLeaderboardItem(
    leader: MainScreenContract.State.Leaderboard.Item,
    includeSpacer: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = if (includeSpacer) 10.dp else 0.dp)
            .background(
                color = AppTheme.colors.surface,
                shape = AppTheme.shapes.extraLarge
            )
            .padding(16.dp)
    ) {
        AppPainterWrapper(
            painterResource = leader.avatar
                ?: painterRes(DesignR.drawable.ic_avatar_placeholder),
            loadingContent = {
                Box(
                    modifier = Modifier
                        .size(LeaderAvatarSize)
                        .background(
                            color = AppTheme.colors.shimmerForeground,
                            shape = AppTheme.shapes.small,
                        )
                )
            },
            successContent = { painter ->
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(LeaderAvatarSize)
                        .clip(AppTheme.shapes.small)
                )
            }
        )

        Spacer(modifier = Modifier.width(24.dp))

        Text(
            text = leader.fullName?.extract() ?: stringResource(DesignR.string.profile_name_unknown),
            style = AppTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
            modifier = Modifier
                .weight(2f),
        )

        Text(
            text = stringResource(R.string.main_points_formatted, leader.totalScore),
            style = AppTheme.typography.bodyLarge,
            maxLines = 1,
            fontSize = 17.sp,
            fontWeight = FontWeight.Medium,
            color = AppTheme.colors.onBackground,
        )
    }
}

@Composable
fun MainLeaderboardItemShimmer(
    includeSpacer: Boolean = false,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(bottom = if (includeSpacer) 10.dp else 0.dp)
            .clip(AppTheme.shapes.extraLarge)
            .shimmerEffect()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(LeaderAvatarSize)
                .background(
                    color = AppTheme.colors.shimmerForeground,
                    shape = AppTheme.shapes.small,
                )
        )

        Spacer(modifier = Modifier.width(24.dp))

        Box(
            modifier = Modifier
                .size(
                    width = 100.dp,
                    height = 17.dp
                )
                .background(
                    color = AppTheme.colors.shimmerForeground,
                    shape = AppTheme.shapes.small,
                )
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(
                    width = 70.dp,
                    height = 17.dp
                )
                .background(
                    color = AppTheme.colors.shimmerForeground,
                    shape = AppTheme.shapes.small,
                )
        )
    }
}

private val LeaderAvatarSize = 36.dp

@Composable
private fun MainLeaderboardItemPreview() {
    AppTheme {
        Column {
            MainLeaderboardItemShimmer(includeSpacer = false)

            Spacer(modifier = Modifier.height(10.dp))

            MainLeaderboardItem(
                leader = MainScreenContract.State.Leaderboard.Item(
                    id = "1",
                    fullName = strRes("Ivan Ivanov"),
                    avatar = painterRes(DesignR.drawable.ic_avatar_placeholder),
                    totalScore = 10f
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            MainLeaderboardItem(
                leader = MainScreenContract.State.Leaderboard.Item(
                    id = "1",
                    fullName = strRes("Thebigname ThebigLastName"),
                    avatar = painterRes(DesignR.drawable.ic_avatar_placeholder),
                    totalScore = 10f
                )
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun MainLeaderboardItemPreviewLight() {
    MainLeaderboardItemPreview()
}

@Composable
@Preview(
    showBackground = true,
    backgroundColor = 0x000000,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
private fun MainLeaderboardItemPreviewDark() {
    MainLeaderboardItemPreview()
}
