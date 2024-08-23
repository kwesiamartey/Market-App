/*
package com.yupee.yupee.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.yupee.yupee.R
import com.yupee.yupee.adapter.CountryListAdapter
import com.yupee.yupee.adapter.FeaturedProductListAdapter
import com.yupee.yupee.adapter.ProductListAdapter
import com.yupee.yupee.databinding.FragmentFeaturedProductBinding
import com.yupee.yupee.databinding.FragmentListOfCountrieBinding
import com.yupee.yupee.entity.PostProductResponse
import com.yupee.yupee.models.CountryListData
import com.yupee.yupee.models.FeaturedProductData
import com.yupee.yupee.viewModels.MyViewModel


class FeaturedFragment : Fragment() {

    lateinit var _bind:FragmentFeaturedProductBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bind = FragmentFeaturedProductBinding.inflate(layoutInflater)


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        viewModelClass.productId.observe(viewLifecycleOwner, Observer {
            try {

                try {
                    viewModelClass.queryCategoryListOfProduct( type = it!!).asLiveData().observe(viewLifecycleOwner, Observer {
                        Log.i("cate", "${it}")

                        try {
                            val layoutManager = LinearLayoutManager(requireContext())
                            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                            layoutManager.isSmoothScrollbarEnabled = true
                            bind.featuredProductRecyclerview.layoutManager = layoutManager
                            val featuredProductData = mutableListOf<FeaturedProductData>()
                            val adapter = FeaturedProductListAdapter(requireContext(), featuredProductData, requireActivity())
                            bind.featuredProductRecyclerview.adapter = adapter
                        }catch (e:Exception){}
                    })
                }catch (e:Exception){

                }
            }catch (e:Exception){}
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.user_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            android.R.id.home -> {
                (requireActivity() as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, DashboardFragment()).commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

}*/
