package jp.ac.yuge.micom.megurimasuposinputapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class QRReadActivity : AppCompatActivity() {
    companion object {
       const val RESULT_CODE = 200
    }

    private var qrReader: DecoratedBarcodeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrread)

        title = "QRリーダ"

        // QR読み取り開始
        qrReader = findViewById(R.id.qr_reader)
        qrReader!!.setStatusText("QRコードを画面中央に配置してください")

        // 読み取り結果
        qrReader!!.decodeSingle(object: BarcodeCallback{
            override fun barcodeResult(result: BarcodeResult?) {
                if(result == null){ return }

                Log.d("QRData", result.text)

                // 読み取り結果がnullでなければ結果を返す
                val resultIntent = Intent()
                resultIntent.putExtra("QRData", result.text)
                setResult(RESULT_CODE, resultIntent)
                qrReader!!.pause()
                finish()
            }

            override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) { }
        })

        // カメラ入力画像表示
        qrReader!!.resume()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(qrReader != null){
            qrReader!!.pause()
        }
    }
}
