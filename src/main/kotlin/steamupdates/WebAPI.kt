package steamupdates

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import spark.Request
import spark.Response
import spark.Spark

data class GameUpdateData(val buildId: String, val timestamp: String)


fun setupWebApi() {
    Spark.get("/gameUpdates/:gameId") { req, res -> ReturnLastTenBuildsForGame(req, res) }
}

fun ReturnLastTenBuildsForGame(req: Request, res: Response): Any? {
    val gameId = req.params(":gameId")
    val updates = transaction {
        val updates = GameUpdate.select { GameUpdate.gameId.eq(gameId) }.orderBy(GameUpdate.timeStamp).take(10)
        return@transaction updates
    }
    val convertedUpdated = updates.map { GameUpdateData(it[GameUpdate.buildId],it[GameUpdate.timeStamp]) }
    val JSON = jacksonObjectMapper()
    val jsonOutput = JSON.writeValueAsString(convertedUpdated)

    return jsonOutput
}
