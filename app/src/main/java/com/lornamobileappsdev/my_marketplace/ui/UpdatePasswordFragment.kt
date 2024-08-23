package com.lornamobileappsdev.my_marketplace.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentUpdateEmailBinding
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdatePasswordFragment : AppCompatActivity() {

    lateinit var _bind: FragmentUpdateEmailBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment

        _bind = FragmentUpdateEmailBinding.inflate(layoutInflater)

        supportActionBar!!.hide()


        setContentView(bind.root)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)


        viewModelClass.queryUserDetails().asLiveData().observe(this@UpdatePasswordFragment, Observer {p->

            val circularProgressDrawable = CircularProgressDrawable(this@UpdatePasswordFragment)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            try {
                if (p.avatar!!.isEmpty()) {
                    Glide.with(this@UpdatePasswordFragment)
                        .asBitmap()
                        .transform(CircleCrop())
                        .placeholder(com.lornamobileappsdev.my_marketplace.R.drawable.ic_avatar)
                        .fitCenter()
                        .into(bind.avater)
                } else {
                    Glide.with(this@UpdatePasswordFragment)
                        .asBitmap()
                        .circleCrop()
                        .load(p.avatar!!)
                        .transform(CircleCrop())
                        .placeholder(circularProgressDrawable)
                        .fitCenter()
                        .into(bind.avater)
                }
            }catch (e:Exception){}
        })

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this@UpdatePasswordFragment, R.color.black)

        bind.cEye.setOnClickListener {
            if(bind.cEye.text.toString().equals("Show")){
                bind.currentPassswordTxt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                bind.cEye.text = "Hide"
            }else{
                bind.currentPassswordTxt.transformationMethod = PasswordTransformationMethod.getInstance()
                bind.cEye.text = "Show"
            }
        }

        bind.nEye.setOnClickListener {
            if(bind.nEye.text.toString().equals("Show")){
                bind.newPasswordTxt.transformationMethod = HideReturnsTransformationMethod.getInstance()
                bind.nEye.text = "Hide"
            }else{
                bind.newPasswordTxt.transformationMethod = PasswordTransformationMethod.getInstance()
                bind.nEye.text = "Show"
            }
        }

        bind.rEye.setOnClickListener {
            if(bind.rEye.text.toString().equals("Show")){
                bind.retypeNewPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                bind.rEye.text = "Hide"
            }else{
                bind.retypeNewPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                bind.rEye.text = "Show"
            }
        }


        bind.btnChange.setOnClickListener {
            viewModelClass.queryUserDetails().asLiveData().observe(this@UpdatePasswordFragment, Observer {p->
                try {
                    bind.progressBarUpdatingUser.visibility = View.VISIBLE
                }catch (e:Exception){}
                try {

                    if (bind.newPasswordTxt.text.toString()
                            .isNotEmpty() || bind.retypeNewPassword.text.toString()
                            .isNotEmpty() || bind.currentPassswordTxt.text.toString().isNotEmpty()
                    ){
                        if(bind.newPasswordTxt.text.toString() == bind.retypeNewPassword.text.toString() && bind.currentPassswordTxt.text.toString() != bind.newPasswordTxt.text.toString()){

                            updatePasswordEmail(p.id!!, bind.currentPassswordTxt.text.toString(), bind.newPasswordTxt.text.toString())

                        }else if(bind.newPasswordTxt.text.toString() == bind.retypeNewPassword.text.toString() && bind.currentPassswordTxt.text.toString() == bind.newPasswordTxt.text.toString()){
                            try {
                                bind.progressBarUpdatingUser.visibility = View.GONE
                            }catch (e:Exception){}
                            Snackbar.make(bind.newPasswordTxt, "Old password shouldnt be the same as the new password", Snackbar.LENGTH_LONG).show()

                        }
                        else if(bind.newPasswordTxt.text.toString() != bind.retypeNewPassword.text.toString()){
                            try {
                                bind.progressBarUpdatingUser.visibility = View.GONE
                            }catch (e:Exception){}
                            Snackbar.make(bind.newPasswordTxt, "New password doesn't match", Snackbar.LENGTH_LONG).show()
                        }
                    }else{
                        try {
                            bind.progressBarUpdatingUser.visibility = View.GONE
                        }catch (e:Exception){}
                        Snackbar.make(bind.newPasswordTxt, "You must fill all fields before continuing", Snackbar.LENGTH_LONG).show()
                    }

                }catch (e:Exception){}
            })
        }

        bind.bk.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(R.id.content, MyProfile()).commit()
        }

    }


    fun updatePasswordEmail(userId:Int, old_password:String, new_password:String){

        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {
               Singleton.Singleton. apiClient.updateUserPasswordDetails(userId, old_password, new_password).enqueue(object : Callback<Boolean>{
                    override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                        val mResponse = response.body()
                        if(response.isSuccessful){
                            if(response.code() == 200){

                                try {
                                    lifecycleScope.launchWhenStarted {
                                        delay(4000L)
                                    }
                                    bind.progressBarUpdatingUser.visibility = View.GONE

                                } catch (e: Exception) {
                                }


                                if(mResponse == true){
                                    viewModelClass.deleteProfileDatatbase()
                                    lifecycleScope.launchWhenStarted {
                                        delay(2000L)
                                    }

                                    val alertDialog = AlertDialog.Builder(this@UpdatePasswordFragment)
                                    alertDialog.setCancelable(false)
                                    alertDialog.setTitle("Update password")
                                    alertDialog.setMessage("Password successfully updated.Login with your new password.")
                                    alertDialog.setPositiveButton("Login"){_,_ ->
                                        val intent = Intent(this@UpdatePasswordFragment, SuccessFullPageFragment::class.java)
                                        startActivity(intent)
                                    }.show()

                                }else{
                                    Snackbar.make(bind.newPasswordTxt, "Current password not found", Snackbar.LENGTH_LONG).show()
                                }
                            }else{
                                try {
                                    bind.progressBarUpdatingUser.visibility = View.GONE
                                }catch (e:Exception){}

                                Snackbar.make(bind.newPasswordTxt, "${mResponse}", Snackbar.LENGTH_LONG).show()
                            }
                        }else{
                            try {
                                bind.progressBarUpdatingUser.visibility = View.GONE
                            }catch (e:Exception){}

                            Snackbar.make(bind.newPasswordTxt, "${mResponse}", Snackbar.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Boolean>, t: Throwable) {
                        try {
                            bind.progressBarUpdatingUser.visibility = View.GONE
                        }catch (e:Exception){}
                        Snackbar.make(bind.newPasswordTxt, t.message.toString(), Snackbar.LENGTH_LONG).show()
                    }

                })


            }catch (e:Exception){}


        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, NoSignalWifiFragment()).commit()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.home -> {
                supportFragmentManager.beginTransaction().replace(R.id.content, MyProfile()).addToBackStack(null).commit()

            }
        }
        return super.onOptionsItemSelected(item)
    }
}