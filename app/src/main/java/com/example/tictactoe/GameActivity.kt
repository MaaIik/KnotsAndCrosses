package com.example.tictactoe

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.tictactoe.GameManager.reset
import com.example.tictactoe.api.data.Game
import com.example.tictactoe.api.data.GameState
import com.example.tictactoe.databinding.ActivityGameBinding
import kotlinx.android.synthetic.main.activity_game.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GamePlayHolder {

    companion object {
        var GamePlay: Game? = null
    }
}

class GamestatusHolder {
    companion object {
        var status: Boolean = false
    }
}

class MarkHolder {
    companion object {
        var YourMark: String? = null
        var OpponentMark: String? = null
    }
}

class GameActivity : AppCompatActivity() {

    // Setup
    private lateinit var binding: ActivityGameBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startNewGameButton.setOnClickListener {
            reset()
        }


        when (GamePlayHolder.GamePlay!!.players.size) {
            1 -> {
                MarkHolder.YourMark = "X"
                MarkHolder.OpponentMark = "O"
            }

            2 -> {
                MarkHolder.YourMark = "O"
                MarkHolder.OpponentMark = "X"
            }
        }

        // Display Game ID
        binding.displayGameId.text = "GameID: ${GamePlayHolder.GamePlay?.gameId.toString()}"


        // Button Functionality
        binding.one.setOnClickListener { onBoxClicked(Position(0, 0)) }
        binding.two.setOnClickListener { onBoxClicked(Position(0, 1)) }
        binding.three.setOnClickListener { onBoxClicked(Position(0, 2)) }
        binding.four.setOnClickListener { onBoxClicked(Position(1, 0)) }
        binding.five.setOnClickListener { onBoxClicked(Position(1, 1)) }
        binding.six.setOnClickListener { onBoxClicked(Position(1, 2)) }
        binding.seven.setOnClickListener { onBoxClicked(Position(2, 0)) }
        binding.eight.setOnClickListener { onBoxClicked(Position(2, 1)) }
        binding.nine.setOnClickListener { onBoxClicked(Position(2, 2)) }


        //updatePoints()
        update_screen()
    }


    private fun update_screen() {
        CoroutineScope(Dispatchers.IO).launch {
            while (!GamestatusHolder.status!!) {
                GameManager.pollGame(GamePlayHolder.GamePlay!!.gameId)
                delay(500)
                this@GameActivity.runOnUiThread {
                    val game = GamePlayHolder.GamePlay!!
                    binding.playerOneScore.text = game.players[0]
                    binding.wonLose.text = game.gameId
                    if (GamePlayHolder.GamePlay!!.players.size == 2) {
                        binding.playerTwoScore.text = game.players[1]
                        binding.wonLose.text = null
                    }
                    binding.one.text = game.state[0][0].takeUnless { it == "0" }
                    binding.two.text = game.state[0][1].takeUnless { it == "0" }
                    binding.three.text = game.state[0][2].takeUnless { it == "0" }
                    binding.four.text = game.state[1][0].takeUnless { it == "0" }
                    binding.five.text = game.state[1][1].takeUnless { it == "0" }
                    binding.six.text = game.state[1][2].takeUnless { it == "0" }
                    binding.seven.text = game.state[2][0].takeUnless { it == "0" }
                    binding.eight.text = game.state[2][1].takeUnless { it == "0" }
                    binding.nine.text = game.state[2][2].takeUnless { it == "0" }
                    testWinning()
                }
            }
        }
    }

    private fun testWinning() {
        val game = GamePlayHolder.GamePlay?.state!!
        when (MarkHolder.YourMark) {
            "X" -> {
                if (GameManager.hasGameEnded("X", game) != null) {
                    GameManager.hasGameEnded("X", game)?.let { showWinner(it) }
                    binding.wonLose.text = "You Won"
                    binding.startNewGameButton.isVisible = true
                    GamestatusHolder.status = true
                }
                if (GameManager.hasGameEnded("O", game) != null) {
                    GameManager.hasGameEnded("O", game)?.let { showWinner(it) }
                    binding.wonLose.text = "You Lost"
                    binding.startNewGameButton.isVisible = true
                    GamestatusHolder.status = true
                }
            }
            "O" -> {
                if (GameManager.hasGameEnded("O", game) != null) {
                    GameManager.hasGameEnded("O", game)?.let { showWinner(it) }
                    binding.wonLose.text = "You Won"
                    binding.startNewGameButton.isVisible = true
                    GamestatusHolder.status = true
                }
                if (GameManager.hasGameEnded("X", game) != null) {
                    GameManager.hasGameEnded("X", game)?.let { showWinner(it) }
                    binding.wonLose.text = "You lost"
                    binding.startNewGameButton.isVisible = true
                    GamestatusHolder.status = true
                }
            }
        }

    }



    private fun onBoxClicked(position: Position) {
        GameManager.makeMove(position)
        println("1111111111onboxclicked${GamePlayHolder.GamePlay?.state}")

        }

    private fun showWinner(winningLine: WinningLine) {
        val (winningBoxes, background) = when (winningLine) {
            WinningLine.ROW_0 -> Pair(listOf(binding.one, binding.two, binding.three), R.drawable.horizontal_line)
            WinningLine.ROW_1 -> Pair(listOf(binding.four, binding.five, binding.six), R.drawable.horizontal_line)
            WinningLine.ROW_2 -> Pair(listOf(binding.seven, binding.eight, binding.nine), R.drawable.horizontal_line)
            WinningLine.COLUMN_0 -> Pair(listOf(binding.one, binding.four, binding.seven), R.drawable.vertical_line)
            WinningLine.COLUMN_1 -> Pair(listOf(binding.two, binding.five, binding.eight), R.drawable.vertical_line)
            WinningLine.COLUMN_2 -> Pair(listOf(binding.three, binding.six, binding.nine), R.drawable.vertical_line)
            WinningLine.DIAGONAL_LEFT -> Pair(listOf(one, five, nine),
                R.drawable.left_diagonal_line
            )
            WinningLine.DIAGONAL_RIGHT -> Pair(listOf(three, five, seven),
                R.drawable.right_diagonal_line
            )
        }

        winningBoxes.forEach { box ->
            box.background = ContextCompat.getDrawable(GameActivity@ this, background)
        }
    }

}