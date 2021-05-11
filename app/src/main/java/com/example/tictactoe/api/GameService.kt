package com.example.tictactoe.api

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.tictactoe.App
import com.example.tictactoe.R
import com.example.tictactoe.api.data.Game
import com.example.tictactoe.api.data.GameState
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

typealias GameServiceCallback = (state: Game?, errorCode:Int? ) -> Unit


object GameService {

    /// NOTE: Do not want to have App.context all over the code. Also it is nice if we later want to support different contexts
    private val context = App.context

    /// NOTE: Good practice to use a queue for performing requests.
    private val requestQue: RequestQueue = Volley.newRequestQueue(context)

    /// NOTE: One possible way of constructing a list of API url.
    // You want to construct the urls so that you can support different environments (i.e. Debug, Test, Prod etc)
    private enum class APIEndpoints(val url:String) {
        CREATE_GAME("%1s%2s%3s".format(context.getString(R.string.protocol), context.getString(R.string.domain),context.getString(
            R.string.base_path)))
    }


    fun createGame(playerId:String, state: GameState, callback:GameServiceCallback) {

        val url = APIEndpoints.CREATE_GAME.url

        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("state",JSONArray(state))

        val request = object : JsonObjectRequest(
            Request.Method.POST,url, requestData,
            Response.Listener{
                // Success game created.
                val game = Gson().fromJson(it.toString(0), Game::class.java)
                println("1111111111111111createGame ${game}")
                callback(game,null) },
            Response.ErrorListener{
                callback(null, it.networkResponse.statusCode)
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = context.getString(R.string.game_service_key)
                return headers
            }
        }

        requestQue.add(request)
    }

    fun joinGame(playerId:String, gameId:String, callback: GameServiceCallback){
        val url = "https://generic-game-service.herokuapp.com/Game/${gameId}/join"

        val requestData = JSONObject()
        requestData.put("player", playerId)
        requestData.put("gameId", gameId)

        val request = object : JsonObjectRequest(
                Request.Method.POST, url, requestData,
                Response.Listener{
                    val game = Gson().fromJson(it.toString(0), Game::class.java)
                    callback(game, null)},

                Response.ErrorListener{
                    callback(null, it.networkResponse.statusCode)
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = App.context.getString(R.string.game_service_key)
                return headers
            }
        }
        requestQue.add(request)

    }

    fun updateGame(gameId: String, gameState:GameState, callback: GameServiceCallback){
        val url = "https://generic-game-service.herokuapp.com/Game/${gameId}/update"

        val requestData = JSONObject()
        requestData.put("gameId", gameId)
        requestData.put("state", JSONArray(gameState))

        val request = object : JsonObjectRequest(
                Request.Method.POST, url, requestData,
                Response.Listener{
                    val game = Gson().fromJson(it.toString(0), Game::class.java)
                    callback(game, null) },

                Response.ErrorListener{
                    callback(null, it.networkResponse.statusCode)
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = App.context.getString(R.string.game_service_key)
                return headers
            }
        }
        requestQue.add(request)

    }

    fun pollGame(gameId: String,callback:GameServiceCallback){

        val url = "https://generic-game-service.herokuapp.com/Game/${gameId}/poll"

        val request = object : JsonObjectRequest(
                Request.Method.GET, url, null,
                Response.Listener{
                    val game = Gson().fromJson(it.toString(0), Game::class.java)
                    callback(game, null)
                    println("$game") },

                Response.ErrorListener{
                    callback(null, it.networkResponse.statusCode)
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Game-Service-Key"] = App.context.getString(R.string.game_service_key)
                return headers
            }
        }
        requestQue.add(request)

    }

}