package com.capstone.easyfarm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.capstone.easyfarm.databinding.ActivityNaviBinding

class NaviActivity : AppCompatActivity() {

    lateinit var binding: ActivityNaviBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 최초 실행 화면 안쪽 틀을 Fragment1으로 띄우기
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout, Fragment1())
            .commit()

        // 내비게이션 뷰 누를 때마다 Fragment 변경하기
        binding.navigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.it1 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrameLayout, Fragment1())
                    .commit()
                R.id.it2 -> supportFragmentManager.beginTransaction()
                    .replace(R.id.mainFrameLayout, Fragment2())
                    .commit()
                R.id.it3 -> supportFragmentManager!!.beginTransaction()
                    .replace(R.id.mainFrameLayout, Fragment3())
                    .commit()
            }
            true
        }
    }
}