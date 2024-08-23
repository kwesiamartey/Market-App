package com.lornamobileappsdev.my_marketplace.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.facebook.shimmer.ShimmerFrameLayout
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentUserProfileHeadersBinding
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.delay

class UserProfileHeadersFragment : Fragment() {

    lateinit var _bind: FragmentUserProfileHeadersBinding
    val bind get() = _bind

    val userId: Int? = null

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MyViewModel::class.java)
    }

    lateinit var shimmerFrameLayout: ShimmerFrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentUserProfileHeadersBinding.inflate(layoutInflater)
        shimmerFrameLayout = bind.userProfileShimmer
        shimmerFrameLayout.startShimmer()



        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var poneNumer:String = ""
        lifecycleScope.launchWhenStarted {
            getUsers().let {
                try {
                    Log.i("web_user", "${it}")
                    poneNumer = it!!.phoneNumber.toString()
                    bind.userFullname.text = it.fullName
                    bind.userLocation.text = it.country
                    bind.userLanguage.text = "English"
                    bind.prDescriptionStore.text = it.description
                    bind.userDateJoined.text = it.date_and_time

                    val circularProgressDrawable = CircularProgressDrawable(requireContext())
                    circularProgressDrawable.strokeWidth = 5f
                    circularProgressDrawable.centerRadius = 30f
                    circularProgressDrawable.start()

                    if (it.avatar!!.isEmpty()) {
                            Glide.with(requireContext())
                            .asBitmap()
                            .load(R.drawable.ic_avatar)
                            .transform(CenterCrop(), RoundedCorners(100))
                            .placeholder(circularProgressDrawable)
                            .into(bind.uAvatar)


                    } else {

                        Glide.with(requireContext())
                            .asBitmap()
                            .load(it.avatar)
                            .transform(CenterCrop(), RoundedCorners(100))
                            .placeholder(circularProgressDrawable)
                            .into(bind.uAvatar)
                    }


                    if (it.accountStatus == "Verified") {
                        bind.txtCheckVerifiedUser.text = "Verified"
                        bind.verifyDetails.text = "This store has verified with us so kindly feel free to transaction with them"
                        bind.txtCheckVerifiedUser.setTextColor(resources.getColor(R.color.ColorGreen))
                        bind.txtGetVerified.visibility = View.INVISIBLE
                    } else {
                        bind.txtCheckVerifiedUser.text = "Not Verified"
                        bind.verifyDetails.text = "THis store is not verified yet.."
                        bind.txtCheckVerifiedUser.setTextColor(resources.getColor(R.color.orange))
                        bind.txtGetVerified.visibility = View.VISIBLE
                    }

                    shimmerFrameLayout.visibility = View.GONE
                    shimmerFrameLayout.stopShimmer()
                    bind.userMainWrapper.visibility = View.VISIBLE

                }catch (e:Exception){}

                bind.cntSeller.setOnClickListener {
                    bind.cntSeller.text = poneNumer
                    it.setOnClickListener {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$poneNumer"))
                        startActivity(intent)
                    }
                }

            }

        }
    }


    suspend fun getUsers(): SignupResponses? {
        delay(1000L)
        var userId: Int? = null
        viewModelClass.postuserId.observe(viewLifecycleOwner, Observer {
            userId = it
        })


        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {
                val response = Singleton.Singleton.apiClient.api.getUser(userId!!)
                if (response.isSuccessful) {
                    return response.body()
                    Log.i("iUser", response.body().toString())
                } else {
                    return null
                    Log.i("iUser", response.body().toString())
                }
            } catch (e: Exception) { }

        } else {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }

        return null
    }
}