package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.lornamobileappsdev.my_marketplace.adapter.UserProductListAdapter
import com.lornamobileappsdev.my_marketplace.databinding.FragmentMainItemListBinding
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.useCases.CustomItemAnimator
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainItemListFragment : Fragment() {

    lateinit var _bind:FragmentMainItemListBinding
    val bind get() = _bind

    private val viewModel: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    private val pullingDataViewModel: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PullingDataFromServerViewModel::class.java)
    }

    private val productList = mutableListOf<PostProductResponse>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _bind = FragmentMainItemListBinding.inflate(layoutInflater)



        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.apply {
            dashViewAll.setOnClickListener {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, AllProductFragment()).commit()
            }
        }

        viewModel.products.observe(viewLifecycleOwner, Observer {
            it.map { it }.forEach {
                productList.add(it)
                Log.i("itemsData", productList.toString())
            }
            CoroutineScope(Dispatchers.IO).launch {
                getProductFromRoom(productList)
            }
        })
    }

    fun CoroutineScope.getProductFromRoom(productList:MutableList<PostProductResponse>) {
        launch {
            try {
                //Toast.makeText(requireContext(), productList.toString(), Toast.LENGTH_LONG).show()
                val layoutManager = LinearLayoutManager(requireContext())
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                val customItemAnimator = CustomItemAnimator()
                bind.mainUiRecyclerview.itemAnimator = customItemAnimator
                layoutManager.isSmoothScrollbarEnabled = true
                layoutManager.stackFromEnd = false
                bind.mainUiRecyclerview.layoutManager = layoutManager
                bind.mainUiRecyclerview.visibility = View.VISIBLE
                val adapter1 = UserProductListAdapter(
                    requireContext(),
                    productList,
                    requireActivity(),
                    viewModel,
                    pullingDataViewModel
                )
                bind.mainUiRecyclerview.adapter = adapter1

            }catch (e:java.lang.Exception){}

        }
    }
}