package com.lornamobileappsdev.my_marketplace.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.AllProductListAdapter
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentAllProductBinding
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class AllProductFragment : Fragment() {

    lateinit var _bind:FragmentAllProductBinding
    val bind get() = _bind


    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity())[MyViewModel::class.java]
    }

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity())[PullingDataFromServerViewModel::class.java]
    }

    val datas: MutableList<PostProductResponse> = mutableListOf()


    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    lateinit var layoutManager: LinearLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _bind = FragmentAllProductBinding.inflate(layoutInflater)


        if(savedInstanceState != null){
            try {
                bind.menu.setOnClickListener {

                    val bottomDrawar = BottomSheetDialog(requireContext())
                    bottomDrawar.setContentView(R.layout.dash_batoomsheet_manu)
                    bottomDrawar.setCancelable(true)

                    bottomDrawar.findViewById<TextView>(R.id.setting)!!.setOnClickListener {
                        bottomDrawar.hide()
                        val intent = Intent(requireContext(), SettingsFragment::class.java)
                        startActivity(intent)
                    }
                    bottomDrawar.findViewById<TextView>(R.id.logout)!!.setOnClickListener {
                        bottomDrawar.hide()

                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setTitle("Logout")
                        alertDialog.setMessage("Are you sure you want to logout?")
                        alertDialog.setNegativeButton("Cancel") { _, _ ->

                        }
                        alertDialog.setPositiveButton("Logout") { _, _ ->
                            viewModelClass.deleteProfileDatatbase()
                            viewModelClass.deleteAllproductDatatbase()
                            /*  val intent = Intent(requireContext(), MainActivity::class.java)
                             startActivity(intent)*/
                            requireActivity().finish()
                            requireActivity().finishAffinity()
                        }

                        alertDialog.show()
                    }


                    bottomDrawar.show()
                }

                bind.search.setOnClickListener {
                    val intent = Intent(requireContext(), SearchProductionFragment::class.java)
                    startActivity(intent)
                }

                bind.backk.setOnClickListener {
                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
                }
            }catch (e:Exception){}

            if (viewModelClass.isNetworkAvailable(test_website)) {

                try {
                    lifecycleScope.launchWhenStarted {


                        try {
                            Log.i("allproductAdapt", "${datas}")

                            if(datas.size == 0){
                                bind.txtNothing.visibility = View.VISIBLE
                                bind.allfeaturedprogress.visibility = View.GONE
                            }else{
                                bind.result.text = "Result: ${datas.size}"
                                pullDataFromServer()
                            }

                        } catch (e: Exception) { }


                    }


                }catch (e:Exception){}



            } else {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
            }
        }


        bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white))

        val window: Window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireContext(), android.R.color.black)

        try {
            bind.menu.setOnClickListener {

                val bottomDrawar = BottomSheetDialog(requireContext())
                bottomDrawar.setContentView(R.layout.dash_batoomsheet_manu)
                bottomDrawar.setCancelable(true)

                bottomDrawar.findViewById<TextView>(R.id.setting)!!.setOnClickListener {
                    bottomDrawar.hide()
                    val intent = Intent(requireContext(), SettingsFragment::class.java)
                    startActivity(intent)
                }
                bottomDrawar.findViewById<TextView>(R.id.logout)!!.setOnClickListener {
                    bottomDrawar.hide()

                    val alertDialog = AlertDialog.Builder(requireContext())
                    alertDialog.setTitle("Logout")
                    alertDialog.setMessage("Are you sure you want to logout?")
                    alertDialog.setNegativeButton("Cancel") { _, _ ->

                    }
                    alertDialog.setPositiveButton("Logout") { _, _ ->
                        viewModelClass.deleteProfileDatatbase()
                        viewModelClass.deleteAllproductDatatbase()
                        viewModelClass.deleterecentlyviewdata()
                        viewModelClass.deleteMySavedProductAll()
                        /*  val intent = Intent(requireContext(), MainActivity::class.java)
                         startActivity(intent)*/
                        requireActivity().finish()
                        requireActivity().finishAffinity()
                    }

                    alertDialog.show()
                }


                bottomDrawar.show()
            }

            bind.search.setOnClickListener {
                val intent = Intent(requireContext(), SearchProductionFragment::class.java)
                startActivity(intent)
            }

            bind.backk.setOnClickListener {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
            }
        }catch (e:Exception){}


        layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.isSmoothScrollbarEnabled = true
        bind.allProductRecyclerview.layoutManager = layoutManager


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.hide()

        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {
                lifecycleScope.launch {
                    pullDataFromServer()
                }

            }catch (e:Exception){}



        } else {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }
    }


    suspend fun CoroutineScope.pullDataFromServer() {

        launch {
            try {
                bind.allfeaturedprogress.visibility = View.VISIBLE

                val okHttpClient = OkHttpClient().newBuilder()
                    .connectTimeout(3, TimeUnit.MINUTES)
                    .readTimeout(3, TimeUnit.MINUTES)
                    .writeTimeout(3, TimeUnit.MINUTES)
                    .build()

                val retrofit: Retrofit.Builder by lazy {
                    Retrofit.Builder().baseUrl(BuildConfig.API_URL)
                        .client(okHttpClient).addConverterFactory(GsonConverterFactory.create())
                }
                val retrofitApi: Api by lazy {
                    retrofit.client(okHttpClient)
                    retrofit.build().create(Api::class.java)
                }
                val apiClient = ApiClient(retrofitApi)
                val client = apiClient.api.getAllProductlistthisIsForUpdateWhenSwipeRefresh()
                if (client.isSuccessful) {
                    if (client.code() == 200) {
                        bind.result.text = "Result: ${client.body()!!.size}"
                        launch {
                            val adapter = AllProductListAdapter(
                                requireContext(),
                                client.body() as MutableList<PostProductResponse>,
                                requireActivity(),
                                viewModelClass,
                                viewModelClassTwo
                            )
                            bind.allProductRecyclerview.adapter = adapter
                        }
                        bind.txtNothing.visibility = View.GONE
                        bind.allfeaturedprogress.visibility = View.GONE

                    } else {
                        Log.i("retrofit", client.body()!!.toString())
                    }
                } else {
                    Log.i("retrofit", client.body()!!.toString())
                }

                bind.allfeaturedprogress.visibility = View.GONE

            } catch (e: Exception) { }
        }

    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("allcategory", "allcategory")



    }
}