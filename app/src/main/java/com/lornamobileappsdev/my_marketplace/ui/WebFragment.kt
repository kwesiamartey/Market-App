package com.lornamobileappsdev.my_marketplace.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentWebBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel



class WebFragment : Fragment() {

    lateinit var _bind: FragmentWebBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentWebBinding.inflate(layoutInflater)

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

        val toolbar:Toolbar = bind.webToolbar
        toolbar.findViewById<TextView>(R.id.bk_btn).setOnClickListener {
            try{
                val intent = Intent(requireContext(), DashboardFragment::class.java)
                startActivity(intent)
            }catch (e:Exception){}
        }

        when (viewModelClass.isNetworkAvailable(test_website)) {
            true -> {
                try {
                    val mWebView: WebView = bind.wbView
                    val webSettings: WebSettings = mWebView.settings.apply {
                        mWebView.settings
                        javaScriptEnabled = true
                        javaScriptEnabled = true
                        builtInZoomControls = false
                        setSupportZoom(false)
                        javaScriptCanOpenWindowsAutomatically = true
                        allowFileAccess = true
                        domStorageEnabled = true
                        viewModelClass.link.observe(viewLifecycleOwner, Observer { u ->
                            Log.i("mini", "url = ${u}")
                            mWebView.loadUrl("${u}")
                        })
                        mWebView.webViewClient = WebViewClient()
                    }


                } catch (e: Exception) {
                    Log.i("TAG", e.message.toString())
                }
            }
            false -> {
                try {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                } catch (e: Exception) { }
            }
        }

        return bind.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        setHasOptionsMenu(false)
        (activity as AppCompatActivity).supportActionBar!!.hide()
        super.onViewCreated(view, savedInstanceState)

    }

}
