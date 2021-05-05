package com.example.tictactoe

import android.content.Intent
import com.example.tictactoe.CurrentPlayerHolder.Companion.currentPlayer
import com.example.tictactoe.api.GameService
import com.example.tictactoe.api.GameServiceCallback
import com.example.tictactoe.api.data.Game
import com.example.tictactoe.api.data.GameState

class CurrentPlayerHolder{
    companion object {
        var currentPlayer : Int = 0
    }
}

object GameManager {

    var player: String? = null
    var state: GameState? = null


    var player1Points = 0
    var player2Points = 0

    val currentPlayerSign: String
        get() {
            return if (currentPlayer == 1) "X" else "O"
        }

    var StartingGameState: GameState = listOf(
            listOf(0, 0, 0),
            listOf(0, 0, 0),
            listOf(0, 0, 0))

    private var stateY = arrayOf( // 2D Array
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0),
            intArrayOf(0, 0, 0)
    )

    fun makeMove(position: Position): WinningLine? {
        stateY[position.row][position.column] = currentPlayer

        val winningLine = hasGameEnded()

        // */
        if (winningLine == null) {
            currentPlayer = 3 - currentPlayer
        } else {
            when (currentPlayer) {
                1 -> player1Points++
                2 -> player2Points++
            }
        }
        // */
        return winningLine
    }



    fun reset() {
        stateY = arrayOf(
                intArrayOf(0, 0, 0),
                intArrayOf(0, 0, 0),
                intArrayOf(0, 0, 0)
        )
        currentPlayer = 1
    }

    private fun hasGameEnded(): WinningLine? {
        if (stateY[0][0] == currentPlayer && stateY[0][1] == currentPlayer && stateY[0][2] == currentPlayer) {
            return WinningLine.ROW_0
        } else if (stateY[1][0] == currentPlayer && stateY[1][1] == currentPlayer && stateY[1][2] == currentPlayer) {
            return WinningLine.ROW_1
        } else if (stateY[2][0] == currentPlayer && stateY[2][1] == currentPlayer && stateY[2][2] == currentPlayer) {
            return WinningLine.ROW_2
        } else if (stateY[0][0] == currentPlayer && stateY[1][0] == currentPlayer && stateY[2][0] == currentPlayer) {
            return WinningLine.COLUMN_0
        } else if (stateY[0][1] == currentPlayer && stateY[1][1] == currentPlayer && stateY[2][1] == currentPlayer) {
            return WinningLine.COLUMN_1
        } else if (stateY[0][2] == currentPlayer && stateY[1][2] == currentPlayer && stateY[2][2] == currentPlayer) {
            return WinningLine.COLUMN_2
        } else if (stateY[0][0] == currentPlayer && stateY[1][1] == currentPlayer && stateY[2][2] == currentPlayer) {
            return WinningLine.DIAGONAL_LEFT
        } else if (stateY[0][2] == currentPlayer && stateY[1][1] == currentPlayer && stateY[2][0] == currentPlayer) {
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

                val intent = Intent(MainActivity.mainContext, GameActivity::class.java)
                MainActivity.mainContext.startActivity(intent)

            }
        }

    }

    fun updateGame(gameId: String, state: GameState, callback: GameServiceCallback) {
        GameService.updateGame(gameId, state) { game: Game?, err: Int? ->
            if (err != null) {
                ///TODO("What is the error code? 406 you forgot something in the header. 500 the server di not like what you gave it")
                println("Error: Update Game")
            } else {
                /// TODO("We have a game. What to do?)
                GamePlayHolder.GamePlay = game

                //StartingGameState[1][] = 7
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

                //makeMove
            }
        }

    }

// Method 2

    //class GameManager {

       /* private var currentPlayer = 1
        var player1Points = 0
        var player2Points = 0*/

        /*val currentPlayerMark: String
            get() {
                return if (currentPlayer == 1) "X" else "O"
            }*/

        /*private var state = arrayOf( // 2D Array
                intArrayOf(0, 0, 0),
                intArrayOf(0, 0, 0),
                intArrayOf(0, 0, 0)
        )*/



        /*fun makeMove(position: Position): WinningLine? {
            stateY[position.row][position.column] = currentPlayer

            val winningLine = hasGameEnded()

            if (winningLine == null) {
                currentPlayer = 3 - currentPlayer
            } else {
                when (currentPlayer) {
                    1 -> player1Points++
                    2 -> player2Points++
                }
            }

            return winningLine
        }*/

        /*fun reset() {
            stateY = arrayOf(
                    intArrayOf(0, 0, 0),
                    intArrayOf(0, 0, 0),
                    intArrayOf(0, 0, 0)
            )
            currentPlayer = 1
        }

        private fun hasGameEnded(): WinningLine? {
            if (stateY[0][0] == currentPlayer && stateY[0][1] == currentPlayer && stateY[0][2] == currentPlayer) {
                return WinningLine.ROW_0
            } else if (stateY[1][0] == currentPlayer && stateY[1][1] == currentPlayer && stateY[1][2] == currentPlayer) {
                return WinningLine.ROW_1
            } else if (stateY[2][0] == currentPlayer && stateY[2][1] == currentPlayer && stateY[2][2] == currentPlayer) {
                return WinningLine.ROW_2
            } else if (stateY[0][0] == currentPlayer && stateY[1][0] == currentPlayer && stateY[2][0] == currentPlayer) {
                return WinningLine.COLUMN_0
            } else if (stateY[0][1] == currentPlayer && stateY[1][1] == currentPlayer && stateY[2][1] == currentPlayer) {
                return WinningLine.COLUMN_1
            } else if (stateY[0][2] == currentPlayer && stateY[1][2] == currentPlayer && stateY[2][2] == currentPlayer) {
                return WinningLine.COLUMN_2
            } else if (stateY[0][0] == currentPlayer && stateY[1][1] == currentPlayer && stateY[2][2] == currentPlayer) {
                return WinningLine.DIAGONAL_LEFT
            } else if (stateY[0][2] == currentPlayer && stateY[1][1] == currentPlayer && stateY[2][0] == currentPlayer) {
                return WinningLine.DIAGONAL_RIGHT
            }
            return null
        }*/

        //Yversion 2
        /*private fun hasGameEndedV2(): Boolean {
            stateY.forEach { row ->
                val hasWon = row.all { player -> player == currentPlayer }
                if (hasWon) return true
            }

            for (i: Int in stateY.indices) {
                val hasWon = stateY[i].all { player -> player == currentPlayer }
                if (hasWon) return true
            }

            for (i in stateY.indices) {
                if (stateY[i][i] != currentPlayer) return false
            }

            for (i in stateY.size until 0) {
                if (stateY[i][i] != currentPlayer) return false
            }

            return true
        }*/
    }




