package com.lornamobileappsdev.my_marketplace.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.*
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.lornamobileappsdev.my_marketplace.adsModel.AdsModel
import com.lornamobileappsdev.my_marketplace.constant.firebase_document_id
import com.lornamobileappsdev.my_marketplace.constant.firebase_id
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentBottomNavigationsHomesBinding
import com.lornamobileappsdev.my_marketplace.navigations.BottomNavigation
import com.lornamobileappsdev.my_marketplace.useCases.MyMessagesList
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


class BottomNavigationsHomesFragment : Fragment() ,BottomNavigation{

    lateinit var _bind: FragmentBottomNavigationsHomesBinding
    val bind get() = _bind

    val viewModel: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    lateinit var mAdView: AdView

    lateinit var firestore : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentBottomNavigationsHomesBinding.inflate(layoutInflater)

        firestore = FirebaseFirestore.getInstance()
        MobileAds.initialize(requireContext()) {}
        mAdView = bind.adViewf

        firestore.collection(firebase_document_id).document(firebase_id).addSnapshotListener { value, error ->
            if (error != null) {
                // Handle the error here
                Log.e("iAds", "Error fetching Firestore document: ${error.message}")
                return@addSnapshotListener  // Exit the function to prevent further execution
            }

            if (value != null && value.exists()) {
                // Convert the Firestore document to your AdsModel class
                val adsModel = value.toObject(AdsModel::class.java)

                if (adsModel != null) {
                    // Now you can access the properties of adsModel
                    Log.i("iAds", "Document data: ${adsModel.status}")
                    if (adsModel.status){
                        val adRequest = AdRequest.Builder().build()
                        mAdView.loadAd(adRequest)
                    }

                } else {
                    Log.e("iAds", "Failed to convert Firestore document to AdsModel")
                }
            } else {
                Log.e("iAds", "Firestore document does not exist or is null")
            }
        }

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

        when (viewModel.isNetworkAvailable(test_website)) {

            true -> {
                lifecycleScope.launchWhenStarted {
                    try {

                        lifecycleScope.launchWhenStarted {
                            viewModel.queryUserDetails().asLiveData()
                                .observe(viewLifecycleOwner, Observer {
                                    if (it == null) {
                                        bind.messageListCount.visibility = View.GONE
                                    } else {
                                        lifecycleScope.launchWhenStarted {
                                            MyMessagesList.my_messages_list(it.id!!).asFlow().onStart {  }
                                                .collectIndexed { index, value ->
                                                    if (value != null) {
                                                        if (value.viewed != "0") {
                                                            try {
                                                                bind.messageListCount.visibility = View.GONE
                                                            }catch (e:Exception){}

                                                        } else {
                                                            try {
                                                                bind.messageListCount.visibility =
                                                                    View.VISIBLE
                                                                Log.i("all_message_1", value.toString())
                                                                bind.messageListCount.text = value.viewed.count().toString()
                                                            } catch (e: Exception) {
                                                            }
                                                        }
                                                    } else {
                                                        try {
                                                            bind.messageListCount.visibility =
                                                                View.GONE
                                                        } catch (e: Exception) {
                                                        }
                                                    }
                                                }

                                        }
                                    }
                                })
                        }


                    } catch (e: Exception) {
                    }
                }
            }
            else -> {
                GlobalScope.launch {
                    delay(3000L)
                    try {
                        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                    } catch (e: Exception) {
                    }
                }
            }
        }

        BottomNav()

