package com.lornamobileappsdev.my_marketplace.ui


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.databinding.FragmentDashboardBinding
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel


class DashboardFragment : Fragment() {

    lateinit var _bind: FragmentDashboardBinding
    val bind get() = _bind
    val viewModel: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    val viewModelTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity()).get(PullingDataFromServerViewModel::class.java)
    }

    val storeList: MutableList<com.lornamobileappsdev.my_marketplace.entity.PostProductResponse> =
        mutableListOf()
    val storeList1: MutableList<PostProductResponse> = mutableListOf()


    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    val expandIn by lazy {
        AnimationUtils.loadAnimation(requireContext(), R.anim.anim)
    }

    var userDetails: Boolean = false

    // Initialize the UMP SDK

    private lateinit var consentInformation: ConsentInformation
    private lateinit var consentForm: ConsentForm

    val localListSizw: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _bind = FragmentDashboardBinding.inflate(layoutInflater)

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


        // Set tag for under age of consent. false means users are not under
        // age.
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()


        consentInformation = UserMessagingPlatform.getConsentInformation(requireContext())
        consentInformation.requestConsentInfoUpdate(requireActivity(),
            params,
            {
                // The consent information state was updated.
                // You are now ready to check if a form is available.
                if (consentInformation.isConsentFormAvailable) {
                    loadForm()
                }
            },
            {
                // Handle the error.
            })
        consentInformation.reset()


        return bind.root

    }

    fun loadForm() {
        // Loads a consent form. Must be called on the main thread.
        UserMessagingPlatform.loadConsentForm(
            requireContext(),
            {
                if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.REQUIRED) {
                    consentForm.show(
                        requireActivity(),
                        ConsentForm.OnConsentFormDismissedListener {
                            if (consentInformation.consentStatus == ConsentInformation.ConsentStatus.OBTAINED){
                                // App can start requesting ads.
                            }
                            // Handle dismissal by reloading form.
                            loadForm()
                        }
                    )
                }
            },
            {
                // Handle the error.
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        if (savedInstanceState != null) {

            bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white))

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
                        viewModel.deleteProfileDatatbase()
                        viewModel.deleteAllproductDatatbase()
                        viewModel.deleterecentlyviewdata()
                        viewModel.deleteMySavedProductAll()
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
        }

        bind.bottomAppBar.setBackgroundColor(resources.getColor(R.color.white))

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
                    viewModel.deleteProfileDatatbase()
                    viewModel.deleteAllproductDatatbase()
                    viewModel.deleterecentlyviewdata()
                    viewModel.deleteMySavedProductAll()
                    /*  val intent = Intent(requireContext(), MainActivity::class.java)
                     startActivity(intent)*/
                    requireActivity().finish()
                    requireActivity().finishAffinity()
                    bottomDrawar.dismiss()
                }

                alertDialog.show()
            }
            bottomDrawar.show()
        }

        bind.search.setOnClickListener {
            val intent = Intent(requireContext(), SearchProductionFragment::class.java)
            startActivity(intent)
        }

        bind.swiperefresh.setOnRefreshListener {
            // Signal SwipeRefreshLayout to start the progress indicator
            bind.swiperefresh.isRefreshing = true
            // Start the refresh background task.
            // This method calls setRefreshing(false) when it's finished.
            /*lifecycleScope.launchWhenStarted {
                viewModelTwo.compareCloudSizeAndLocalSize()
                *//*if(viewModelTwo.decision.value == 1){
                    Log.i("getCloudDatasize", "decision ${viewModelTwo.decision.value!!}")
                }
                else
                {
                    Log.i("getCloudDatasize", "decision ${viewModelTwo.decision.value!!}")
                    viewModelTwo.post_product_list.value!!.asFlow().collect{
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val products = com.lornamobileappsdev.my_marketplace.entity.PostProductResponse(
                                    id = it.id,
                                    userId = it.userId,
                                    user_name = it.user_name,
                                    title = it.title,
                                    currenxy = it.currenxy,
                                    price = it.price.toString(),
                                    category = it.category,
                                    country = it.country,
                                    location = it.location,
                                    desc = it.desc,
                                    dateAndtimme = it.dateAndtimme,
                                    type = it.type,
                                    negotiation = it.negotiation,
                                    image = it.image,
                                    image_two = it.image_two,
                                    image_three = it.image_three,
                                    image_four = it.image_four,
                                    image_five = it.image_five,
                                    image_six = it.image_six,
                                    paid_product = it.paid_product,
                                    pst_phone_number = it.pst_phone_number.toString(),
                                    subscription_type = it.subscription_type,
                                    subscription_date_start = it.subscription_date_start,
                                    subscription_date_due = it.subscription_date_due,
                                    views = it.views,
                                    rates = it.rates,
                                    approval_status = it.approval_status
                                )

                                viewModel.insertIntoProductDatatbase(products)

                            } catch (e: Exception) {
                                // Handle exception
                            }
                        }
                    }
                }*//*
            }*/

            try{
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).commit()
            }catch (e:Exception){}

            Handler(Looper.myLooper()!!).postDelayed({
                bind.swiperefresh.isRefreshing = false
            }, 4000)
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dashboard", "dashboard")

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Leave empty do disable back press or
                    // write your code which you want
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,
            callback
        )
    }

}