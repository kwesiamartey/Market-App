package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSeeFullDescriptionBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel


class SeeFullDescriptionFragment : Fragment() {

    lateinit var _bind:FragmentSeeFullDescriptionBinding
    val bind get() = _bind

    val pullingDataFromServerViewModel:PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PullingDataFromServerViewModel::class.java)
    }

    val viewModelClass:MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentSeeFullDescriptionBinding.inflate(layoutInflater)


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.title = "TradeTower"
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        try {

            try {
                bind.title.text = viewModelClass.title.value
                bind.descrip.text = viewModelClass.desc.value
            } catch (e: Exception) { }

            bind.bk.setOnClickListener {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null).commit()
            }

        } catch (e: Exception) { }
    }
}