package com.lornamobileappsdev.my_marketplace.ui


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.databinding.FragmentUserProfileHeadersBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay

class MyUserProfileHeadersFragment : Fragment() {

    lateinit var _bind:FragmentUserProfileHeadersBinding
    val bind get() = _bind

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

        shimmerFrameLayout = bind.userProfileShimmer
        shimmerFrameLayout.startShimmer()

        bind.editTxt.visibility = View.VISIBLE

        lifecycleScope.launchWhenStarted {
            getUsers()
        }

        return bind.root
    }


    suspend fun getUsers() {
        delay(1000L)
        var poneNumer:String = ""
        try {
            lifecycleScope.launchWhenStarted {
                viewModelClass.queryUserDetails().asLiveData(Dispatchers.IO).observe(viewLifecycleOwner, Observer {
                    // Toast.makeText(requireContext(), "${it}", Toast.LENGTH_LONG).show()
                    try {
                        Log.i("user", "${it}")
                        try {
                            poneNumer = it.phoneNumber.toString()
                            viewModelClass.postuserId.value = it.id
                            bind.userFullname.text = it.fullName
                            bind.userLocation.text = it.country
                            bind.userLanguage.text = "English"
                            bind.userDateJoined.text = it.dateAndTime
                            bind.prDescriptionStore.text = it.description
                            //bind.myAvatart.setImageBitmap(byteArrayToBitmap(it.avatar!!))
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
                                    .load(it.avatar!!)
                                    .transform(CenterCrop(), RoundedCorners(100))
                                    .placeholder(circularProgressDrawable)
                                    .into(bind.uAvatar)
                            }

                            if (it!!.accountStatus == "Verified") {
                                bind.txtCheckVerifiedUser.text = "Verified"
                                bind.verifyDetails.text = "This store has verified with us so kindly feel free to do transaction with them"
                                bind.txtCheckVerifiedUser.setTextColor(resources.getColor(R.color.ColorGreen))
                                bind.txtGetVerified.visibility = View.INVISIBLE
                            } else {
                                bind.txtCheckVerifiedUser.text = "Not Verified"
                                bind.verifyDetails.text =
                                    "We ask everyone for a few details before verifying account, so get a head start by doing it now"
                                bind.txtCheckVerifiedUser.setTextColor(resources.getColor(R.color.orange))
                                bind.txtGetVerified.visibility = View.VISIBLE
                            }
                        } catch (e: Exception) {
                        }
                    } catch (e: Exception) {
                        Log.i("TAGG", e.message.toString())
                    }
                })
            }
            bind.editTxt.setOnClickListener{

                val bottomDrawar = BottomSheetDialog(requireContext())
                bottomDrawar.setContentView(R.layout.edit_profile_bottomsheet)
                bottomDrawar.setCancelable(true)

                bottomDrawar.findViewById<TextView>(R.id.update_my_detail_btn)!!.setOnClickListener {
                    bottomDrawar.hide()
                    val intent = Intent(requireContext(),ProfileUpdateFragment::class.java)
                    startActivity(intent)
                }
                bottomDrawar.findViewById<TextView>(R.id.updateEmails_btn)!!.setOnClickListener {
                    bottomDrawar.hide()

                    val intent = Intent(requireContext(), UpdateEmailFragment::class.java)
                    startActivity(intent)
                }
                bottomDrawar.findViewById<TextView>(R.id.updateAvatar_btn)!!.setOnClickListener {
                    bottomDrawar.hide()

                    val intent = Intent(requireContext(), UpdateAvatarFragment::class.java)
                    startActivity(intent)
                }
                bottomDrawar.findViewById<TextView>(R.id.updatePasswords_btn)!!.setOnClickListener {
                    bottomDrawar.hide()

                    val intent = Intent(requireContext(), UpdatePasswordFragment::class.java)
                    startActivity(intent)
                }

                bottomDrawar.show()
            }

            bind.cntSeller.setOnClickListener {
                bind.cntSeller.text = poneNumer
                it.setOnClickListener {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$poneNumer"))
                    startActivity(intent)
                }
            }

        } catch (e: Exception) { }

    }
}