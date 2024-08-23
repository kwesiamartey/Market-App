package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.lornamobileappsdev.my_marketplace.databinding.FragmentStaySfeBinding


class StaySfeFragment : Fragment() {

    lateinit var _bind: FragmentStaySfeBinding
    val bind get() = _bind



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentStaySfeBinding.inflate(layoutInflater)


        bind.bk.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).commit()
        }

        return bind.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
    }
}