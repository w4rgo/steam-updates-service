package steamupdates

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Game : Table() {
    val id = varchar("steam_game_id", length = 50).primaryKey() // Column<String>
    val name = varchar("name", length = 50) // Column<String>
}

object GameUpdate : Table() {
    val buildId = varchar("buildid", 50).primaryKey() // Column<String>
    val timeStamp = varchar("timestamp", 50) // Column<String>
    val gameId = (varchar("game_id", 50) references Game.id).nullable() // Column<Int?>
}

fun createDatabase(configData: ConfigData) {
    connectDatabase(configData)
    transaction {
        SchemaUtils.create(Game, GameUpdate)

        for (game in configData.games) {
            Game.insertIgnore {
                it[name] = game.name
                it[id] = game.steamGameId
            }
        }

        for (game in Game.selectAll()) {
            println("Game in DB: ${game[Game.id]}: ${game[Game.name]} : ${game[Game.id]}")
        }
    }
}

private fun connectDatabase(configData: ConfigData) {
    val url = configData.database.url
    val user = configData.database.user
    val pass = configData.database.pass
    val connectionString = "jdbc:mysql://${url}"
    Database.connect(connectionString, "com.mysql.jdbc.Driver", user, pass)
}

fun insertGameUpdate(gameIds : String, buildIds : String , timestamps : String) {

    transaction {
        GameUpdate.insertIgnore {
            it[buildId] = buildIds
            it[gameId] = gameIds
            it[timeStamp] = timestamps
        }
    }
}