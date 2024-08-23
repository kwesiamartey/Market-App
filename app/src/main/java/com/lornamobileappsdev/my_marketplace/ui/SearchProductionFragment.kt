package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.lornamobileappsdev.my_marketplace.adapter.SearchRelatered2ProductListAdapter
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSearchProductionBinding
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.useCases.CustomItemAnimator
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class SearchProductionFragment : AppCompatActivity()  {
    lateinit var _bind: FragmentSearchProductionBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    val viewModelClassTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(this).get(PullingDataFromServerViewModel::class.java)
    }

    lateinit var layoutManager : LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        _bind = FragmentSearchProductionBinding.inflate(layoutInflater)

        setContentView(bind.root)

        supportActionBar!!.hide()
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
        window.statusBarColor = ContextCompat.getColor(this@SearchProductionFragment, android.R.color.black)

        supportActionBar!!.hide()

        bind.searchItem.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that, within s, the count characters beginning at start are about to be replaced by new text with length after.

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that, within s, the count characters beginning at start have just replaced old text that had length before.
                val text = s.toString() // Get the text from the EditText
                // Do something with the text here
                lifecycleScope.launch {
                    searchProductFromCloud(text.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // This method is called to notify you that, somewhere within s, the text has been changed.
                //searchQuery(s.toString())
            }
        })

        try {
            bind.backk.setOnClickListener {
                supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
            }
        }catch (e:Exception){}

        layoutManager = GridLayoutManager(this@SearchProductionFragment, 2)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        val customItemAnimator = CustomItemAnimator()
        bind.searchProduction.itemAnimator = customItemAnimator
        layoutManager.isSmoothScrollbarEnabled = true
        bind.searchProduction.layoutManager = layoutManager
    }


    fun CoroutineScope.searchProductFromCloud(wording:String){
        launch {
            val client = Singleton.Singleton.apiClient.api.searchProductWithName("approved", wording)
            if(client.isSuccessful){
                if(client.code() == 200){
                    bind.result.text = "Result: "+client.body()!!.size.toString()
                    launch {
                        val adapter = SearchRelatered2ProductListAdapter(
                            this@SearchProductionFragment,
                            client.body()!!.toMutableList(),
                            this@SearchProductionFragment,
                            viewModelClass,
                            viewModelClassTwo
                        )
                        bind.searchProduction.adapter = adapter
                    }
                }
            }
        }
    }

}