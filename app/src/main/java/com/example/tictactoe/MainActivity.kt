package com.example.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.tictactoe.databinding.ActivityMainBinding
import com.example.tictactoe.dialogs.CreateGameDialog
import com.example.tictactoe.dialogs.GameDialogListener
import com.example.tictactoe.dialogs.JoinGameDialog


class MainActivity : AppCompatActivity() , GameDialogListener {


    val TAG:String = "MainActivity"

    lateinit var binding:ActivityMainBinding
    //lateinit var startNewGameButton: Button

    companion object {
        lateinit var mainContext : MainActivity

    }

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

        mainContext = this

        //GameManager.createGame("Testing createGame")

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
        val dlg = JoinGameDialog()
        dlg.show(supportFragmentManager, "JoinGameDialogFragment")

    }

    override fun onDialogCreateGame(player: String){
        Log.d(TAG, player)
    }

    override fun onDialogJoinGame(player: String, gameId: String) {
        Log.d(TAG, "$player $gameId")
    }
}