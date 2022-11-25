package com.capstone.easyfarm

import android.app.StatusBarManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.easyfarm.databinding.ActivityResultBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_result.*
import okhttp3.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class ResultActivity : AppCompatActivity() {

    var pestName = "init_pestName"
    var pestPercentage = 0.19971016
    var realPath: String? = ""

    lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)



        showProgressBar(false)

        supportActionBar?.title = "진단 결과"

        realPath = intent.getStringExtra("realPath")

        // 결과창 이미지
        binding.iv1.setImageURI(intent.getParcelableExtra("photoUri"))

        // 병해충 명
        pestName = intent.getStringExtra("pestName").toString()
        when (pestName) {
            "콩불마름병" -> binding.tv2.text = "콩 불마름병"
            "콩점무늬병" -> binding.tv2.text = "콩 점무늬병"
            "배추검은썩음병" -> binding.tv2.text = "배추 검은썩음병"
            "배추노균병" -> binding.tv2.text = "배추 노균병"
            "고추흰가루병" -> binding.tv2.text = "고추 흰가루병"
            "고추탄저병" -> binding.tv2.text = "고추 탄저병"
            "파녹병" -> binding.tv2.text = "파 녹병"
            "파노균병" -> binding.tv2.text = "파 노균병"
            "파검은무늬병" -> binding.tv2.text = "파 검은무늬병"
            "무노균병" -> binding.tv2.text = "무 노균병"
            "무검은무늬병" -> binding.tv2.text = "무 검은무늬병"
            "정상_콩" -> binding.tv2.text = "정상_콩"
            "정상_배추" -> binding.tv2.text = "정상_배추"
            "정상_고추" -> binding.tv2.text = "정상_고추"
            "정상_파" -> binding.tv2.text = "정상_파"
            "정상_무" -> binding.tv2.text = "정상_무"
        }

        // 확률
        pestPercentage = intent.getDoubleExtra("pestPercentage", 0.19971016)
        pestPercentage = (pestPercentage * 10000.0).roundToInt() / 100.0
        binding.tv3.text = "${pestPercentage}%"
        binding.pb1.progress = pestPercentage.toInt()

        var URL = ""
        if ((pestName?.substring(0..1)).equals("정상")) {
            btn1.visibility = View.INVISIBLE
            iv3.visibility=View.INVISIBLE
        } else {
            iv4.visibility=View.INVISIBLE
            for (i in 0 until SplashActivity.PEST_NAMES.size) {
                for (j in 0 until SplashActivity.PEST_NAMES[i].size) {
                    if (SplashActivity.PEST_NAMES[i][j] == pestName)
                        URL = SplashActivity.PEST_URLS[i][j]
                }
            }
        }

        // 상세보기
        btn1.setOnClickListener {
            intent = Intent(this, WebViewActivity::class.java)
            intent.putExtra("URL", URL)
            intent.putExtra("prevActivity", "ResultActivity")
            startActivity(intent)
        }

        // 나의 식물에 추가
        btn2.setOnClickListener {
            showProgressBar(true)
            binding.btn1.isClickable = false
            binding.btn2.isClickable = false
            binding.btn3.isClickable = false
            work_PostResult()
        }

        // 메인화면 가기
        btn3.setOnClickListener {
            finishAffinity()
            val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)
        }
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


    fun work_PostResult() {

        val file = File(realPath)
        var fileName = "image.jpg"

        var requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        var body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", fileName, requestBody)

        var gson: Gson = GsonBuilder().setLenient().create()

        val service = ResultActivity.RetrofitAPI_Nodejs.service
        service.PostResult(
            body,
            SplashActivity.deviceId,
            pestName,
            pestPercentage,
            LocalDate.now().toString()
        )
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!.string()
                        Log.d("성공", result)

                        showProgressBar(false)
                        binding.btn1.isClickable = true
                        binding.btn2.isClickable = true
                        binding.btn3.isClickable = true

                        Toast.makeText(this@ResultActivity, "진단 결과 업로드 성공", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Log.d("response 실패", response.code().toString())
                        Toast.makeText(this@ResultActivity, "response 실패", Toast.LENGTH_SHORT)
                            .show()
                        showProgressBar(false)
                        binding.btn1.isClickable = true
                        binding.btn2.isClickable = true
                        binding.btn3.isClickable = true
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                    Toast.makeText(this@ResultActivity, "실패", Toast.LENGTH_SHORT).show()
                    showProgressBar(false)
                    binding.btn1.isClickable = true
                    binding.btn2.isClickable = true
                    binding.btn3.isClickable = true
                }
            })
    }

    fun showProgressBar(isShow: Boolean) {
        if (isShow) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }
}