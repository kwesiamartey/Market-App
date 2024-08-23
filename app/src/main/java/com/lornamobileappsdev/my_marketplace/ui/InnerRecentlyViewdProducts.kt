package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lornamobileappsdev.my_marketplace.adapter.InnerRecentlyViewedAdapter
import com.lornamobileappsdev.my_marketplace.databinding.FragmentInnerRecentlyViewedBinding
import com.lornamobileappsdev.my_marketplace.entity.RecentlyViewData
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel

class InnerRecentlyViewdProducts : Fragment(){

    lateinit var _bind: FragmentInnerRecentlyViewedBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(this)[PullingDataFromServerViewModel::class.java]
    }

    lateinit var layoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _bind = FragmentInnerRecentlyViewedBinding.inflate(layoutInflater)


        (activity as AppCompatActivity).supportActionBar!!.hide()

        if (savedInstanceState != null) {
            getProductList()
        }

        getProductList()

        bind.moveToRecentlyViewed.setOnClickListener {
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, RecentlyViewdProducts()).commit()
            }catch (e:Exception){}
        }


        return bind.root

    }


    //call produt list from room
    fun getProductList() {

        lifecycleScope.launchWhenStarted {

            try {
                viewModelClassTwo.queryRecentlyViewdListOfroductData().asLiveData().observe(viewLifecycleOwner,
                        Observer {
                            Log.i("checikg", "1 = " + it.size.toString())
                            if (it.size == 0) {

                                Log.i("checikg", "3 = " + it.size.toString())
                                bind.innerRecentViewedWrapper.visibility = View.GONE
                            } else {

                                Log.i("checikg", "2 = " + it.size.toString())

                                layoutManager = LinearLayoutManager(requireContext())
                                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                                layoutManager.isSmoothScrollbarEnabled = true
                                bind.innerRecentViewedRecyclerview.layoutManager = layoutManager
                                bind.innerRecentViewedWrapper.visibility = View.VISIBLE
                                val adapter = InnerRecentlyViewedAdapter(
                                    requireContext(),
                                    it as MutableList<RecentlyViewData>,
                                    requireActivity(),
                                    viewModelClass,
                                    viewModelClassTwo
                                )
                                bind.innerRecentViewedRecyclerview.adapter = adapter
                            }
                        })

            } catch (e: Exception) { }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("innerRecent", "innerRecent")
    }
}