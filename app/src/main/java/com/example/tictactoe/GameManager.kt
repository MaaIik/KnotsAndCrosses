package com.example.tictactoe

import android.content.Intent
import com.example.tictactoe.api.GameService
import com.example.tictactoe.api.data.Game
import com.example.tictactoe.api.data.GameState

object GameManager {

    var state: GameState? = null

    val StartingGameState: GameState = mutableListOf(
        mutableListOf("0", "0", "0"),
        mutableListOf("0", "0", "0"),
        mutableListOf("0", "0", "0")
    )

    fun resetGame() {
        updateGame(GamePlayHolder.GamePlay!!.gameId, StartingGameState)
    }

    fun makeMove(position: Position) {
        val tempoState: GameState = GamePlayHolder.GamePlay!!.state

        if (tempoState[position.row][position.column] == "0") {
            tempoState[position.row][position.column] = TagHolder.yourTag!!
            updateGame(GamePlayHolder.GamePlay!!.gameId, tempoState)
        }
    }

    fun hasGameEnded(Tag: String, stateGame: GameState): WinningLine? {
        if (stateGame[0][0] == Tag && stateGame[0][1] == Tag && stateGame[0][2] == Tag) {
            return WinningLine.ROW_0
        } else if (stateGame[1][0] == Tag && stateGame[1][1] == Tag && stateGame[1][2] == Tag) {
            return WinningLine.ROW_1
        } else if (stateGame[2][0] == Tag && stateGame[2][1] == Tag && stateGame[2][2] == Tag) {
            return WinningLine.ROW_2
        } else if (stateGame[0][0] == Tag && stateGame[1][0] == Tag && stateGame[2][0] == Tag) {
            return WinningLine.COLUMN_0
        } else if (stateGame[0][1] == Tag && stateGame[1][1] == Tag && stateGame[2][1] == Tag) {
            return WinningLine.COLUMN_1
        } else if (stateGame[0][2] == Tag && stateGame[1][2] == Tag && stateGame[2][2] == Tag) {
            return WinningLine.COLUMN_2
        } else if (stateGame[0][0] == Tag && stateGame[1][1] == Tag && stateGame[2][2] == Tag) {
            return WinningLine.DIAGONAL_LEFT
        } else if (stateGame[0][2] == Tag && stateGame[1][1] == Tag && stateGame[2][0] == Tag) {
            return WinningLine.DIAGONAL_RIGHT
        }
        return null
    }

    fun createGame(player: String) {
        GameService.createGame(player, StartingGameState) { game: Game?, err: Int? ->
            if (err != null) {
                println("Error: Create Game")
            } else {
                GamePlayHolder.GamePlay = game

                val intent = Intent(MainActivity.mainContext, GameActivity::class.java)
                MainActivity.mainContext.startActivity(intent)
            }
        }

    }

    fun joinGame(playerId: String, gameId: String) {
        GameService.joinGame(playerId, gameId) { game: Game?, err: Int? ->
            if (err != null) {
                println("Error: Join Game")
            } else {
                GamePlayHolder.GamePlay = game

                val intent = Intent(MainActivity.mainContext, GameActivity::class.java)
                MainActivity.mainContext.startActivity(intent)
            }
        }

    }

    fun updateGame(gameId: String, state: GameState) {
        GameService.updateGame(gameId, state) { game: Game?, err: Int? ->
            if (err != null) {
                println("Error: Update Game")
            } else {
                GamePlayHolder.GamePlay = game
            }
        }
    }

    fun pollGame(gameId: String) {
        GameService.pollGame(gameId) { game: Game?, err: Int? ->
            if (err != null) {
                println("Error: Poll Game")
            } else {
                GamePlayHolder.GamePlay = game
            }
        }
    }

}




