package jp.ac.yuge.micom.megurimasuposinputapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class HostConnectionACtivity : AppCompatActivity() {
    private var server: ServerSocket? = null
    private var socket: Socket? = null
    private var isConnecting = false
    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host_connection)

        // 対戦相手位置情報入力ボタン
        findViewById<Button>(R.id.input_pos_button).setOnClickListener {
            val inputOpponentPosActivity = Intent(this, InputOpponentPosActivity::class.java)
            startActivityForResult(inputOpponentPosActivity, 0)
        }

        // 再接続受け付け開始ボタン
        findViewById<Button>(R.id.reopen_button).setOnClickListener {
            if(isConnecting){ return@setOnClickListener }
            thread{
                closeSocket()
                initSocket()
                receiveData()
            }
        }

        // Activity起動時に接続を受け付けるようにする
        thread {
            initSocket()
            receiveData()
        }
    }

    // Activity終了時に強制Close
    override fun onDestroy() {
        super.onDestroy()
        closeSocket()
    }

    // Activityを呼んだ結果が返ってくる
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(resultCode){
            InputOpponentPosActivity.RESULT_CODE -> {
                val opponentPos = data!!.getIntArrayExtra("OpponentPos")
                thread {
                    sendData("OpponentPos:${opponentPos[0]}:${opponentPos[1]}")
                }
            }
        }
    }

    /* ----------以下TCP通信を担う関数たち ※スレッド内で呼ぶこと！！ ---------- */

    private fun initSocket(){
        if(isConnecting){ return }
        isConnecting = true

        try{
            // サーバを建てる
            server = ServerSocket(6666); writeLog("Waiting...")
            socket = server!!.accept(); writeLog("Connection Success!")
            sendData("HElloWorld")
            sendData("こんにちは！")
        }catch(e: Exception){
            e.printStackTrace()
            closeSocket()
        }
    }

    private fun closeSocket(){
        // 終了
        if(socket != null){
            socket!!.close()
            socket = null
        }
        if(server != null){
            server!!.close()
            server = null
        }
        writeLog("Connection Close")

        isConnecting = false
    }

    // データ受信
    private fun receiveData(){
        try{
            val reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            while(true) {
                val receiveText = reader.readLine()?: "end"
                if (receiveText == "end") { break }
                writeLog(receiveText)
            }
            closeSocket()
        }catch(e: Exception){
            closeSocket()
        }
    }

    // データ送信
    private fun sendData(text: String){
        try{
            val writer = socket!!.getOutputStream()
            writer.write((text + "\n").toByteArray())
        }catch(e: Exception){
            e.printStackTrace()
            closeSocket()
        }
    }

    private fun writeLog(text: String){
        handler.post {
            findViewById<TextView>(R.id.tcp_log_textview).text = text
        }
    }
}
