package jp.ac.yuge.micom.megurimasuposinputapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner

class SettingActivity : AppCompatActivity() {
    companion object {
        const val RESUT_CODE = 300
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // 設定内容取得
        val settedMode = intent.getStringExtra("Mode")
        val settedDepth = intent.getIntExtra("Depth", 2)
        val settedStrategy = intent.getIntArrayExtra("Strategy")

        // 設定内容判定
        findViewById<Spinner>(R.id.mode_select).setSelection(if(settedMode == "AI") 0 else 1)
        findViewById<Spinner>(R.id.depth_num).setSelection(settedDepth)
        findViewById<Spinner>(R.id.bruteforce_num).setSelection(settedStrategy[0])
        findViewById<Spinner>(R.id.stalker_num).setSelection(settedStrategy[1])
        findViewById<Spinner>(R.id.random_num).setSelection(settedStrategy[2])

        // 送信ボタン
        findViewById<Button>(R.id.setting_send_button).setOnClickListener {
            // 情報取得
            val mode = findViewById<Spinner>(R.id.mode_select).selectedItem as String
            val depth = findViewById<Spinner>(R.id.depth_num).selectedItem.toString().toInt()
            val bruteforce = findViewById<Spinner>(R.id.bruteforce_num).selectedItem.toString().toInt()
            val stalker = findViewById<Spinner>(R.id.stalker_num).selectedItem.toString().toInt()
            val random = findViewById<Spinner>(R.id.random_num).selectedItem.toString().toInt()

            // 結果を返す
            val resultIntent = Intent()
            resultIntent.putExtra("Mode", mode)
            resultIntent.putExtra("Depth", depth)
            resultIntent.putExtra("BruteForce", bruteforce)
            resultIntent.putExtra("Stalker", stalker)
            resultIntent.putExtra("Random", random)
            setResult(RESUT_CODE, resultIntent)
            finish()
        }
    }
}
