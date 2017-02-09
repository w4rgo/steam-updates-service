package steamupdates

import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.io.*

fun QuerySteamCmdForAllGames(steamCmdPath : String) {
    println("-> Starting query")
    transaction {
        for (game in Game.selectAll()) {
            val processOutputText = querySteamCmd(game[Game.id], steamCmdPath)
            extractBuildId(processOutputText, game[Game.id])
        }
    }
    println("<- Query finished")
}

private fun querySteamCmd(gameId: String, steamCmdPath: String): String {
    println("Using steamcmd to get info about : ${gameId}")
    val steamCmdScript = createSteamCmdScript(gameId, steamCmdPath);
    val process = ProcessBuilder(steamCmdScript.toString(), gameId)
            .start()
    val processOutputText = process.inputStream.reader().readText()
    process.waitFor()
    process.destroy()
    return processOutputText
}

private fun extractBuildId(processOutputText: String, gameId: String) {
    if (processOutputText.length == 0) {
        println("Could't rusfind information about ${gameId}")
        return
    }
    val outputWithoutSpaces = processOutputText
            .replace(" ", "")
            .replace("\t", "")
            .replace("\n", "")
            .replace("\r", "")

    val firstSplit = outputWithoutSpaces.split("\"branches\"{\"public\"{\"buildid\"")
    if (firstSplit.count() <= 1) {
        println("Couldnt split for the build id")
        return
    }

    val secondSplit = firstSplit[1]
            .split("\"")
    if (secondSplit.count() <= 5) {
        print("Couldnt split for the time updated")
        return
    }
    val buildId = secondSplit[1]
    val timeUpdated = secondSplit[5]
    println("saved update ${gameId} : ${buildId} : ${timeUpdated}")
    insertGameUpdate(gameId, buildId, timeUpdated)
}

@Throws(IOException::class)
private fun createSteamCmdScript(gameId: String, steamCmdPath: String): File {
    val tempScript = File.createTempFile("steamCmdTempScript", null)
    tempScript.deleteOnExit()
    PrintWriter(OutputStreamWriter(FileOutputStream(tempScript))).use { printWriter ->
        printWriter.println("#!/bin/bash")
        printWriter.println("rm -r appcache")
        printWriter.println("cd ${steamCmdPath}")
                                               
        printWriter.println("./steamcmd.sh +login anonymous +app_info_update 1 +app_info_print  ${gameId} +app_info_print  ${gameId} +quit")
        printWriter.close()
    }
    tempScript.setExecutable(true)
    return tempScript
}
