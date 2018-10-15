package jp.ac.yuge.micom.megurimasuposinputapp

import android.app.Activity
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

        // QRコード読み取りボタン
        findViewById<Button>(R.id.read_qr_button).setOnClickListener {
            val qrReadActivity = Intent(this, QRReadActivity::class.java)
            startActivityForResult(qrReadActivity, 0)
        }

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

        // 接続解除ボタン
        findViewById<Button>(R.id.close_button).setOnClickListener {
            thread {
                closeSocket()
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

            QRReadActivity.RESULT_CODE -> {
                val qrData = data!!.getStringExtra("QRData")
                thread {
                    sendData("QRData:$qrData")
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
        }catch(e: Exception){
//            e.printStackTrace()
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
                val receiveText = reader.readLine()?: break
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
//            e.printStackTrace()
            closeSocket()
        }
    }

    private fun writeLog(text: String){
        handler.post {
            findViewById<TextView>(R.id.tcp_log_textview).text = text
        }
    }
}
