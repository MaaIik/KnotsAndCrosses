package com.example.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.example.tictactoe.databinding.ActivityMainBinding
import com.example.tictactoe.dialogs.CreateGameDialog
import com.example.tictactoe.dialogs.GameDialogListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() , GameDialogListener {

    val TAG:String = "MainActivity"

    lateinit var binding:ActivityMainBinding
    //lateinit var startNewGameButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startGameButton.setOnClickListener {
            createNewGame()
        }

        binding.joinGameButton.setOnClickListener {
            joinGame()
        }

        //Uth
        /*startNewGameButton = findViewById(R.id.startNewGameButton)

        startNewGameButton.setOnClickListener {
            val intent = Intent(MainActivity@this, GameActivity::class.java)
            startActivity(intent)
        }*/

    }

    private fun createNewGame(){
        val dlg = CreateGameDialog()
        dlg.show(supportFragmentManager, "CreateGameDialogFragment")


    }

    private fun joinGame(){
        val dlg = CreateGameDialog()
        dlg.show(supportFragmentManager, "JoinGameDialogFragment")

    }

    override fun onDialogCreateGame(player: String){
        Log.d(TAG, player)
    }

    override fun onDialogJoinGame(player: String, gameId: String) {
        Log.d(TAG, "$player $gameId")
    }
}