package com.capstone.easyfarm

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_plantselect.*

class PlantSelectActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plantselect)

        // 액션 바 타이틀 "작물 선택"으로 변경
        supportActionBar?.title = "작물 선택"

        // 전 Fragment에서 btn1 눌렀는지 btn2 눌렀는지 값 가져오기
        val buttonType = intent.getStringExtra("buttonType")

        lateinit var intent: Intent

        // 작물 순서대로 버튼 눌렀을 때
        // buttonType이 1이라면 PhotoSelectActivity로 이동 buttonType이 2라면 PestSelectActivity로 이동
        // Activity로 이동하면서 어떤 작물 선택했는지 전달
        btn1.setOnClickListener {
            if (buttonType.equals("1"))
                intent = Intent(this, PhotoSelectActivity::class.java)
            else if (buttonType.equals("2"))
                intent = Intent(this, PestSelectActivity::class.java)
            intent.putExtra("plantType", "콩")
            startActivity(intent)
        }
        btn2.setOnClickListener {
            if (buttonType.equals("1"))
                intent = Intent(this, PhotoSelectActivity::class.java)
            else if (buttonType.equals("2"))
                intent = Intent(this, PestSelectActivity::class.java)
            intent.putExtra("plantType", "배추")
            startActivity(intent)
        }
        btn3.setOnClickListener {
            if (buttonType.equals("1"))
                intent = Intent(this, PhotoSelectActivity::class.java)
            else if (buttonType.equals("2"))
                intent = Intent(this, PestSelectActivity::class.java)
            intent.putExtra("plantType", "고추")
            startActivity(intent)
        }
        btn4.setOnClickListener {
            if (buttonType.equals("1"))
                intent = Intent(this, PhotoSelectActivity::class.java)
            else if (buttonType.equals("2"))
                intent = Intent(this, PestSelectActivity::class.java)
            intent.putExtra("plantType", "파")
            startActivity(intent)
        }
        btn5.setOnClickListener {
            if (buttonType.equals("1"))
                intent = Intent(this, PhotoSelectActivity::class.java)
            else if (buttonType.equals("2"))
                intent = Intent(this, PestSelectActivity::class.java)
            intent.putExtra("plantType", "무")
            startActivity(intent)
        }
    }
}
