package components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

/**
 * TODO
 * @author setruth
 * @date 2024/2/12
 * @time 10:59
 */
@Composable
fun EmptyLayout() = Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Image(painter = painterResource("empty.svg"), contentDescription = "")
    }
