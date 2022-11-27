package com.capstone.easyfarm

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide

class ListViewBtnAdapter internal constructor(
    context: Context?,
    resourceId: Int, list: ArrayList<ListViewBtnItem?>?,
    private var listBtnClickListener: MyRecordActivity
) :
    ArrayAdapter<Any?>(context!!, resourceId, list!! as List<Any?>), View.OnClickListener {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        if (convertView == null)
            convertView = LayoutInflater.from(parent?.context)
                .inflate(R.layout.listview_btn_item, parent, false)

        val imageView = convertView!!.findViewById<View>(R.id.imageView1) as ImageView
        val textView1 = convertView.findViewById<View>(R.id.textView1) as TextView
        val textView2 = convertView.findViewById<View>(R.id.textView2) as TextView
        val textView3 = convertView.findViewById<View>(R.id.textView3) as TextView


        val listViewItem = getItem(position) as ListViewBtnItem?

        Glide.with(convertView)
            .load(listViewItem!!.MyPlant_image_URL)
            .into(imageView)

        when (listViewItem.MyPlant_Pest) {
            "콩불마름병" -> textView1.text = "콩 불마름병"
            "콩점무늬병" -> textView1.text = "콩 점무늬병"
            "배추검은썩음병" -> textView1.text = "배추 검은썩음병"
            "배추노균병" -> textView1.text = "배추 노균병"
            "고추흰가루병" -> textView1.text = "고추 흰가루병"
            "고추탄저병" -> textView1.text = "고추 탄저병"
            "파녹병" -> textView1.text = "파 녹병"
            "파노균병" -> textView1.text = "파 노균병"
            "파검은무늬병" -> textView1.text = "파 검은무늬병"
            "무노균병" -> textView1.text = "무 노균병"
            "무검은무늬병" -> textView1.text = "무 검은무늬병"
            "정상_콩" -> textView1.text = "정상_콩"
            "정상_배추" -> textView1.text = "정상_배추"
            "정상_고추" -> textView1.text = "정상_고추"
            "정상_파" -> textView1.text = "정상_파"
            "정상_무" -> textView1.text = "정상_무"
        }
        if (listViewItem.MyPlant_Pest.substring(0 until 2) == "정상") {
            (convertView.findViewById<View>(R.id.button1) as ImageButton).visibility =
                View.INVISIBLE
        }
        textView2.text = "${listViewItem.MyPlant_Percentage}%"
        textView3.text = listViewItem.MyPlant_Date.substring(0..9)

        val button1 = convertView.findViewById<View>(R.id.button1) as ImageButton
        button1.tag = position
        button1.setOnClickListener(this)


        val button2 = convertView.findViewById<View>(R.id.button2) as ImageButton
        button2.tag = position
        button2.setOnClickListener(this)


        return convertView
    }

    override fun onClick(v: View) {
        when (v?.id) {
            R.id.button1 -> {
                listBtnClickListener!!.onListBtnClick1(v.tag as Int)
            }
            R.id.button2 -> {
                listBtnClickListener!!.onListBtnClick2(v.tag as Int)
            }
        }

    }

    init {
        listBtnClickListener = listBtnClickListener
    }

}