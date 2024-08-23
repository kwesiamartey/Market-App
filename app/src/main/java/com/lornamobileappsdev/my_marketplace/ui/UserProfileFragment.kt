package com.lornamobileappsdev.my_marketplace.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.*
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentUserProfileBinding
import com.lornamobileappsdev.my_marketplace.entity.PostProductResponse
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.useCases.*
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.*


class UserProfileFragment : Fragment(){
    lateinit var _bind: FragmentUserProfileBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity())[PullingDataFromServerViewModel::class.java]
    }

    val products = mutableListOf<PostProductResponse>()

    var userId: Int? = null
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentUserProfileBinding.inflate(layoutInflater)

        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white))

        if(savedInstanceState != null){

            when (viewModelClass.isNetworkAvailable(test_website)) {

                true -> {
                    try {
                        lifecycleScope.launch {
                            pullDataFromServer()

                        }
                    } catch (e: Exception) { }
                }
                else -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        delay(3000L)
                        try {
                            requireActivity().supportFragmentManager.beginTransaction()
                                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                        } catch (e: Exception) { }
                    }
                }
            }
        }

       lifecycleScope.launch {
           pullDataFromServer()
       }

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

        layoutManager = GridLayoutManager(requireContext(), 2).apply {
            orientation = LinearLayoutManager.VERTICAL
            isSmoothScrollbarEnabled = true
            bind.userRecyclerview.layoutManager = this
        }

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(false)
        super.onViewCreated(view, savedInstanceState)

        when (viewModelClass.isNetworkAvailable(test_website)) {

            true -> {
                try {
                    lifecycleScope.launch {
                        pullDataFromServer()
                    }
                } catch (e: Exception) { }
            }
            else -> {
                    try {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                    } catch (e: Exception) { }
            }
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
                    alertDialog.setNegativeButton("Cancel") { _, _ -> }
                    alertDialog.setPositiveButton("Logout") { _, _ ->
                        viewModelClass.deleteProfileDatatbase()
                        viewModelClass.deleteAllproductDatatbase()
                        viewModelClass.deleterecentlyviewdata()
                        viewModelClass.deleteMySavedProductAll()
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
                    .replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null).commit()
            }
        }catch (e:Exception){}
    }

    suspend fun CoroutineScope.pullDataFromServer() {

        launch {
            try {
                bind.lProgressbar.visibility = View.VISIBLE
                val client = Singleton.Singleton.apiClient.api.getuserAllProductlist("approved", viewModelClass.postuserId.value!!)
                if (client.isSuccessful) {
                    if (client.code() == 200) {
                        launch {
                            if(client.body().isNullOrEmpty()){
                                bind.userproductNoItem.text = "This store is empty."
                            }else{
                                bind.userproductNoItem.text = "Items under selected store"
                                val adapter = ProductListAdapter(
                                    requireContext(),
                                    client.body()!!.toMutableList(),
                                    requireActivity(),
                                    viewModelClass,
                                    viewModelClassTwo
                                )
                                val customItemAnimator = CustomItemAnimator()
                                bind.userRecyclerview.itemAnimator = customItemAnimator
                                bind.userRecyclerview.adapter = adapter
                            }
                        }
                    } else {
                        Log.i("retrofit", client.body()!!.toString())
                    }
                } else {
                    Log.i("retrofit", client.body()!!.toString())
                }

                bind.lProgressbar.visibility = View.GONE

            } catch (e: Exception) { }
        }
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("userprofilefragment", "userprofilefragment")
    }
}
