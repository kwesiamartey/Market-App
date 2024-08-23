package com.lornamobileappsdev.my_marketplace.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentResetPasswordBinding
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResetPasswordFragment : Fragment() {

    lateinit var _bind:FragmentResetPasswordBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(requireActivity())[MyViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _bind = FragmentResetPasswordBinding.inflate(layoutInflater)
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

        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind.cEye.setOnClickListener {
            if(bind.cEye.text.toString() == "Show"){
                bind.txtNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                bind.cEye.text = "Hide"
            }else{
                bind.txtNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                bind.cEye.text = "Show"
            }
        }

        bind.zEye.setOnClickListener {
            if(bind.zEye.text.toString() == "Show"){
                bind.txtConfirmPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                bind.zEye.text = "Hide"
            }else{
                bind.txtConfirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                bind.zEye.text = "Show"
            }
        }


        bind.btnUpdatePassword.setOnClickListener {
            val reset_code = bind.txtResetCode.text.toString()
            val newPassword = bind.txtNewPassword.text.toString()
            val confirmPassword = bind.txtConfirmPassword.text.toString()

            try {

                if (reset_code.isNotEmpty() && newPassword.isNotEmpty() && confirmPassword.isNotEmpty()
                ){
                    if(newPassword == confirmPassword && reset_code.isNotEmpty()){

                        viewModelClass.email.observe(viewLifecycleOwner, Observer {
                            //Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                            updatePassword(it,reset_code,newPassword)
                        })

                    }else if(newPassword != confirmPassword){

                        Snackbar.make(bind.imageView6, "new password and confirm password doesn't match", Snackbar.LENGTH_LONG).show()

                    }
                }else{
                    Snackbar.make(bind.imageView6, "You must fill all fields before continuing", Snackbar.LENGTH_LONG).show()
                }

            }catch (e:Exception){}

        }
    }


    fun updatePassword(email:String, reset_code:String,newPassword:String){
        try {
            bind.progressBar.visibility = View.VISIBLE
        }catch (e:Exception){}


        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {

                try {
                    Singleton.Singleton.apiClient.updateLoginForgotPassword(email, reset_code, newPassword).enqueue(object :
                        Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            val mResponse = response.body()
                            if(response.isSuccessful){
                                if(response.code() == 200){
                                    try {
                                        bind.progressBar.visibility = View.GONE
                                    }catch (e:Exception){}
                                    if(mResponse == true){
                                        Snackbar.make(bind.imageView6, "Password reset successfully, Please Sign in ", Snackbar.LENGTH_LONG).show()
                                        lifecycleScope.launchWhenStarted {
                                            delay(3000L)
                                        }
                                        try {
                                            val intent = Intent(requireContext(), SignInFragment::class.java)
                                            startActivity(intent)
                                        }catch (e:Exception){}
                                    }else{
                                        Snackbar.make(bind.imageView6, "Current password not found", Snackbar.LENGTH_LONG).show()
                                    }
                                }else{
                                    try {
                                        bind.progressBar.visibility = View.GONE
                                    }catch (e:Exception){}

                                    Snackbar.make(bind.imageView6, "${mResponse}", Snackbar.LENGTH_LONG).show()
                                }
                            }else{
                                try {
                                    bind.progressBar.visibility = View.GONE
                                }catch (e:Exception){}

                                Snackbar.make(bind.imageView6, "${mResponse}", Snackbar.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            try {
                                bind.progressBar.visibility = View.GONE
                            }catch (e:Exception){}
                            Snackbar.make(bind.imageView6, t.message.toString(), Snackbar.LENGTH_LONG).show()
                        }

                    })
                }catch (e:Exception){}

            }catch (e:Exception){}


        } else {
            (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }
    }

}