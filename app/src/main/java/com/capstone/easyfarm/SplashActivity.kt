package com.capstone.easyfarm

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

    companion object {

        //        val BASE_URL_FLASK = "http://192.168.0.101:5000/"       // WIFI home
//        val BASE_URL_FLASK = "http://192.168.21.15:5000/"       // HOTSPOT
//        val BASE_URL_FLASK = "http://34.64.86.255:5000/"        // HOTSPOT 원형
        val BASE_URL_FLASK = "http://34.64.86.255:5000/"        // AWS 원형

        //        val BASE_URL_NODEJS = "http://192.168.0.101:3000/"      // WIFI home
//        val BASE_URL_NODEJS = "http://192.168.21.15:3000/"      // HOTSPOT
//        val BASE_URL_NODEJS = "http://192.168.21.178:3000/"     // HOTSPOT 라연
        val BASE_URL_NODEJS = "http://18.144.156.210:3000/"     // AWS 라연

        var deviceId = "init_deviceId"
        var latitude = 0.19971016
        var longitude = 0.19971016
        var userId = "init_userId"
        val PEST_URLS = arrayOf(
            arrayOf(
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?hiKncrCode=FC&sKncrCode=FC030301&dtlKey=D00001489&pageIndex=2",
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?hiKncrCode=FC&sKncrCode=FC030301&dtlKey=D00004151&pageIndex=3"
            ),
            arrayOf(
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?hiKncrCode=VC&sKncrCode=VC021001&dtlKey=D00000706&pageIndex=1",
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?hiKncrCode=VC&sKncrCode=VC021001&dtlKey=D00000714&pageIndex=1"
            ),
            arrayOf(
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?hiKncrCode=VC&sKncrCode=VC011205&dtlKey=D00000197&pageIndex=6",
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?dtlKey=D00000195&totalSearchYn=Y"
            ),
            arrayOf(
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?dtlKey=D00001577&totalSearchYn=Y",
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?dtlKey=D00001574&totalSearchYn=Y",
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?dtlKey=D00001566&totalSearchYn=Y"
            ),
            arrayOf(
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?dtlKey=D00000574&totalSearchYn=Y",
                "https://ncpms.rda.go.kr/mobile/MobileSicknsDtlR.ms?dtlKey=D00000568&totalSearchYn=Y"
            ),
        )
        val PEST_NAMES = arrayOf(
            arrayOf("콩불마름병", "콩점무늬병"),
            arrayOf("배추검은썩음병", "배추노균병"),
            arrayOf("고추흰가루병", "고추탄저병"),
            arrayOf("파녹병", "파노균병", "파검은무늬병"),
            arrayOf("무노균병", "무검은무늬병")
        )
    }

    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var mLastLocation: Location
    internal lateinit var mLocationRequest: LocationRequest
    private val REQUEST_PERMISSION_LOCATION = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // actionBar 숨기기
        supportActionBar?.hide()

        // 디바이스 ID 구하기
        deviceId = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )

        // 권한 체크하고 위치 구하고
        // 통신 함수 내장 : 서버에 디바이스ID 위도 경도 보내기
        // 1초 있다가 NaviActivity로 넘어가기
        mLocationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
        if (checkPermissionForLocation(this)) {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            mLastLocation = locationResult.lastLocation
            latitude = mLastLocation.latitude
            longitude = mLastLocation.longitude

            work_PostUser()
            userIdCheck()

            Log.d("여기", userId)

            // 1초 기다린 후에 NaviActivity를 실행시킨 후 현재 열려있는 창(Splash 창)을 종료시켜준다.
            val handler = Handler()
            handler.postDelayed(
                {
                    val intent = Intent(baseContext, NaviActivity::class.java)
                    startActivity(intent)
                    finish()
                },
                1000
            )


        }
    }

    private fun checkPermissionForLocation(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                true
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_PERMISSION_LOCATION
                )
                false
            }
        } else {
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Log.d("ttt", "onRequestPermissionsResult() _ 권한 허용 거부")
                Toast.makeText(this, "권한이 없어 해당 기능을 실행할 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
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


    fun work_PostUser() {
        val service = SplashActivity.RetrofitAPI_Nodejs.service
        service.PostUser(
            SplashActivity.deviceId,
            SplashActivity.longitude,
            SplashActivity.latitude
        )
            .enqueue(object : retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {

                        val result = response.body()!!.string()
                        Log.d("성공", result)

                        Toast.makeText(this@SplashActivity, "유저 정보 업로드 성공", Toast.LENGTH_SHORT)
                            .show()

                    } else {
                        Log.d("response 실패", response.code().toString())
                        Toast.makeText(this@SplashActivity, "response 실패", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                    Toast.makeText(this@SplashActivity, "실패", Toast.LENGTH_SHORT)
                        .show()

                }
            })
    }

    fun userIdCheck() {
        var auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) { // 이미 가입한 회원인 경우
            userId = user.uid // uid를 가져온다.
            Log.d("익명로그인 중복", userId)
        } else {
            auth.signInAnonymously() // 익명으로 가입한다.
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) { // 가입 성공한 경우
                        userId = auth.currentUser!!.uid
                        Log.d("익명로그인 성공", userId)
                    } else {
                        // 가입 실패한 경우
                    }
                }
        }

    }
}