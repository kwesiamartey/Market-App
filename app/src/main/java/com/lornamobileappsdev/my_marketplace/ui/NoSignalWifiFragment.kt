package com.lornamobileappsdev.my_marketplace.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.lornamobileappsdev.my_marketplace.MainActivity
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentNoSignalWifiBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel


class NoSignalWifiFragment : Fragment() {

    lateinit var _bind: FragmentNoSignalWifiBinding
    val bind get()  = _bind
    val viewModeling: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _bind = FragmentNoSignalWifiBinding.inflate(layoutInflater)

        val window: Window = requireActivity().window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.black)

        bind.buttonRetryNetword.setOnClickListener {
            when(viewModeling.isNetworkAvailable(test_website)){
                true -> {
                    Log.i("internet","connected No-Internet frag ${viewModeling.isNetworkAvailable(test_website)}")
                    try {
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)

                    } catch (e: Exception) {
                        Log.i("TAG", e.message.toString())
                    }
                }
                else ->{
                    Log.i("internet","not connec connected No-Internet frag ${viewModeling.isNetworkAvailable(test_website)}")
                    try {
                        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                    }catch (e:Exception){}
                }
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