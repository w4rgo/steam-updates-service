package steamupdates
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

data class ConfigData(val steamcmd: SteamCmdData, val database: DatabaseData, val games: List<GameData>)
data class SteamCmdData(val path: String, val refreshMinutes: Long)
data class DatabaseData(val url: String, val user: String, val pass : String)
data class GameData(val name: String, val steamGameId: String)

fun readConfig(path : String) : ConfigData {
    val JSON = jacksonObjectMapper()
    val file = File(path)
    val configData  = JSON.readValue<ConfigData>(file)
    return configData
}

