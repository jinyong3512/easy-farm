package com.capstone.easyfarm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.capstone.easyfarm.databinding.Fragment1Binding

class Fragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = Fragment1Binding.inflate(inflater, container, false)

        binding.btn1.setColorFilter(resources.getColor(R.color.green))

        // btn1(병해충 진단) 누를 시 "1" 전달하고 PlantSelectActivity로 이동
        binding.btn1.setOnClickListener {
            val intent = Intent(activity, PlantSelectActivity::class.java)
            intent.putExtra("buttonType", "1")
            startActivity(intent)
        }

        return binding.root
    }
}