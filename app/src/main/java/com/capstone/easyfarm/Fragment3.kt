package com.capstone.easyfarm

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.capstone.easyfarm.databinding.Fragment3Binding
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

class Fragment3 : Fragment() {

    lateinit var binding: Fragment3Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = Fragment3Binding.inflate(inflater, container, false)

        binding.btn1.setColorFilter(resources.getColor(R.color.green))

        binding.tv1.text = "디바이스 ID : " + SplashActivity.deviceId

        if (SplashActivity.latitude >= 33 && SplashActivity.latitude <= 39 &&
            SplashActivity.longitude >= 126 && SplashActivity.latitude <= 129
        ) {
            // Geocoder 하고
            binding.tv2.text =
                "현재 위치 : ${getAddress(SplashActivity.latitude, SplashActivity.longitude)}"
        } else {
            binding.tv2.text =
                "위도 : " + SplashActivity.latitude.toString() + '\n' + "경도 : " + SplashActivity.longitude.toString()
        }

        // btn1(푸시 알림 설정) 누를 시 DB 변경해야 함 (미구현)
        binding.btn1.setOnClickListener {
            work_alarm()
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


    fun work_alarm() {

        val service = Fragment3.RetrofitAPI_Nodejs.service
        service.alarm(
            SplashActivity.deviceId
        )
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {

                        val result = response.body()!!.string()
                        Log.d("성공", result)

                        var text = ""
                        if (result.equals("0")) {
                            text = "푸시 알림 설정 OFF"
                        } else if (result == "1") {
                            text = "푸시 알림 설정 ON"
                        } else {
                            text = "res.send 이상함"
                        }

                        Toast.makeText(activity, text, Toast.LENGTH_SHORT).show()


                    } else {
                        Log.d("response 실패", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
            })
    }

    //위도 경도로 주소 구하는 Reverse-GeoCoding
    private fun getAddress(latitude: Double, longitude: Double): String {
        val geoCoder = Geocoder(context, Locale.KOREA)
        var addr = "Reverse-GeoCoding 오류"

        //GRPC 오류? try catch 문으로 오류 대처
        try {
            addr = geoCoder.getFromLocation(latitude, longitude, 1).first().getAddressLine(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return addr
    }
}