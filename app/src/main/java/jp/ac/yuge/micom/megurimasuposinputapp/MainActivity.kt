package jp.ac.yuge.micom.megurimasuposinputapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // PC接続ボタン
        findViewById<Button>(R.id.connect_pc_button).setOnClickListener {
            val hostCheckDerectionActivity = Intent(this, HostConnectionACtivity::class.java)
            startActivity(hostCheckDerectionActivity)
        }

        // 方向チェックボタン
        findViewById<Button>(R.id.check_derection_button).setOnClickListener {
            val checkDerectionActivity = Intent(this, CheckDerectionActivity::class.java)
            startActivity(checkDerectionActivity)
        }
    }
}
