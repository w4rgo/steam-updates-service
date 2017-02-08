package steamupdates

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    val configData = readConfig("config.json")
    createDatabase(configData)
    setupWebApi()
    ScheduleSteamCmdQueries(configData)
}

private fun ScheduleSteamCmdQueries(configData: ConfigData) {
    val minutes : Long =  configData.steamcmd.refreshMinutes
    val scheduler = Executors.newScheduledThreadPool(1)
    scheduler.scheduleAtFixedRate({ QuerySteamCmdForAllGames(configData.steamcmd.path) }, 0, minutes, TimeUnit.SECONDS)
}
