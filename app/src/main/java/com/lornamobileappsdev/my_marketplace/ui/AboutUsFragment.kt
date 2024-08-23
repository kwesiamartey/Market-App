package com.lornamobileappsdev.my_marketplace.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentAboutUsBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel

class AboutUsFragment : AppCompatActivity(){

    lateinit var _bind: FragmentAboutUsBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        _bind = FragmentAboutUsBinding.inflate(layoutInflater)

        setContentView(bind.root)

        supportActionBar!!.hide()

        when (viewModelClass.isNetworkAvailable(test_website)) {
            true -> {
                try {
                    val mWebView: WebView = bind.wbView
                    val webSettings: WebSettings = mWebView.settings
                    mWebView.settings
                    webSettings.javaScriptEnabled = true
                    webSettings.javaScriptEnabled = true
                    webSettings.builtInZoomControls = false
                    webSettings.setSupportZoom(false)
                    webSettings.javaScriptCanOpenWindowsAutomatically = true
                    webSettings.allowFileAccess = true
                    webSettings.domStorageEnabled = true
                    mWebView.loadUrl("https://structuredappsstreaming.win/about_us.php")
                    mWebView.webViewClient = WebViewClient()
                    //StartAppSDK.init(requireContext(), "${R.string.startapp_app_id}", true)


                    //dialogue.findViewById<LinearLayout>(R.id.bottom_banner_container).addView(bannerView)


                } catch (e: Exception) {
                    Log.i("TAG", e.message.toString())
                }
            }
            false -> {
                try {
                    supportFragmentManager.beginTransaction()
                        .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                } catch (e: Exception) {
                }
            }
        }

        bind.bk.setOnClickListener {
            val intent = Intent(this@AboutUsFragment, SettingsFragment::class.java)
            startActivity(intent)
        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {

                try{
                    val intent = Intent(this@AboutUsFragment, DashboardFragment::class.java)
                    startActivity(intent)
                }catch (e:Exception){}
            }
        }
        return super.onOptionsItemSelected(item)
    }
}