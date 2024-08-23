package com.lornamobileappsdev.my_marketplace.ui

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.AllProductListAdapter

import com.lornamobileappsdev.my_marketplace.databinding.FragmentMyProfileBinding
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.useCases.CustomItemAnimator
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.util.*


class MyProfile : Fragment(){

    lateinit var _bind: FragmentMyProfileBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity())[PullingDataFromServerViewModel::class.java]
    }

    val expandIn by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
    }

    lateinit var countrySwitch: Spinner
    var countrySelected: String = ""

    val products = mutableListOf<PostProductResponse>()

    var userIds: Int? = null

    lateinit var layoutManager: LinearLayoutManager

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Inflate the layout for this fragment
        _bind = FragmentMyProfileBinding.inflate(layoutInflater)


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


        if(savedInstanceState != null){
            getMyProductList()
        }




        bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white))
        getMyProductList()
        layoutManager = GridLayoutManager(requireContext(),2)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.reverseLayout = true
        bind.userRecyclerview.layoutManager = layoutManager
        val customItemAnimator = CustomItemAnimator()
        bind.userRecyclerview.itemAnimator = customItemAnimator

        val s = SimpleDateFormat("dd/MM/yyyy") //,hh:mm:ss
        val format: String = s.format(Date())
        val timeStamp = format

        val ss = SimpleDateFormat("hh:mm:ss") //,hh:mm:ss
        val format_time: String = ss.format(Date())
        val timeSt = format_time

        return bind.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

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

    }

    fun getMyProductList() {
        viewModelClass.queryUserDetails().asLiveData().observe(viewLifecycleOwner, Observer { s ->
            //Toast.makeText(requireContext(), s.id.toString(), Toast.LENGTH_LONG).show()
            lifecycleScope.launchWhenStarted {
                try {
                   pullDataFromServer(s.id!!)
                } catch (e: Exception) {
                }
            }
        })
    }


    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("myProfile", "myProfile")

    }

    suspend fun CoroutineScope.pullDataFromServer(id:Int) {

        launch {
            try {
                bind.llProgressbar.visibility = View.VISIBLE
                val client = Singleton.Singleton.apiClient.api.getuserAllProductlist("approved", id)
                if (client.isSuccessful) {
                    if (client.code() == 200) {
                        launch {
                            if(client.body().isNullOrEmpty()){
                                bind.userproductNoItem.text = "This store is empty."
                            }else{
                                bind.userproductNoItem.text = "Items under selected store"

                                val adapter = AllProductListAdapter(
                                    requireContext(),
                                    client.body()!!.toMutableList(),
                                    requireActivity(),
                                    viewModelClass,
                                    viewModelClassTwo
                                )

                                bind.userRecyclerview.adapter = adapter
                            }
                        }

                    } else {
                        Log.i("retrofit", client.body()!!.toString())
                    }
                } else {
                    Log.i("retrofit", client.body()!!.toString())
                }

                bind.llProgressbar.visibility = View.GONE

            } catch (e: Exception) { }
        }

    }
}