package com.example.tictactoe

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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

class GameActivity : AppCompatActivity() {

    // Setup
    private lateinit var binding: ActivityGameBinding
    private lateinit var startNewGameButton: Button
    private lateinit var player1Points: TextView
    private lateinit var player2Points: TextView
    private lateinit var gameID: TextView


    // Buttons
    private lateinit var one: TextView
    private lateinit var two: TextView
    private lateinit var three: TextView
    private lateinit var four: TextView
    private lateinit var five: TextView
    private lateinit var six: TextView
    private lateinit var seven: TextView
    private lateinit var eight: TextView
    private lateinit var nine: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //binding.GameIdText.text = GamePlayHolder.GamePlay?.gameId

        if (GamePlayHolder.GamePlay?.players?.size == 1){
            CurrentPlayerHolder.currentPlayer = 1
        }

        if (GamePlayHolder.GamePlay?.players?.size == 2){
            CurrentPlayerHolder.currentPlayer = 2
        }

        // Start New Game Functionality
        startNewGameButton = findViewById(R.id.start_new_game_button)

        startNewGameButton.setOnClickListener {
            GameManager.reset()
            resetboxes()
        }

        // Display Game ID
        gameID = findViewById(R.id.displayGameId)
        gameID.text = "GameID: ${GamePlayHolder.GamePlay?.gameId.toString()}"

        // Player score
        player1Points = findViewById(R.id.player_one_score)
        player2Points = findViewById(R.id.player_two_score)


        // Buttons
        one = findViewById(R.id.one)
        two = findViewById(R.id.two)
        three = findViewById(R.id.three)
        four = findViewById(R.id.four)
        five = findViewById(R.id.five)
        six = findViewById(R.id.six)
        seven = findViewById(R.id.seven)
        eight = findViewById(R.id.eight)
        nine = findViewById(R.id.nine)

        // Button Functionality
        one.setOnClickListener { onBoxClicked(one, Position(0, 0)) }
        two.setOnClickListener { onBoxClicked(two, Position(0, 1)) }
        three.setOnClickListener { onBoxClicked(three, Position(0, 2)) }
        four.setOnClickListener { onBoxClicked(four, Position(1, 0)) }
        five.setOnClickListener { onBoxClicked(five, Position(1, 1)) }
        six.setOnClickListener { onBoxClicked(six, Position(1, 2)) }
        seven.setOnClickListener { onBoxClicked(seven, Position(2, 0)) }
        eight.setOnClickListener { onBoxClicked(eight, Position(2, 1)) }
        nine.setOnClickListener { onBoxClicked(nine, Position(2, 2)) }


        updatePoints()
    //GameManager.updateGame(GamePlayHolder.GamePlay!!.gameId, GamePlayHolder.GamePlay!!.state)
    }

    // Displaying the Score
    private fun updatePoints() {
        player1Points.text = "Player X Points: ${GameManager.player1Points}"
        player2Points.text = "Player O Points: ${GameManager.player2Points}"
    }

    /*private fun update_screen() {
        CoroutineScope(Dispatchers.IO).launch {
            while (!GamestatusHolder.status) {
                GameManager.PollGame(GameHolder.MainGame!!.gameId)
                delay(500)
                this@GameActivity.runOnUiThread {
                    val game = GameHolder.MainGame!!
                    binding.player1.text = game.players[0]
                    binding.wonLose.text = game.gameId
                    if (GameHolder.MainGame!!.players.size == 2) {
                        binding.player2.text = game.players[1]
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
                    test_wining()
                }
            }
        }
    }*/

    private fun resetboxes() {
        one.text = ""
        two.text = ""
        three.text = ""
        four.text = ""
        five.text = ""
        six.text = ""
        seven.text = ""
        eight.text = ""
        nine.text = ""

        one.background = null
        two.background = null
        three.background = null
        four.background = null
        five.background = null
        six.background = null
        seven.background = null
        eight.background = null
        nine.background = null

        one.isEnabled = true
        two.isEnabled = true
        three.isEnabled = true
        four.isEnabled = true
        five.isEnabled = true
        six.isEnabled = true
        seven.isEnabled = true
        eight.isEnabled = true
        nine.isEnabled = true
    }

    private fun onBoxClicked(box: TextView, position: Position) {
        if (box.text.isEmpty()) {
            box.text = GameManager.currentPlayerSign
            val winningLine = GameManager.makeMove(position)
            if (winningLine != null) {
                updatePoints()
                disableBoxes()
                showWinner(winningLine)
            }
        }
    }

    private fun disableBoxes() {
        one.isEnabled = false
        two.isEnabled = false
        three.isEnabled = false
        four.isEnabled = false
        five.isEnabled = false
        six.isEnabled = false
        seven.isEnabled = false
        eight.isEnabled = false
        nine.isEnabled = false
    }

    private fun showWinner(winningLine: WinningLine) {
        val (winningBoxes, background) = when (winningLine) {
            WinningLine.ROW_0 -> Pair(listOf(one, two, three), R.drawable.horizontal_line)
            WinningLine.ROW_1 -> Pair(listOf(four, five, six), R.drawable.horizontal_line)
            WinningLine.ROW_2 -> Pair(listOf(seven, eight, nine), R.drawable.horizontal_line)
            WinningLine.COLUMN_0 -> Pair(listOf(one, four, seven), R.drawable.vertical_line)
            WinningLine.COLUMN_1 -> Pair(listOf(two, five, eight), R.drawable.vertical_line)
            WinningLine.COLUMN_2 -> Pair(listOf(three, six, nine), R.drawable.vertical_line)
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
