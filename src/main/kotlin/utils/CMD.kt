package utils

import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.charset.Charset

/**
 * TODO
 * @author setruth
 * @date 2024/2/12
 * @time 10:06
 */
object CMD {
    private val runtime = Runtime.getRuntime()

    fun executeCommand(command: String): List<String> {
        val process = runtime.exec(command)
        val reader =  BufferedReader(InputStreamReader(process.inputStream, Charset.forName("GBK")))
        var line: String?
        val outputList = mutableListOf<String>()

        while (reader.readLine().also { line = it } != null) {
            outputList.add(line!!)
        }

        return outputList
    }
}
