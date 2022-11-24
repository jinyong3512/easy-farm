package com.capstone.easyfarm

import android.Manifest
import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.capstone.easyfarm.databinding.ActivityPhotoselectBinding
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

class PhotoSelectActivity : AppCompatActivity() {

    lateinit var binding: ActivityPhotoselectBinding


    val PERMISSIONS = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val PERMISSIONS_REQUEST = 100

    private val BUTTON1 = 100
    private val BUTTON2 = 200
    private var photoUri: Uri? = null


    var realPath: String? = ""


    data class ResponseDTO_Flask(val result: Result)
    data class Result(val pestName: String, val pestPercentage: Double)

    var pestName = "init_pestName"
    var pestPercentage = 0.19971016


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoselectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showProgressBar(false)

        supportActionBar?.title = "사진 선택"


        checkPermissions(PERMISSIONS, PERMISSIONS_REQUEST)

        binding.btn1.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val photoFile = File(
                File("${filesDir}/image").apply {
                    if (!this.exists()) {
                        this.mkdirs()
                    }
                },
                "photo.jpg"
            )

            photoUri = FileProvider.getUriForFile(
                this,
                "com.capstone.easyfarm.fileprovider",
                photoFile
            )
            takePictureIntent.resolveActivity(packageManager)?.also {
                takePictureIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    photoUri
                )
                startActivityForResult(takePictureIntent, BUTTON1)
            }
        }

        binding.btn2.setOnClickListener {
            val getImageIntent = Intent(Intent.ACTION_GET_CONTENT)
            getImageIntent.type = "image/*"
            startActivityForResult(getImageIntent, BUTTON2)
        }

        binding.btn3.setOnClickListener {
            val plantType = intent.getStringExtra("plantType")

            if (photoUri != null) {
                showProgressBar(true)
                binding.btn1.isClickable=false
                binding.btn2.isClickable=false
                binding.btn3.isClickable=false
                if (photoUri!!.path.equals("/my_internal_images/photo.jpg"))
                    realPath = "/data/data/com.capstone.easyfarm/files/image/photo.jpg"
                else
                    realPath = getRealPathFromURI(this@PhotoSelectActivity, photoUri)

                // 서버에 무슨 작물인지랑 사진 전송할 함수
                work_prediction(plantType!!)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                BUTTON1 -> {
                    binding.iv1.setImageURI(photoUri)
                    Toast.makeText(this, photoUri?.path, Toast.LENGTH_LONG).show()
                }
                BUTTON2 -> {
                    photoUri = data?.data
                    binding.iv1.setImageURI(photoUri)
                }
            }
        }
    }

    private fun checkPermissions(permissions: Array<String>, permissionsRequest: Int): Boolean {
        val permissionList: MutableList<String> = mutableListOf()
        for (permission in permissions) {
            val result = ContextCompat.checkSelfPermission(
                this, permission
            )
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission)
            }
        }
        if (permissionList.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                permissionList.toTypedArray(),
                PERMISSIONS_REQUEST
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "권한 승인 부탁드립니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun getRealPathFromURI(context: Context?, uri: Uri?): String? {

        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri!!)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split: Array<String?> = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                return if ("primary".equals(type, ignoreCase = true)) {
                    (Environment.getExternalStorageDirectory().toString() + "/"
                            + split[1])
                } else {
                    val SDcardpath = getRemovableSDCardPath(context)?.split("/Android".toRegex())!!
                        .toTypedArray()[0]
                    SDcardpath + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"),
                    java.lang.Long.valueOf(id)
                )
                return getDataColumn(context!!, contentUri, null, null)
            } else if (isMediaDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split: Array<String?> = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(
                    context!!, contentUri, selection,
                    selectionArgs
                )
            }
        } else if (uri != null) {
            if ("content".equals(uri.getScheme(), ignoreCase = true)) {
                // Return the remote address
                return if (isGooglePhotosUri(uri)) uri.getLastPathSegment() else getDataColumn(
                    context!!,
                    uri,
                    null,
                    null
                )
            } else if ("file".equals(uri.getScheme(), ignoreCase = true)) {
                return uri.getPath()
            }
        }
        return null
    }

    fun getRemovableSDCardPath(context: Context?): String? {
        val storages = ContextCompat.getExternalFilesDirs(context!!, null)
        return if (storages.size > 1 && storages[0] != null && storages[1] != null) storages[1].toString() else ""
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String?>?
    ): String? {
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(
                uri!!, projection,
                selection, selectionArgs, null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }

    fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri
            .authority
    }

    fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri
            .authority
    }

    fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri
            .authority
    }

    fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri
            .authority
    }

    object RetrofitAPI_Flask {
        var okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(SplashActivity.BASE_URL_FLASK)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        val service: Service by lazy { retrofit.create(Service::class.java) }
    }

    fun work_prediction(plantType: String) {

        val file = File(realPath!!)
        var fileName = "image.jpg"

        var requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
        var body: MultipartBody.Part =
            MultipartBody.Part.createFormData("image", fileName, requestBody)

        var gson: Gson = GsonBuilder().setLenient().create()

        val service = RetrofitAPI_Flask.service
        service.prediction(body, plantType)
            .enqueue(object : retrofit2.Callback<ResponseDTO_Flask> {
                override fun onResponse(
                    call: Call<ResponseDTO_Flask>,
                    response: Response<ResponseDTO_Flask>
                ) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        Log.d("성공", "$result")

                        pestName = result?.result!!.pestName
                        pestPercentage = result?.result!!.pestPercentage

                        val intent = Intent(this@PhotoSelectActivity, ResultActivity::class.java)
                        intent.putExtra("photoUri", photoUri);
                        intent.putExtra("pestName", pestName)
                        intent.putExtra("pestPercentage", pestPercentage)
                        intent.putExtra("plantType", plantType)
                        intent.putExtra("realPath", realPath)

                        showProgressBar(false)
                        binding.btn1.isClickable=true
                        binding.btn2.isClickable=true
                        binding.btn3.isClickable=true
                        startActivity(intent)

                    } else {
                        Log.d("response 실패", response.code().toString())
                        Toast.makeText(this@PhotoSelectActivity, "response 실패", Toast.LENGTH_SHORT).show()
                        showProgressBar(false)
                        binding.btn1.isClickable=true
                        binding.btn2.isClickable=true
                        binding.btn3.isClickable=true
                    }
                }

                override fun onFailure(call: Call<ResponseDTO_Flask>, t: Throwable) {
                    Log.d("실패", t.message.toString())
                    Toast.makeText(this@PhotoSelectActivity, "실패", Toast.LENGTH_SHORT).show()
                    showProgressBar(false)
                    binding.btn1.isClickable=true
                    binding.btn2.isClickable=true
                    binding.btn3.isClickable=true
                }
            })
    }

    fun showProgressBar(isShow:Boolean){
        if (isShow) binding.progressBar.visibility= View.VISIBLE
        else binding.progressBar.visibility= View.GONE
    }
}