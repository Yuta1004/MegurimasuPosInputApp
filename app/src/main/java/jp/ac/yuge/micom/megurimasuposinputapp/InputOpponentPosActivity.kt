package jp.ac.yuge.micom.megurimasuposinputapp

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class InputOpponentPosActivity : AppCompatActivity(){
    companion object {
        const val RESULT_CODE = 100
    }

    private val agentNames = listOf("相手エージェント１", "相手エージェント２")
    private val inpPosList = arrayListOf<Int>()
    private var isPanelRemoval = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_opponent_pos)

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
        resultInent.putExtra("OpponentPos", inpPosList.toIntArray())
        setResult(RESULT_CODE, resultInent)
        finish()
    }
}
