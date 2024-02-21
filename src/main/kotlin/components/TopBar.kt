package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import configs.WindowConfig

/**
 * TODO
 * @author setruth
 * @date 2024/2/12
 * @time 14:18
 */
@Composable
fun FrameWindowScope.TopBar(
    windowState: WindowState,
    isDark: Boolean,
    themeChange: (Boolean) -> Unit,
    exitApplication: () -> Unit,
) = WindowDraggableArea(
    modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onDoubleTap = {
                windowZoom(windowState)
            }
        )
    }
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxHeight().weight(1f)) {
            Image(modifier = Modifier.size(30.dp), painter = painterResource("wifi.svg"), contentDescription = null)
            Text(modifier = Modifier.padding(horizontal = 10.dp).weight(1f), text = WindowConfig.NAME)
        }

        WindowsControl(windowState, close = exitApplication) {
            ControlItem(
                onclick = {
                    themeChange(!isDark)
                }
            ) {
                AnimatedVisibility(isDark){
                    Icon(painterResource("month.svg"), contentDescription = null)
                }
                AnimatedVisibility(!isDark){
                    Icon(painterResource("sun.svg"), contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun WindowsControl(windowState: WindowState, close: () -> Unit, startContent: @Composable () -> Unit) = Row {
    startContent()
    //min
    ControlItem(
        onclick = {
            windowState.isMinimized = true
        }
    ) {
        Image(painter = painterResource("min.svg"), contentDescription = null)
    }
    //max
    ControlItem(
        onclick = {
            windowZoom(windowState)
        }
    ) {
        if (windowState.placement == WindowPlacement.Floating) {
            Image(painter = painterResource("max.svg"), contentDescription = null)
        } else {
            Image(painter = painterResource("restore.svg"), contentDescription = null)
        }

    }
    //close
    ControlItem(
        modifier = Modifier
            .clip(RoundedCornerShape(topEnd = 10.dp)),
        onclick = {
            close()
        }
    ) {
        Icon(painterResource("close.svg"), contentDescription = null, tint = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun ControlItem(modifier: Modifier = Modifier, onclick: () -> Unit, content: @Composable () -> Unit) = Column(
    modifier = modifier
        .size(35.dp)
        .clickable { onclick() },
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Box(Modifier.padding(8.dp)) {
        content()
    }
}

private fun windowZoom(windowState: WindowState) {
    if (windowState.placement == WindowPlacement.Floating) {
        windowState.placement = WindowPlacement.Maximized
    } else {
        windowState.placement = WindowPlacement.Floating
    }
}