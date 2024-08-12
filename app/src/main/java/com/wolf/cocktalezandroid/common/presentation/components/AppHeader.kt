import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wolf.cocktalezandroid.common.presentation.theme.CocktalezTheme
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHeader(
    modifier: Modifier = Modifier,
    showBackArrow: Boolean = false,
    onBackArrowClicked: () -> Unit = {},
    navActions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit = {}
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                title()
            }
        },
        navigationIcon = {
            if (showBackArrow) {
                IconButton(
                    onClick = onBackArrowClicked
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        },
        actions = navActions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Preview
@Composable
fun StandardToolbarPreview() {
    CocktalezTheme {
        AppHeader(
            title = {
                Text(
                    text = "About",
                )
            }
        )
    }
}
