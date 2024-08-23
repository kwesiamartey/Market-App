package com.lornamobileappsdev.my_marketplace.ui

import android.content.Context
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.adapter.RelatedSearchListAdapter
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSelectedCountryBinding
import com.lornamobileappsdev.my_marketplace.entity.RelatedSearchData
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import java.util.Locale


class SelectedCountryFragment : Fragment() {

    lateinit var _bind: FragmentSelectedCountryBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }
    lateinit var layoutManager : LinearLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentSelectedCountryBinding.inflate(layoutInflater)


        val supportActionBar: ActionBar? = (requireActivity() as AppCompatActivity).supportActionBar
        if (supportActionBar != null) supportActionBar.hide()

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


        layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        layoutManager.isSmoothScrollbarEnabled = true
        bind.recyclerRelatedSearchView.layoutManager = layoutManager

        val countryName = getCountryName(requireContext())
        bind.coutrylistWrapper.visibility = View.VISIBLE
        bind.txtCurrentLocation.text = truncateSentence(countryName)
        Log.i("country name", "1 ="+countryName.toString())


        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*viewModelClass.selectedCountryName.observe(viewLifecycleOwner, Observer {
            bind.txtCurrentLocation.text  = it
            Log.i("country name", "1 ="+it.toString())
        })

        lifecycleScope.launchWhenStarted {
            viewModelClass.queryUserDetails().collect {
                try {
                    // Toast.makeText(requireContext(), "${it.id}", Toast.LENGTH_LONG).show()
                    if(it == null){
                        bind.coutrylistWrapper.visibility = View.GONE
                    }else{
                       *//* bind.coutrylistWrapper.visibility = View.VISIBLE
                        bind.txtCurrentLocation.text = it.country!!*//*
                        Log.i("country name", "2 ="+viewModelClass.selectedCountryName.value.toString())
                    }

                } catch (e: Exception) {
                    Log.i("TAG", e.message.toString())
                }
            }
        }*/

        lifecycleScope.launchWhenStarted {
            viewModelClass.queryRelatedSeachDatatbase().asLiveData().observe(viewLifecycleOwner, Observer {
                try {
                    // Toast.makeText(requireContext(), "${it.id}", Toast.LENGTH_LONG).show()
                    if(it.size == 0){
                        bind.relatedSearcWrapper.visibility = View.GONE
                    }else{
                        bind.relatedSearcWrapper.visibility = View.VISIBLE
                        try {
                            val adapter = RelatedSearchListAdapter(
                                requireContext(),
                                it as MutableList<RelatedSearchData>,
                                requireActivity(),
                                viewModelClass
                            )
                            bind.recyclerRelatedSearchView.adapter = adapter

                        } catch (e: Exception) { }
                    }

                } catch (e: Exception) {
                    Log.i("TAG", e.message.toString())
                }
            })
        }

        bind.allRelatedSearch.setOnClickListener {
            val animationSet = AnimationSet(true)
            animationSet.addAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.like_button_anim))
            it.startAnimation(animationSet)
            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, AllRelatedSearchFragment()).addToBackStack(null).commit()
        }
    }


    fun truncateSentence(sentence: String): String {
        return if (sentence.length > 30) {
            sentence.take(15) + "..."
        } else {
            sentence
        }
    }

    fun getCountryName(context: Context): String {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val countryCode = telephonyManager.simCountryIso
        val locale = Locale("", countryCode)
        return locale.displayCountry
    }

}