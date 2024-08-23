package com.lornamobileappsdev.my_marketplace.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lornamobileappsdev.my_marketplace.MainActivity
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.AllProductListAdapter
import com.lornamobileappsdev.my_marketplace.adapter.UserProductListAdapter
import com.lornamobileappsdev.my_marketplace.constant.app_name
import com.lornamobileappsdev.my_marketplace.databinding.ActivityProductListUiBinding
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.useCases.CustomItemAnimator
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductListUi : Fragment() {

    private lateinit var binding: ActivityProductListUiBinding

    private val viewModel: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    private val pullingDataViewModel: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PullingDataFromServerViewModel::class.java)
    }

    private val productList = mutableListOf<PostProductResponse>()

    val datas: MutableList<PostProductResponse> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = ActivityProductListUiBinding.inflate(inflater, container, false)

        binding.apply {

            storeName.text = "how to stay safe in ${app_name}"

            viewAllCategoryList.setOnClickListener {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryIconButtonFragment()).commit()
            }

            homeWare.setOnClickListener {
                viewModel.category.value = "HomeWare"
                lifecycleScope.launchWhenStarted { delay(2000L) }
                try {

                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

                } catch (e: Exception) { }
            }

            realEstate.setOnClickListener {
                viewModel.category.value = "Real Estate"
                lifecycleScope.launchWhenStarted { delay(2000L) }
                try {

                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

                } catch (e: Exception) { }
            }

            wristWatch.setOnClickListener {
                viewModel.category.value = "Wristwatches"
                lifecycleScope.launchWhenStarted { delay(2000L) }
                try {

                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

                } catch (e: Exception) { }
            }

            fishing.setOnClickListener {
                viewModel.category.value = "Fishing"
                lifecycleScope.launchWhenStarted { delay(2000L) }
                try {

                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

                } catch (e: Exception) { }
            }

            sneakers.setOnClickListener {
                viewModel.category.value = "Sneakers"
                lifecycleScope.launchWhenStarted { delay(2000L) }
                try {

                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

                } catch (e: Exception) { }
            }

            accessories.setOnClickListener {
                viewModel.category.value = "Accessories"
                lifecycleScope.launchWhenStarted { delay(2000L) }
                try {

                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

                } catch (e: Exception) { }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        viewModel.products.observe(viewLifecycleOwner, Observer {
            it.map { it }.forEach {
                productList.add(it)
                Log.i("itemsData", productList.toString())
            }

            viewModel.gridView.observe(viewLifecycleOwner, Observer {
                if (it == 0) {
                    lifecycleScope.launch {
                        getProductFromRoom(productList)
                    }

                } else {

                    lifecycleScope.launch {
                        getProductFromRoomGrid(productList)
                    }
                }
            })
            if(productList.size > 0){
                try {
                    binding.productProgressbar.visibility = View.GONE
                }catch (e:Exception){}
            }
        })


        val window: Window = requireActivity().window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.black)

        binding.noItemFoundWrapper.visibility = View.GONE
        binding.retryGetProduct.visibility = View.GONE
        binding.productNoItem.text = "Items under selected store"

        binding.dashChangeRecyclerStyle.setOnClickListener {
            if (viewModel.gridView.value == 0) {
                viewModel.gridView.value = 1
                binding.dashChangeRecyclerStyleLinearlayout.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_view_list_24
                    )
                )
                binding.dashChangeRecyclerStyle.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_view_comfy_24_blue
                    )
                )
            }
        }

        binding.dashChangeRecyclerStyleLinearlayout.setOnClickListener {
            if (viewModel.gridView.value == 1) {
                viewModel.gridView.value = 0
                binding.dashChangeRecyclerStyleLinearlayout.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.baseline_view_list_24_blue
                    )
                )
                binding.dashChangeRecyclerStyle.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_grid_view_24
                    )
                )
            }
        }

        binding.retryGetProduct.setOnClickListener {
            binding.retryProgressbar.visibility = View.GONE
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }

        binding.stayingSafe.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, StaySfeFragment()).commit()
        }

        binding.dashViewAll.setOnClickListener {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, AllProductFragment()).commit()
        }

    }


    fun CoroutineScope.getProductFromRoom(productList:MutableList<PostProductResponse>) {
        launch {
            try {
                //Toast.makeText(requireContext(), productList.toString(), Toast.LENGTH_LONG).show()
                val layoutManager = LinearLayoutManager(requireContext())
                layoutManager.orientation =LinearLayoutManager.HORIZONTAL
                val customItemAnimator = CustomItemAnimator()
                binding.productListUiRecyclerview.itemAnimator = customItemAnimator
                layoutManager.isSmoothScrollbarEnabled = true
                layoutManager.stackFromEnd = false
                binding.productListUiRecyclerview.layoutManager = layoutManager
                binding.productListUiRecyclerview.visibility = View.VISIBLE
                val adapter1 = UserProductListAdapter(
                    requireContext(),
                    productList,
                    requireActivity(),
                    viewModel,
                    pullingDataViewModel
                )
                binding.productListUiRecyclerview.adapter = adapter1

            }catch (e:java.lang.Exception){}

        }
    }


    fun CoroutineScope.getProductFromRoomGrid(productList:MutableList<PostProductResponse>) {
        launch {
            //Toast.makeText(requireContext(), productList.toString(), Toast.LENGTH_LONG).show()
            val layoutManager = GridLayoutManager(requireContext(), 2)
            val customItemAnimator = CustomItemAnimator()
            binding.productListUiRecyclerview.itemAnimator = customItemAnimator
            layoutManager.isSmoothScrollbarEnabled = true
            layoutManager.reverseLayout = false
            binding.productListUiRecyclerview.layoutManager = layoutManager
            binding.productListUiRecyclerview.visibility = View.VISIBLE

            val adapter = AllProductListAdapter(
                requireContext(),
                productList,
                requireActivity(),
                viewModel,
                pullingDataViewModel
            )
            binding.productListUiRecyclerview.adapter = adapter
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("productListUi", "productListUi")
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
    }

    /*suspend fun CoroutineScope.pullDataFromServer() {
           delay(2000L)
           launch {
               try {
                   binding.productProgressbar.visibility = View.VISIBLE

                   val client = Singleton.Singleton.apiClient.api.getAllProductlistthisIsForUpdateWhenSwipeRefresh()
                   if (client.isSuccessful) {
                       if (client.code() == 200) {
                           viewModel.products.postValue(client.body())
                       } else {
                           Log.i("retrofit", client.body()!!.toString())
                       }
                   } else {
                       Log.i("retrofit", client.body()!!.toString())
                   }

                   binding.productProgressbar.visibility = View.GONE

               } catch (e: Exception) { }
           }

    }*/
}
