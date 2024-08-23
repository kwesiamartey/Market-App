package com.lornamobileappsdev.my_marketplace.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.AllProductListAdapter
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentCategoriesBinding
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.useCases.CustomItemAnimator
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class CategoryFragment : Fragment() {

    lateinit var _bind:FragmentCategoriesBinding
    val bind get() = _bind

    val viewModelClass:MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    val viewModelClass1:MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    val expandIn by lazy {
        AnimationUtils.loadAnimation(context, R.anim.anim)
    }
    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity())[PullingDataFromServerViewModel::class.java]
    }

    lateinit var layoutManager :LinearLayoutManager

    private val products = mutableListOf<PostProductResponse>()

    var categoryName:String? = null

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    //lateinit var mAdView: AdView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentCategoriesBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        if(savedInstanceState != null){

            bind.cateName.text = viewModelClass.categoryName.value.toString()
            layoutManager = GridLayoutManager(requireContext(), 2)
            layoutManager.orientation = GridLayoutManager.VERTICAL
            layoutManager.isSmoothScrollbarEnabled = true
            val customItemAnimator = CustomItemAnimator()
            bind.categoryRecyclerview.itemAnimator = customItemAnimator
            bind.categoryRecyclerview.layoutManager = layoutManager

            when (viewModelClass.isNetworkAvailable(test_website)) {
                true -> {
                    try {
                        lifecycleScope.launch {
                            getListOfCategoryProduct()
                        }

                    } catch (e: Exception) { }
                }
                else -> {
                    GlobalScope.launch {
                        delay(3000L)
                        try {
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                        } catch (e: Exception) {

                        }
                    }
                }
            }
        }


        viewModelClass.recyclerViewState = bind.categoryRecyclerview.layoutManager?.onSaveInstanceState()

        if (viewModelClass.recyclerViewState != null) {
            bind.categoryRecyclerview.layoutManager?.onRestoreInstanceState(viewModelClass.recyclerViewState)
        }

        bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white))

        MobileAds.initialize(requireContext()) {}

       /* mAdView = bind.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)*/

        val window: Window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireActivity(), android.R.color.black)

        bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white))


        val spacingInPixels = 12 // replace with your desired spacing


        bind.cateName.text = viewModelClass.categoryName.value.toString()
        layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.reverseLayout = true
        val customItemAnimator = CustomItemAnimator()
        bind.categoryRecyclerview.itemAnimator = customItemAnimator
        bind.categoryRecyclerview.layoutManager = layoutManager

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

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        when (viewModelClass.isNetworkAvailable(test_website)) {
            true -> {
                try {
                    bind.cateName.text = viewModelClass.category.value.toString()
                    //BottomNav()
                    lifecycleScope.launch {
                        getListOfCategoryProduct()
                    }

                } catch (e: Exception) { }
            }
            else -> {
                GlobalScope.launch {
                    delay(3000L)
                    try {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                    } catch (e: Exception) {

                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("category", "category")

    }


    suspend fun CoroutineScope.getListOfCategoryProduct(){
        bind.lFeaturesProgress.visibility = View.VISIBLE
        launch {
            val client = Singleton.Singleton.apiClient.api.getFeaturedProduct("approved", viewModelClass.category.value!!)
            if(client.isSuccessful){
                if(client.code() == 200){
                    launch {
                        bind.result.text = "Result : ${client.body()!!.size}"
                        val adapter = AllProductListAdapter(
                            requireContext(), client.body()!!.toMutableList(),
                            requireActivity(),
                            viewModelClass,
                            viewModelClassTwo
                        )
                        bind.categoryRecyclerview.adapter = adapter
                    }
                    bind.lFeaturesProgress.visibility = View.GONE
                }
            }
        }
    }
}
