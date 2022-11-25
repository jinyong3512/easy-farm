package com.capstone.easyfarm

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
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
                iv1.setImageResource(R.drawable.pestselect_image_bean)
                btn1.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_1))
                btn2.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_1))
                btn3.visibility = View.GONE
                btn1.text = "콩 불마름병"
                btn2.text = "콩 점무늬병"
            }
            2 -> {
                iv1.setImageResource(R.drawable.pestselect_image_cabbage)
                btn1.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_2))
                btn2.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_2))
                btn3.visibility = View.GONE
                btn1.text = "배추 검은썩음병"
                btn2.text = "배추 노균병"
            }
            3 -> {
                iv1.setImageResource(R.drawable.pestselect_image_pepper)
                btn1.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_3))
                btn2.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_3))
                btn3.visibility = View.GONE
                btn1.text = "고추 흰가루병"
                btn2.text = "고추 탄저병"
            }
            4 -> {
                iv1.setImageResource(R.drawable.pestselect_image_onion)
                btn1.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_4))
                btn2.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_4))
                btn3.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_4))
                btn1.text = "파 녹병"
                btn2.text = "파 노균병"
                btn3.text = "파 검은무늬병"
            }
            5 -> {
                iv1.setImageResource(R.drawable.pestselect_image_radish)
                btn1.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_5))
                btn2.backgroundTintList = ColorStateList.valueOf(resources.getColor(R.color.pest_color_5))
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