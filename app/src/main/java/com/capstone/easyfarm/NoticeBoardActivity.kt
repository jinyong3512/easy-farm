package com.capstone.easyfarm


import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class NoticeBoardActivity : AppCompatActivity() {

    val dataModelList = mutableListOf<DataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_noticeboard)

        val database = Firebase.database
        val myRef = database.getReference("myMemo")

        val listView = findViewById<ListView>(R.id.mainLV)

        val adapter_list = ListViewAdapter(dataModelList)

        listView.adapter = adapter_list

        Log.d("DataModel------", dataModelList.toString())

        myRef.child(Firebase.auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("point", dataModelList.toString())
                    dataModelList.clear()
                    Log.d("point", dataModelList.toString())

                    for (dataModel in snapshot.children) {
                        Log.d("Data", dataModel.toString())
                        dataModelList.add(dataModel.getValue(DataModel::class.java)!!)

                    }
                    adapter_list.notifyDataSetChanged()
                    Log.d("DataModel", dataModelList.toString())

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        //메모 입력
        val writeButton = findViewById<ImageView>(R.id.writeBtn)
        writeButton.setOnClickListener {

            val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("작물 메모 다이얼로그")

            val mAlertDialog = mBuilder.show()


            val DateSelectBtn = mAlertDialog.findViewById<Button>(R.id.dateSelectBtn)

            var dateText = ""

            DateSelectBtn?.setOnClickListener {

                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)

                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(
                        view: DatePicker?, year: Int, month: Int, dayOfMonth: Int
                    ) {
                        Log.d("MAIN", "${year}. ${month + 1}. ${dayOfMonth}")
                        DateSelectBtn.setText("${year}. ${month + 1}. ${dayOfMonth}")

                        dateText = "${year}, ${month + 1}, ${dayOfMonth}"
                    }

                }, year, month, date)
                dlg.show()

            }

            //저장 버튼 클릭-> 데이터베이스에 저장
            val saveBtn = mAlertDialog.findViewById<Button>(R.id.saveBtn)
            saveBtn?.setOnClickListener {
                //dateText(날짜), healMemo(입력) 값 DB에 저장
                val healMemo = mAlertDialog.findViewById<EditText>(R.id.healthMemo)?.text.toString()

                val database = Firebase.database
                val myRef = database.getReference("myMemo").child(Firebase.auth.currentUser!!.uid)

                val model = DataModel(dateText, healMemo)

                myRef
                    .push()
                    .setValue(model)

                mAlertDialog.dismiss()

            }

        }

    }

}