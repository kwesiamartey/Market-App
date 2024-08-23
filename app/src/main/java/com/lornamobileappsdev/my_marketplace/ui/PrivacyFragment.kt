package com.lornamobileappsdev.my_marketplace.ui

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.url_for_news
import com.lornamobileappsdev.my_marketplace.databinding.FragmentAboutUsBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel

class PrivacyFragment : Fragment(){
    lateinit var _bind: FragmentAboutUsBinding
    val bind get() = _bind
    var mediaPlayer: MediaPlayer = MediaPlayer()

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bind = FragmentAboutUsBinding.inflate(layoutInflater)

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
                    mWebView.loadUrl("${url_for_news}privacy.php")
                    mWebView.webViewClient = WebViewClient()
                    //StartAppSDK.init(requireContext(), "${R.string.startapp_app_id}", true)


                    //dialogue.findViewById<LinearLayout>(R.id.bottom_banner_container).addView(bannerView)


                } catch (e: Exception) {
                    Log.i("TAG", e.message.toString())
                }
            }
            false -> {
                try {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                } catch (e: Exception) {
                }
            }
        }

        bind.bk.setOnClickListener {
            val intent = Intent(requireContext(), SettingsFragment::class.java)
            startActivity(intent)
        }

        return bind.root
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    private fun stop() {
        mediaPlayer.pause()
        mediaPlayer.stop()
        mediaPlayer.release()
        mediaPlayer = MediaPlayer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar!!.title = "Privacy"
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {

                try{
                    val intent = Intent(requireContext(), DashboardFragment::class.java)
                    startActivity(intent)

                }catch (e:Exception){}
            }
        }
        return super.onOptionsItemSelected(item)
    }
}