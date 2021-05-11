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


    fun makeMove(position: Position) { //: WinningLine?
        val tempoState: GameState = GamePlayHolder.GamePlay!!.state

        if (tempoState[position.row][position.column] == "0") {
            tempoState[position.row][position.column] = MarkHolder.YourMark!!
            updateGame(GamePlayHolder.GamePlay!!.gameId, tempoState)
            println("1111111tempostate${tempoState}")
        }

    }



    fun reset() {
        updateGame(GamePlayHolder.GamePlay!!.gameId, StartingGameState)
    }

    fun hasGameEnded(Mark: String, stateGame: GameState): WinningLine? {
        if (stateGame[0][0] == Mark && stateGame[0][1] == Mark && stateGame[0][2] == Mark) {
            return WinningLine.ROW_0
        } else if (stateGame[1][0] == Mark && stateGame[1][1] == Mark && stateGame[1][2] == Mark) {
            return WinningLine.ROW_1
        } else if (stateGame[2][0] == Mark && stateGame[2][1] == Mark && stateGame[2][2] == Mark) {
            return WinningLine.ROW_2
        } else if (stateGame[0][0] == Mark && stateGame[1][0] == Mark && stateGame[2][0] == Mark) {
            return WinningLine.COLUMN_0
        } else if (stateGame[0][1] == Mark && stateGame[1][1] == Mark && stateGame[2][1] == Mark) {
            return WinningLine.COLUMN_1
        } else if (stateGame[0][2] == Mark && stateGame[1][2] == Mark && stateGame[2][2] == Mark) {
            return WinningLine.COLUMN_2
        } else if (stateGame[0][0] == Mark && stateGame[1][1] == Mark && stateGame[2][2] == Mark) {
            return WinningLine.DIAGONAL_LEFT
        } else if (stateGame[0][2] == Mark && stateGame[1][1] == Mark && stateGame[2][0] == Mark) {
            return WinningLine.DIAGONAL_RIGHT
        }
        return null
    }

    fun createGame(player: String) {

        GameService.createGame(player, StartingGameState) { game: Game?, err: Int? ->
            if (err != null) {
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                println("Error: Create Game")
            } else {
                /// TODO("We have a game. What to do?)
                GamePlayHolder.GamePlay = game

                val intent = Intent(MainActivity.mainContext, GameActivity::class.java)
                MainActivity.mainContext.startActivity(intent)

            }
        }

    }

    fun joinGame(playerId: String, gameId: String) {
        GameService.joinGame(playerId, gameId) { game: Game?, err: Int? ->
            if (err != null) {
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                println("Error: Join Game")
            } else {
                /// TODO("We have a game. What to do?)
                GamePlayHolder.GamePlay = game
                println(game)

                val intent = Intent(MainActivity.mainContext, GameActivity::class.java)
                MainActivity.mainContext.startActivity(intent)

            }
        }

    }

    fun updateGame(gameId: String, state: GameState) {
        GameService.updateGame(gameId, state) { game: Game?, err: Int? ->
            if (err != null) {
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                println("Error: Update Game")
            } else {
                /// TODO("We have a game. What to do?)
                GamePlayHolder.GamePlay = game

            }
        }

    }

    fun pollGame(gameId: String) {
        GameService.pollGame(gameId) { game: Game?, err: Int? ->
            if (err != null) {
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                println("Error: Poll Game")
            } else {
                /// TODO("We have a game. What to do?)
                GamePlayHolder.GamePlay = game

            }
        }

    }

}




