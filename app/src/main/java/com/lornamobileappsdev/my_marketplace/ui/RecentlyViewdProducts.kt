package com.lornamobileappsdev.my_marketplace.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.RecentlyViewedAdapter
import com.lornamobileappsdev.my_marketplace.databinding.FragmentRecentlyViewedBinding
import com.lornamobileappsdev.my_marketplace.entity.RecentlyViewData
import com.lornamobileappsdev.my_marketplace.useCases.OnClickListernersRecyclerMySavedItem
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel

class RecentlyViewdProducts : Fragment(), OnClickListernersRecyclerMySavedItem {

    lateinit var _bind: FragmentRecentlyViewedBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(this)[PullingDataFromServerViewModel::class.java]
    }

    lateinit var layoutManager: LinearLayoutManager

    lateinit var mAdView: AdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _bind = FragmentRecentlyViewedBinding.inflate(layoutInflater)


        (activity as AppCompatActivity).supportActionBar!!.hide()

        if (savedInstanceState != null) {
            getProductList()
        }

        //bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white));
        MobileAds.initialize(requireContext()) {}

        mAdView = bind.adView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

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

        getProductList()


        mAdView.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }

        bind.bk.setOnClickListener {
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, DashboardFragment()).commit()
            }catch (e:Exception){}
        }

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

        }catch (e:Exception){}

        return bind.root

    }


    //call produt list from room
    fun getProductList() {

        lifecycleScope.launchWhenStarted {
            try {
                viewModelClassTwo.queryRecentlyViewdListOfroductData().asLiveData()
                    .observe(viewLifecycleOwner,
                        Observer {
                            Log.i("checikg", "1 = " + it.size.toString())
                            if (it.size == 0) {

                                Log.i("checikg", "3 = " + it.size.toString())
                                bind.emptySavedTxt.visibility = View.VISIBLE
                            } else {

                                Log.i("checikg", "2 = " + it.size.toString())

                                layoutManager = LinearLayoutManager(requireContext())
                                layoutManager.orientation = LinearLayoutManager.VERTICAL
                                layoutManager.isSmoothScrollbarEnabled = true
                                bind.mySavedRecyclerview.layoutManager = layoutManager
                                bind.emptySavedTxt.visibility = View.GONE
                                val adapter = RecentlyViewedAdapter(
                                    requireContext(),
                                    it.asIterable().reversed() as MutableList<RecentlyViewData>,
                                    requireActivity(),
                                    viewModelClass,
                                    this@RecentlyViewdProducts
                                )
                                bind.mySavedRecyclerview.adapter = adapter
                            }
                        })

            } catch (e: Exception) { }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("savedproduct", "savedproduct")
    }

    override fun onClickListernersRecyclerMySavedItem(id: Int) {
        lifecycleScope.launchWhenStarted {
            viewModelClassTwo.deleteRecentViewedProduct(id)
        }
    }
}