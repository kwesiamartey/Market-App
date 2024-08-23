package com.lornamobileappsdev.my_marketplace.ui

import android.R
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.GridLayoutManager
import androidx.room.TypeConverter
import com.lornamobileappsdev.my_marketplace.adapter.FeaturedProductListAdapter
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentFeedbackAndFeaturedProductBinding
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.useCases.CustomItemAnimator
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.*
import java.text.DecimalFormat
import java.util.*


class FeedbackAndFeaturedProductFragment : Fragment() {

    lateinit var _bind:FragmentFeedbackAndFeaturedProductBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    val viewModelClass1: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(requireActivity())[PullingDataFromServerViewModel::class.java]
    }

    var categor: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentFeedbackAndFeaturedProductBinding.inflate(layoutInflater)

        viewModelClass.recyclerViewState = bind.featuredProductRecyclerview.layoutManager?.onSaveInstanceState()
        if (viewModelClass.recyclerViewState != null) {
            bind.featuredProductRecyclerview.layoutManager?.onRestoreInstanceState(viewModelClass.recyclerViewState)
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
        window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.black)

        when (viewModelClass.isNetworkAvailable(test_website)) {

            true -> {
                lifecycleScope.launch {
                    launch {
                        getFeaturedProductFromServer()
                    }
                }
            }
            else ->{
                lifecycleScope.launchWhenStarted {
                    delay(3000L)
                    try {
                        (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                    } catch (e: Exception) { }
                }
            }
        }


        val layoutManager = GridLayoutManager(requireContext(),2)
        layoutManager.orientation = GridLayoutManager.VERTICAL
        layoutManager.isSmoothScrollbarEnabled = true
        layoutManager.offsetChildrenVertical(12)
        bind.featureWrapper.visibility = View.VISIBLE
        bind.featuredProductRecyclerview.layoutManager = layoutManager

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            launch {
                getFeaturedProductFromServer()
            }
        }


        bind.singleSelectedItemViewmore.setOnClickListener {
            viewModelClass.categoryName.value = categor
            try {
                /*val intent = Intent(requireContext(), CategoryFragment::class.java)
                startActivity(intent)*/
                (activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(R.id.content, CategoryFragment()).addToBackStack(null).commit()
            } catch (e: Exception) { }

        }
    }


    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }

    suspend fun getFeaturedProductFromServer(){
        delay(2000L)
        try {
            Log.i("innnner", viewModelClass.category.value!! )
            val client = Singleton.Singleton.apiClient.api.getFeaturedProduct("approved", viewModelClass.category.value!!)
            if(client.isSuccessful){
                if(client.code() == 200){
                    val adapter = FeaturedProductListAdapter(
                        requireContext(),
                        client.body()!!.toMutableList(),
                        requireActivity(),
                        viewModelClass,
                        viewModelClassTwo
                    )
                    bind.featuredProductRecyclerview.adapter = adapter
                }

                val customItemAnimator = CustomItemAnimator()
                bind.featuredProductRecyclerview.itemAnimator = customItemAnimator
                bind.featureWrapper.visibility = View.VISIBLE
                bind.lFeaturesProgress.visibility = View.GONE
            }
        }catch (e:Exception){}
    }

}