        return bind.root
    }

    override fun BottomNav() {
        // Any update that goes her must be done at the single selected item show too / editing product and

        bind.navHome.setOnClickListener {
            //Toast.makeText(requireContext(), "Hello", Toast.LENGTH_LONG).show()
            try {
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
            } catch (e: Exception) {
            }
        }

        bind.navMessage.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModel.queryUserDetails().collect {
                    Log.i("detailing", "${it}")
                    if (it == null) {
                        try {
                            Snackbar.make(
                                bind.navHome,
                                "Please register / Sign in",
                                Snackbar.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                        }
                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                        }
                        try {
                            val intent = Intent(requireContext(), SignUpFragment::class.java)
                                startActivity(intent)
                        } catch (e: Exception) {
                        }
                    } else if (it.accountStatus == "Verified") {
                        try {
                            val intent = Intent(requireContext(), MessagesFragment::class.java)
                                startActivity(intent)

                        } catch (e: Exception) {
                        }

                    } else if (it.accountStatus == "Not verified") {
                        viewModel.email.value = it.email
                        //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                        val alertDialog =
                            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(false)
                        alertDialog.setTitle("Account not verified")
                        alertDialog.setCancelable(true)
                        alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                        alertDialog.setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(
                                requireContext(),
                                VerifyEmailAdressFragment::class.java
                            )
                            startActivity(intent)
                        }
                        alertDialog.show()
                    }
                }
            }
        }

        bind.navAdd.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModel.queryUserDetails().collect {
                    Log.i("detailing", "${it}")
                    if (it == null) {
                        try {
                            Snackbar.make(
                                bind.navHome,
                                "Please register / Sign in",
                                Snackbar.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                        }

                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                        }
                        try {
                            val intent =
                                Intent(requireContext(), SignUpFragment::class.java)
                            startActivity(intent)
                        } catch (e: Exception) {
                        }
                    } else if (it.accountStatus == "Verified") {
                        try {
                            val intent = Intent(requireActivity(), PostAProductFragment::class.java)
                            startActivity(intent)
                           /* val intent =
                                Intent(requireContext(), WebPostUploadFragment::class.java)
                            startActivity(intent)*/
                            /*(activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, WebPostUploadFragment()).commit()*/

                        } catch (e: Exception) {
                        }

                    } else if (it.accountStatus == "Not verified") {
                        viewModel.email.value = it.email
                        //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                        val alertDialog =
                            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(false)
                        alertDialog.setTitle("Account not verified")
                        alertDialog.setCancelable(true)
                        alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                        alertDialog.setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(
                                requireContext(),
                                VerifyEmailAdressFragment::class.java
                            )
                            startActivity(intent)

                        }
                        alertDialog.show()
                    }
                }
            }
        }

        bind.navSaved.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModel.queryUserDetails().collect {
                    Log.i("detailing", "${it}")
                    if (it == null) {
                        try {
                            Snackbar.make(
                                bind.navHome,
                                "Please register / Sign in",
                                Snackbar.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                        }
                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                        }
                        try {
                            val intent =
                                Intent(requireContext(), SignUpFragment::class.java)
                            startActivity(intent)
                        } catch (e: Exception) {
                        }
                    } else if (it.accountStatus == "Verified") {
                        try {
                            val intent =
                                Intent(requireContext(), MySavedProducts::class.java)
                            startActivity(intent)
                        } catch (e: Exception) {
                        }

                    } else if (it.accountStatus == "Not verified") {
                        viewModel.email.value = it.email
                        //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                        val alertDialog =
                            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(false)
                        alertDialog.setTitle("Account not verified")
                        alertDialog.setCancelable(true)
                        alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                        alertDialog.setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(
                                requireContext(),
                                VerifyEmailAdressFragment::class.java
                            )
                            startActivity(intent)
                        }
                        alertDialog.show()
                    }
                }
            }
        }

        bind.navProfile.setOnClickListener {
            lifecycleScope.launchWhenStarted {
                viewModel.queryUserDetails().collect {
                    Log.i("detailing", "${it}")
                    if (it == null) {
                        try {
                            Snackbar.make(
                                bind.navHome,
                                "Please register / Sign in",
                                Snackbar.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                        }
                        lifecycleScope.launchWhenStarted {
                            delay(2000L)
                        }
                        try {
                            val intent =
                                Intent(requireContext(), SignUpFragment::class.java)
                            startActivity(intent)
                        } catch (e: Exception) {
                        }
                    } else if (it.accountStatus == "Verified") {
                        try {
                            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, MyProfile()).addToBackStack(null).commit()
                        } catch (e: Exception) {
                        }

                    } else if (it.accountStatus == "Not verified") {
                        viewModel.email.value = it.email
                        //startActivity(Intent.createChooser(getEmailIntent(mResponse.email!!, "Verification Code", mResponse.verification_code), "Send mail"))
                        val alertDialog =
                            androidx.appcompat.app.AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(false)
                        alertDialog.setTitle("Account not verified")
                        alertDialog.setCancelable(true)
                        alertDialog.setMessage("Your account is created successfully but not verified, Please verify your account.")
                        alertDialog.setPositiveButton("Continue") { _, _ ->
                            val intent = Intent(
                                requireContext(),
                                VerifyEmailAdressFragment::class.java
                            )
                            startActivity(intent)
                        }
                        alertDialog.show()
                    }
                }
            }
        }
    }
}