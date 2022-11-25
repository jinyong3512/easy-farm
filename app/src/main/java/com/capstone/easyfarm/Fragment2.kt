package com.capstone.easyfarm

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.easyfarm.databinding.Fragment2Binding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Fragment2 : Fragment() {

    companion object {
        lateinit var results_List: List<results>
    }

    data class results(
        val MyPlant_User_id: String,
        val MyPlant_image_URL: String,
        val MyPlant_Pest: String,
        val MyPlant_Date: String,
        val MyPlant_Percentage: Double
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().window.statusBarColor = requireActivity().getColor(R.color.fragment_statusBar_color_2)

        val binding = Fragment2Binding.inflate(inflater, container, false)

        binding.btn1.setColorFilter(resources.getColor(R.color.white))
        binding.btn2.setColorFilter(resources.getColor(R.color.white))
        binding.btn3.setColorFilter(resources.getColor(R.color.white))
        binding.btn5.setColorFilter(resources.getColor(R.color.white))



        // btn1(도감) 누를 시 "2" 전달하고 PlantSelectActivity로 이동
        binding.btn1.setOnClickListener {
            val intent = Intent(activity, PlantSelectActivity::class.java)
            intent.putExtra("buttonType", "2")
            startActivity(intent)
        }

        // 나의 기록 (미구현)
        binding.btn2.setOnClickListener {
            work_GetResult()
        }

        // 예보
        binding.btn3.setOnClickListener {
            val intent = Intent(activity, WebViewActivity::class.java)
            intent.putExtra("prevActivity", "NaviActivity")
            intent.putExtra("URL", "https://ncpms.rda.go.kr/mobile/ForeMain.ms")
            startActivity(intent)
        }

        // 알림 내역 (미구현)
//        binding.btn4.setOnClickListener {
//
//        }

        // 게시판
        binding.btn5.setOnClickListener {
            val intent = Intent(activity, NoticeBoardActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }


    object RetrofitAPI_Nodejs {
        var okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(SplashActivity.BASE_URL_NODEJS)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val service: Service by lazy { retrofit.create(Service::class.java) }
    }


    fun work_GetResult() {

        var gson: Gson = GsonBuilder().setLenient().create()

        val service = Fragment2.RetrofitAPI_Nodejs.service
        service.GetResult(
            SplashActivity.deviceId
        )
            .enqueue(object : retrofit2.Callback<List<Fragment2.results>> {
                override fun onResponse(
                    call: Call<List<Fragment2.results>>,
                    response: Response<List<Fragment2.results>>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("성공", "$result")

                        results_List = result!!

                        val intent = Intent(activity, MyRecordActivity::class.java)
                        startActivity(intent)

                    } else {
                        Log.d("response 실패", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<List<Fragment2.results>>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
            })
    }
}