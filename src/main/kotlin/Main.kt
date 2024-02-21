import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.EmptyLayout
import components.TopBar
import theme.AppTheme
import components.WIFIInfo
import configs.WindowConfig
import utils.CMD

@OptIn(ExperimentalMaterial3Api::class)
fun main() = application {
    Window(
        state = WindowConfig.windowState,
        icon = painterResource("wifi.svg"),
        transparent = true,
        undecorated = true,
        resizable = false,
        title = WindowConfig.NAME,
        onCloseRequest = ::exitApplication
    ) {
        var activeName by remember {
            mutableStateOf<String?>(null)
        }
        var filterQuery by remember {
            mutableStateOf("")
        }
        val allWifiNames = remember {
            mutableStateListOf<String>()
        }
        val darkMode=isSystemInDarkTheme()
        var isDark by remember {
            mutableStateOf(darkMode)
        }

        LaunchedEffect(Unit) {
            CMD.executeCommand("netsh wlan show profiles").WIFINameFilter("所有用户配置文件").forEach {
                allWifiNames.add(it)
            }
        }
        AppTheme(useDarkTheme = isDark) {
            Surface(shape = RoundedCornerShape(10.dp)) {
                Column(modifier = Modifier.fillMaxSize().padding(bottom = 10.dp, start = 10.dp)) {
                    TopBar(WindowConfig.windowState, isDark = isDark, themeChange = {
                        isDark=it
                    }) {
                        exitApplication()
                    }
                    // head
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(bottom = 10.dp, end = 10.dp)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .height(30.dp)
                                .width(10.dp)
                                .padding(top = 0.dp)
                                .background(MaterialTheme.colorScheme.primary)
                        )
                        Text(
                            activeName ?: "(暂无选择)",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp).weight(1f)
                        )
                        SearchBar(
                            modifier = Modifier.padding(top = 0.dp),
                            query = filterQuery,
                            onQueryChange = {
                                filterQuery = it
                            },
                            onActiveChange = {},
                            active = false,
                            onSearch = {},
                            placeholder = { Text("WIFI过滤") },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = null)
                            },
                            trailingIcon = {
                                Image(modifier = Modifier.size(30.dp), painter = painterResource("wifi.svg"),  contentDescription = null)
                            }
                        ) {

                        }
                    }
                    //body
                    Row(modifier = Modifier.fillMaxSize().weight(1f).padding(end = 10.dp)) {
                        //Wi-Fi list info
                        WIFIList(allWifiNames.filter { it.indexOf(filterQuery) != -1 || filterQuery == "" }) { clickName ->
                            activeName = clickName
                        }
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .weight(1f)
                                .animateContentSize()
                                .padding(top = 5.dp, start = 6.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                            ),
                        ) {
                            AnimatedVisibility(visible = activeName != null) {
                                activeName?.let {
                                    WIFIInfo(it)
                                }
                            }
                            AnimatedVisibility(visible = activeName == null) {
                                EmptyLayout()
                            }
                        }

                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WIFIList(wifiNames: List<String>, onclick: (String?) -> Unit) {
    var activeIndex by remember {
        mutableStateOf(-1)
    }
    LazyColumn {
        itemsIndexed(wifiNames) { index, wifiName ->
            val (contentColor, containerColor) = if (activeIndex == index) {
                Pair(MaterialTheme.colorScheme.onTertiaryContainer, MaterialTheme.colorScheme.tertiaryContainer)
            } else {
                Pair(MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.surface)
            }
            Card(
                onClick = {
                    if (activeIndex == index) {
                        onclick(null)
                        activeIndex = -1

                    } else {
                        activeIndex = index
                        onclick(wifiName)
                    }


                },
                modifier = Modifier.width(120.dp).padding(vertical = 3.dp),
                colors = CardDefaults.cardColors(
                    contentColor = contentColor,
                    containerColor = containerColor,
                )
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp).fillMaxSize(),
                ) {
                    Text(text = wifiName, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
        }
    }
}

private infix fun List<String>.WIFINameFilter(key: String) = run {
    filter { it.contains(key) }
        .map { it.substringAfter(":").trim() }
}
