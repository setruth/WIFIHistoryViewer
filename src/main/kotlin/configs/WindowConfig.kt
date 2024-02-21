package configs

import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState

/**
 * TODO
 * @author setruth
 * @date 2024/2/12
 * @time 14:13
 */
object WindowConfig {

    val windowState = WindowState(
        position = WindowPosition.Aligned(Alignment.Center),
        width = 1000.dp,
        height = 700.dp
    )
    const val NAME="WIFI历史查看器"
}