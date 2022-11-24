package com.capstone.easyfarm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.capstone.easyfarm.databinding.ActivityMyrecordBinding
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MyRecordActivity : AppCompatActivity() {

    var items = ArrayList<ListViewBtnItem?>()

    lateinit var adapter: ListViewBtnAdapter

    lateinit var binding: ActivityMyrecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyrecordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = "나의 기록"

        for (i in 0 until Fragment2.results_List.size) {
            var item = ListViewBtnItem()
            item.MyPlant_image_URL = Fragment2.results_List[i].MyPlant_image_URL
            item.MyPlant_Pest = Fragment2.results_List[i].MyPlant_Pest
            item.MyPlant_Date = Fragment2.results_List[i].MyPlant_Date
            item.MyPlant_Percentage = Fragment2.results_List[i].MyPlant_Percentage
            items.add(item)
        }

        adapter = ListViewBtnAdapter(this, R.layout.listview_btn_item, items, this)
        binding.listview1.adapter = adapter

    }

    fun onListBtnClick1(position: Int) {
        var URL = ""
        for (i in 0 until SplashActivity.PEST_NAMES.size) {
            for (j in 0 until SplashActivity.PEST_NAMES[i].size) {
                if (SplashActivity.PEST_NAMES[i][j] == items[position]!!.MyPlant_Pest)
                    URL = SplashActivity.PEST_URLS[i][j]
            }
        }
        intent = Intent(this, WebViewActivity::class.java)
        intent.putExtra("URL", URL)
        intent.putExtra("prevActivity", "ResultActivity")
        startActivity(intent)
    }

    fun onListBtnClick2(position: Int) {
        work_deleteResult(position)
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

    fun work_deleteResult(position: Int) {
        val service = MyRecordActivity.RetrofitAPI_Nodejs.service
        service.deleteResult(
            items[position]!!.MyPlant_image_URL
        )
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()!!.string()
                        Log.d("성공", result)
                        Toast.makeText(this@MyRecordActivity, "삭제 완료", Toast.LENGTH_SHORT).show()

                        items.removeAt(position)
//                        adapter.notifyDataSetChanged()
                        adapter = ListViewBtnAdapter(this@MyRecordActivity, R.layout.listview_btn_item, items, this@MyRecordActivity)
                        binding.listview1.adapter = adapter



                    } else {
                        Log.d("response 실패", response.code().toString())
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                }
            })
    }
}
