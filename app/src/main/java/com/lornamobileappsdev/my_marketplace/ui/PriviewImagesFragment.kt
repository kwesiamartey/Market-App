package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.lornamobileappsdev.my_marketplace.databinding.FragmentPriviewImagesBinding
import com.lornamobileappsdev.my_marketplace.models.PostImagesGalleryImagesSelected
import com.lornamobileappsdev.my_marketplace.models.SinglePreviewModel
import com.lornamobileappsdev.my_marketplace.useCases.CustomItemAnimator
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel

class PriviewImagesFragment : Fragment() {

    lateinit var _bind:FragmentPriviewImagesBinding

    val bind get() = _bind

    val viewModelClass: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity())[PullingDataFromServerViewModel::class.java]
    }

    val viewModelClassTwo: MyViewModel by lazy {
        ViewModelProvider(requireActivity())[MyViewModel::class.java]
    }

    val imagess:MutableList<PostImagesGalleryImagesSelected> = mutableListOf()

    val images:MutableList<SinglePreviewModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentPriviewImagesBinding.inflate(layoutInflater)


        val singlePreviewModel = SinglePreviewModel(
            viewModelClassTwo.image.value,
            viewModelClassTwo.image_two.value,
            viewModelClassTwo.image_three.value,
            viewModelClassTwo.image_four.value,
            viewModelClassTwo.image_five.value,
            viewModelClassTwo.image_six.value
        )

        images.add(singlePreviewModel)


        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val customItemAnimator = CustomItemAnimator()
        bind.priviewImagesRecyclerview.itemAnimator = customItemAnimator
        layoutManager.isSmoothScrollbarEnabled = true
        bind.priviewImagesRecyclerview.layoutManager = layoutManager


        val circularProgressDrawable = CircularProgressDrawable(requireContext())
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val adapter = com.lornamobileappsdev.my_marketplace.adapter.SimpleImageViewRecyclerview(
            requireContext(),
            images.toMutableList(),
            requireActivity(),
            viewModelClass
        )
        bind.priviewImagesRecyclerview.adapter = adapter

        bind.bk.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null).commit()
        }


        return bind.root
    }

}