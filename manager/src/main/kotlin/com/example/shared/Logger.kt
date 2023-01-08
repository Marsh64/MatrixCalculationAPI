package shared

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Logger {

    fun info(message : String){
        println("${nowStamp()}  $message")
    }

    private fun nowStamp(): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
        return "\u001B[93m" + LocalDateTime.now().format(formatter) + "\u001b[0m"
    }
}