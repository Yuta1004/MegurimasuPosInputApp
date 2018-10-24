package jp.ac.yuge.micom.megurimasuposinputapp

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class InputActionActivity : AppCompatActivity(){
    companion object {
        // 100: 自チーム, 150: 相手チーム
        const val ALLY_RESULTCODE = 100
        const val OPPONENT_RESULTCODE = 150
    }

    private var resultCode = 150
    private val agentNames = listOf("エージェント１", "エージェント２")
    private val inpPosList = arrayListOf<Int>()
    private var isPanelRemoval = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_opponent_pos)

        // 自チームor相手チームどちらの行動入力なのか
        val requestIntent = intent
        resultCode = requestIntent.getIntExtra("SETRESULT", 150)
        title = if(resultCode == 100){
            "自チーム行動入力"
        }else{
            "相手チーム行動入力"
        }

        // 入力対象エージェント名セット
        findViewById<TextView>(R.id.agent_name_textview).text = agentNames[0]

        // 移動情報選択ボタンのリスナたち
        findViewById<Button>(R.id.action_0_button).setOnClickListener { posButtonPushed(0) }
        findViewById<Button>(R.id.action_1_button).setOnClickListener { posButtonPushed(1) }
        findViewById<Button>(R.id.action_2_button).setOnClickListener { posButtonPushed(2) }
        findViewById<Button>(R.id.action_3_button).setOnClickListener { posButtonPushed(3) }
        findViewById<Button>(R.id.action_4_button).setOnClickListener { posButtonPushed(4) }
        findViewById<Button>(R.id.action_5_button).setOnClickListener { posButtonPushed(5) }
        findViewById<Button>(R.id.action_6_button).setOnClickListener { posButtonPushed(6) }
        findViewById<Button>(R.id.action_7_button).setOnClickListener { posButtonPushed(7) }
        findViewById<Button>(R.id.action_8_button).setOnClickListener { posButtonPushed(8) }

        // パネル除去ボタン
        findViewById<Button>(R.id.removal_panel_button).setOnClickListener {
            isPanelRemoval = !isPanelRemoval

            if(isPanelRemoval){
                findViewById<Button>(R.id.removal_panel_button).setBackgroundColor(Color.parseColor("#FF5555"))
            }else{
                findViewById<Button>(R.id.removal_panel_button).setBackgroundColor(Color.parseColor("#D6D7D7"))
            }
        }
    }

    private fun posButtonPushed(buttonNum: Int){
        inpPosList.add(buttonNum + if(isPanelRemoval) 10 else 0)

        // 位置情報が足りない
        if(inpPosList.size < 2){
            findViewById<TextView>(R.id.agent_name_textview).text = agentNames[1]
            return
        }

        // 十分な量位置情報が入力された
        val resultInent = Intent()
        resultInent.putExtra("Action", inpPosList.toIntArray())
        setResult(resultCode, resultInent)
        finish()
    }
}
