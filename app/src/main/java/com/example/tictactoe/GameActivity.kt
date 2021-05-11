package com.example.tictactoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tictactoe.GameManager.resetGame
import com.example.tictactoe.api.data.Game
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

class TagHolder {
    companion object {
        var yourTag:    String? = null
        var OpponentTag:String? = null
    }
}

class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Start new game
        binding.startNewGameButton.setOnClickListener {
            resetGame()
            GamestatusHolder.status = false
            updateScreen()
            resetBackground()
        }


        when (GamePlayHolder.GamePlay!!.players.size)
        {   1 -> {
                TagHolder.yourTag     = "X"
                TagHolder.OpponentTag = "O"
            }2 -> {
                TagHolder.yourTag     = "O"
                TagHolder.OpponentTag = "X"
            }
        }

        // Display Game ID
        binding.displayGameId.text = "GameID: ${GamePlayHolder.GamePlay?.gameId.toString()}"


        // Button Functionality
        binding.one.setOnClickListener   { onBoxClicked(Position(0, 0)) }
        binding.two.setOnClickListener   { onBoxClicked(Position(0, 1)) }
        binding.three.setOnClickListener { onBoxClicked(Position(0, 2)) }
        binding.four.setOnClickListener  { onBoxClicked(Position(1, 0)) }
        binding.five.setOnClickListener  { onBoxClicked(Position(1, 1)) }
        binding.six.setOnClickListener   { onBoxClicked(Position(1, 2)) }
        binding.seven.setOnClickListener { onBoxClicked(Position(2, 0)) }
        binding.eight.setOnClickListener { onBoxClicked(Position(2, 1)) }
        binding.nine.setOnClickListener  { onBoxClicked(Position(2, 2)) }

        updateScreen()
    }

    private fun countGrid(): Int {
        var count = 0
        for (i in 0..2) {
            for (j in 0..2) {
                if (GamePlayHolder.GamePlay?.state!![i][j] != "0") {
                    count++
                }
            }
        }
        return count
    }


    private fun resetBackground() {
        binding.one.background   = null
        binding.two.background   = null
        binding.three.background = null
        binding.four.background  = null
        binding.five.background  = null
        binding.six.background   = null
        binding.seven.background = null
        binding.eight.background = null
        binding.nine.background  = null

        binding.winLose.text     = null
    }

    private fun updateScreen() {
        CoroutineScope(Dispatchers.IO).launch {
            while (!GamestatusHolder.status!!) {
                GameManager.pollGame(GamePlayHolder.GamePlay!!.gameId)
                delay(500)
                this@GameActivity.runOnUiThread {
                    val game = GamePlayHolder.GamePlay!!
                    binding.playerOneScore.text = game.players[0]
                    if (GamePlayHolder.GamePlay!!.players.size == 2) {
                        binding.playerTwoScore.text = game.players[1]
                        binding.winLose.text = null
                    }
                    binding.one.text    = game.state[0][0].takeUnless { it == "0" }
                    binding.two.text    = game.state[0][1].takeUnless { it == "0" }
                    binding.three.text  = game.state[0][2].takeUnless { it == "0" }
                    binding.four.text   = game.state[1][0].takeUnless { it == "0" }
                    binding.five.text   = game.state[1][1].takeUnless { it == "0" }
                    binding.six.text    = game.state[1][2].takeUnless { it == "0" }
                    binding.seven.text  = game.state[2][0].takeUnless { it == "0" }
                    binding.eight.text  = game.state[2][1].takeUnless { it == "0" }
                    binding.nine.text   = game.state[2][2].takeUnless { it == "0" }
                    testWinning()
                }
            }
        }
    }

    private fun playerTurn(): Boolean {
        var yourTurn = false
        val count = countGrid()
        when (TagHolder.yourTag) {
            "X" -> {
                when (count) {
                    0, 2, 4, 6, 8 -> yourTurn = true
                }
            }
            "O" -> {
                when (count) {
                    1, 3, 5, 7 -> yourTurn = true
                }
            }
        }
        return yourTurn
    }
    
    private fun testWinning() {
        val game = GamePlayHolder.GamePlay?.state!!
        when (TagHolder.yourTag)
        {   "X" -> {
                if (GameManager.hasGameEnded("X", game) != null) {
                    GameManager.hasGameEnded("X", game)?.let { showWinner(it) }
                    binding.winLose.text = "You Won!"
                    GamestatusHolder.status = true
                }
                if (GameManager.hasGameEnded("O", game) != null) {
                    GameManager.hasGameEnded("O", game)?.let { showWinner(it) }
                    binding.winLose.text = "You Lost!"
                    GamestatusHolder.status = true
                } }
            "O" -> {
                if (GameManager.hasGameEnded("O", game) != null) {
                    GameManager.hasGameEnded("O", game)?.let { showWinner(it) }
                    binding.winLose.text = "You Won"
                    GamestatusHolder.status = true
                }
                if (GameManager.hasGameEnded("X", game) != null) {
                    GameManager.hasGameEnded("X", game)?.let { showWinner(it) }
                    binding.winLose.text = "You lost"
                    GamestatusHolder.status = true
                }
            }
        }
    }

    private fun onBoxClicked(position: Position) {
        if (playerTurn()) {
                GameManager.makeMove(position)
                println("1111111111lastBox${GamePlayHolder.GamePlay?.state}")
            }
        }

    private fun showWinner(winningLine: WinningLine) {
        val (winningBoxes, background) = when (winningLine) {
            WinningLine.ROW_0 -> Pair(listOf(binding.one, binding.two, binding.three),     R.drawable.horizontal_line)
            WinningLine.ROW_1 -> Pair(listOf(binding.four, binding.five, binding.six),     R.drawable.horizontal_line)
            WinningLine.ROW_2 -> Pair(listOf(binding.seven, binding.eight, binding.nine),  R.drawable.horizontal_line)
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