package components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import utils.CMD

/**
 * TODO
 * @author setruth
 * @date 2024/2/12
 * @time 10:53
 */
@Composable
fun WIFIInfo(activeName: String)=Box(modifier = Modifier.fillMaxSize().padding(10.dp)) {
    var infoList by remember {
        mutableStateOf<List<String>>(listOf())
    }
    LaunchedEffect(activeName){
        infoList=  CMD.executeCommand("netsh wlan show profile name=${activeName} key=clear").dropWhile { it != "配置文件信息" }
    }
    if (infoList.isEmpty()){
        Text("暂无配置信息")
    }else{
        Column {
            LazyColumn {
                items(infoList){
                    Text(it)
                }
            }
        }
    }
}