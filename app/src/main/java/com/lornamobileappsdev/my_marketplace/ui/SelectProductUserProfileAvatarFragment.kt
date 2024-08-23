package com.lornamobileappsdev.my_marketplace.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSelectProductUserProfileAvatarBinding
import com.lornamobileappsdev.my_marketplace.responses.SignupResponses
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SelectProductUserProfileAvatarFragment : Fragment() {

    lateinit var _bind: FragmentSelectProductUserProfileAvatarBinding
    val bind get() = _bind


    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity())[MyViewModel::class.java]
    }

    val userId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    var userIds: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentSelectProductUserProfileAvatarBinding.inflate(layoutInflater)

        if(savedInstanceState != null){

            CoroutineScope(Dispatchers.IO).launch {
                updateProductViewTOServer(1, viewModelClass.productId.value!!.toInt())
            }

            if (viewModelClass.isNetworkAvailable(test_website)) {

                try {
                    bind.productOwnderName.text = viewModelClass.user_name.value.toString()
                    viewModelClass.postuserId.observe(viewLifecycleOwner, Observer {
                        lifecycleScope.launchWhenStarted {
                            getUsers().let {
                                try {
                                    val circularProgressDrawable = CircularProgressDrawable(requireContext())
                                    circularProgressDrawable.strokeWidth = 5f
                                    circularProgressDrawable.centerRadius = 30f
                                    circularProgressDrawable.start()
                                    userId.value = it!!.id

                                    if (it.avatar!!.isEmpty()) {
                                        Glide.with(requireContext())
                                            .asBitmap()
                                            .load(R.drawable.ic_avatar)
                                            .transform(CenterCrop(), RoundedCorners(100))
                                            .placeholder(circularProgressDrawable)
                                            .dontAnimate()
                                            .into(bind.productOwnerAvatar)
                                        Log.i("check_avatar", it.avatar.toString())
                                    } else {
                                        Glide.with(requireContext())
                                            .asBitmap()
                                            .load(it.avatar!!)
                                            .transform(CenterCrop(), RoundedCorners(100))
                                            .placeholder(circularProgressDrawable)
                                            .dontAnimate()
                                            .into(bind.productOwnerAvatar)
                                        Log.i("check_avatar", it.avatar.toString())
                                    }

                                } catch (e: Exception) { }
                            }
                            bind.userMainWrapper.visibility = View.VISIBLE
                        }
                    })
                }catch (e:Exception){}

            } else { }
        }

        bind.sglMoveToUserProfile.setOnClickListener {
            viewModelClass.postuserId.value = viewModelClass.postuserId.value
            //Toast.makeText(requireContext(), userId.toString(), Toast.LENGTH_LONG).show()
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, UserProfileFragment()).commit()
        }

        viewModelClass.postuserId.observe(viewLifecycleOwner, Observer {
            CoroutineScope(Dispatchers.IO).launch {
                updateProductViewTOServer(1, it.toInt())
            }
        })



        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (viewModelClass.isNetworkAvailable(test_website)) {
            try {
                bind.productOwnderName.text = viewModelClass.user_name.value.toString()
                viewModelClass.postuserId.observe(viewLifecycleOwner, Observer {
                    lifecycleScope.launchWhenStarted {
                        getUsers().let {



                            try {
                                val circularProgressDrawable = CircularProgressDrawable(requireContext())
                                circularProgressDrawable.strokeWidth = 5f
                                circularProgressDrawable.centerRadius = 30f
                                circularProgressDrawable.start()
                                userId.value = it!!.id

                                if (it.avatar!!.isEmpty()) {
                                    Glide.with(requireContext())
                                        .asBitmap()
                                        .load(it.avatar!!)
                                        .transform(CircleCrop())
                                        .placeholder(R.drawable.ic_avatar)
                                        .fitCenter()
                                        .into(bind.productOwnerAvatar)
                                    Log.i("check_avatar", it.avatar.toString())
                                } else {
                                    Glide.with(requireContext())
                                        .asBitmap()
                                        .load(it.avatar!!)
                                        .transform(CircleCrop())
                                        .fitCenter()
                                        .into(bind.productOwnerAvatar)
                                    Log.i("check_avatar", it.avatar.toString())
                                }
                            } catch (e: Exception) { }
                        }
                        bind.userMainWrapper.visibility = View.VISIBLE
                    }
                })
            }catch (e:Exception){}
        } else { }
    }

    suspend fun updateProductViewTOServer(view_numver: Int, post_id: Int) {
        delay(100L)
        try {
            Singleton.Singleton.apiClient.api.updateNumberViewsProductList(view_numver, post_id).enqueue(object :
                Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            response.body()!!
                            try {
                                viewModelClass.views.observe(viewLifecycleOwner, Observer {
                                    var views = it.toInt() + 1
                                    viewModelClass.updateNumberViewsProductList(views ,post_id)
                                })

                            }catch (e:Exception){}
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }

                override fun onFailure(call: Call<Boolean>, t: Throwable) {
                    Log.i("upServer", t.message.toString())
                }

            })
        }catch (e:Exception){}
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("avatar", "avatar")
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