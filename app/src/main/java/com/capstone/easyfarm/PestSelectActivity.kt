package com.capstone.easyfarm

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pestselect.*

class PestSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pestselect)

        supportActionBar?.title = "병해충 선택"

        val plantType = intent.getStringExtra("plantType")
        var plantTypeNum = 0

        when (plantType) {
            "콩" -> plantTypeNum = 1
            "배추" -> plantTypeNum = 2
            "고추" -> plantTypeNum = 3
            "파" -> plantTypeNum = 4
            "무" -> plantTypeNum = 5
        }

        when (plantTypeNum) {
            1 -> {
                btn1.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_bean,
                    0,
                    0,
                    0
                );
                btn2.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_bean,
                    0,
                    0,
                    0
                );
                btn3.visibility = View.GONE
                btn1.text = "콩 불마름병"
                btn2.text = "콩 점무늬병"
            }
            2 -> {
                btn1.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_cabbage,
                    0,
                    0,
                    0
                );
                btn2.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_cabbage,
                    0,
                    0,
                    0
                );
                btn3.visibility = View.GONE
                btn1.text = "배추 검은썩음병"
                btn2.text = "배추 노균병"
            }
            3 -> {
                btn1.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_pepper,
                    0,
                    0,
                    0
                );
                btn2.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_pepper,
                    0,
                    0,
                    0
                );
                btn3.visibility = View.GONE
                btn1.text = "고추 흰가루병"
                btn2.text = "고추 탄저병"
            }
            4 -> {
                btn1.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_onion,
                    0,
                    0,
                    0
                );
                btn2.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_onion,
                    0,
                    0,
                    0
                );
                btn3.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_onion,
                    0,
                    0,
                    0
                );
                btn3.text = SplashActivity.PEST_NAMES[3][2]
                btn1.text = "파 녹병"
                btn2.text = "파 노균병"
                btn3.text = "파 검은무늬병"
            }
            5 -> {
                btn1.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_radish,
                    0,
                    0,
                    0
                );
                btn2.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.plantselect_icon_radish,
                    0,
                    0,
                    0
                );
                btn3.visibility = View.INVISIBLE
                btn1.text = "무 노균병"
                btn2.text = "무 검은무늬병"
            }
        }


        btn1.setOnClickListener {
            intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("prevActivity", "PestSelectActivity")
            intent.putExtra("URL", SplashActivity.PEST_URLS[plantTypeNum - 1][0])
            startActivity(intent)
        }
        btn2.setOnClickListener {
            intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("prevActivity", "PestSelectActivity")
            intent.putExtra("URL", SplashActivity.PEST_URLS[plantTypeNum - 1][1])
            startActivity(intent)
        }
        btn3.setOnClickListener {
            intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("prevActivity", "PestSelectActivity")
            intent.putExtra("URL", SplashActivity.PEST_URLS[plantTypeNum - 1][2])
            startActivity(intent)
        }
    }
}