package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.lornamobileappsdev.my_marketplace.adapter.CountryListAdapter
import com.lornamobileappsdev.my_marketplace.databinding.FragmentListOfCountrieBinding
import com.lornamobileappsdev.my_marketplace.responses.CountryListDataResponse
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel


class ListOfCountrieFragment : AppCompatActivity() {

    lateinit var _bind: FragmentListOfCountrieBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    val countries = mutableListOf<CountryListDataResponse>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        _bind = FragmentListOfCountrieBinding.inflate(layoutInflater)

        setContentView(bind.root)

        supportActionBar!!.show()
        supportActionBar!!.title = "List of countries"
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)

        try {
            bind.pro.visibility = View.VISIBLE
            viewModelClass.queryListOfCountries().asLiveData().observe(this@ListOfCountrieFragment, Observer {
                Log.i("country", it.size.toString())
                val layoutManager = LinearLayoutManager(this)
                layoutManager.orientation = LinearLayoutManager.VERTICAL
                bind.countryListRecyclerviewed.layoutManager = layoutManager
                val adapater = CountryListAdapter(this@ListOfCountrieFragment, it , this@ListOfCountrieFragment, viewModelClass)
                bind.countryListRecyclerviewed.adapter = adapater
            })
            bind.pro.visibility = View.GONE
        } catch (e: Exception) { }

    }

}