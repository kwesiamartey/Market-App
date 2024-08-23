package com.lornamobileappsdev.my_marketplace.ui

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.snackbar.Snackbar
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.FragmentUpdateEmail2Binding
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateEmailFragment : AppCompatActivity() {

    lateinit var _bind:FragmentUpdateEmail2Binding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    var code:String? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment

        _bind = FragmentUpdateEmail2Binding.inflate(layoutInflater)

        setContentView(bind.root)

        supportActionBar!!.hide()

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, android.R.color.black)


        viewModelClass.queryUserDetails().asLiveData().observe(this@UpdateEmailFragment, Observer {p->
            bind.textEmail.setText(p.email)

            val circularProgressDrawable = CircularProgressDrawable(this@UpdateEmailFragment)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            if (p.avatar!!.isEmpty()) {
                Glide.with(this@UpdateEmailFragment)
                    .asBitmap()
                    .load(p.avatar!!)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.ic_avatar)
                    .fitCenter()
                    .into(bind.avater)
            } else {
                Glide.with(this@UpdateEmailFragment)
                    .asBitmap()
                    .circleCrop()
                    .load(p.avatar!!)
                    .transform(CircleCrop())
                    .placeholder(circularProgressDrawable)
                    .fitCenter()
                    .into(bind.avater)
            }
        })

        bind.changeBtn.setOnClickListener {
            viewModelClass.queryUserDetails().asLiveData().observe(this@UpdateEmailFragment, Observer {p->
                code = p.verification_code
                try {

                    if (bind.textEmail.text.toString().isNotEmpty() &&bind.textEmail.text.toString() != p.email ){
                        val alertDialog = AlertDialog.Builder(this@UpdateEmailFragment)
                        alertDialog.setCancelable(false)
                        alertDialog.setTitle("Update email")
                        alertDialog.setMessage("Make sure to provide a valid email address")
                        alertDialog.setNegativeButton("Cancel"){_,_ ->

                        }
                        alertDialog.setPositiveButton("Verify Email"){_,_ ->
                            updateEmail(p.id!!, bind.textEmail.text.toString())
                        }.show()




                    }else{
                        Snackbar.make(bind.textEmail, "Email address can't be the same", Snackbar.LENGTH_LONG).show()
                    }

                }catch (e:Exception){}
            })
        }

        bind.bk.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(android.R.id.content, MyProfile()).commit()
        }

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                try {
                    supportFragmentManager.beginTransaction().replace(android.R.id.content, MyProfile()).addToBackStack(null).commit()
                }catch (e:Exception){}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun updateEmail(userId:Int, email:String){
        try {
            bind.pgr.visibility = View.VISIBLE
        }catch (e:Exception){}


        if (viewModelClass.isNetworkAvailable(test_website)) {

            try {

                try {
                    Singleton.Singleton.apiClient.changesEmail(userId, email).enqueue(object :
                        Callback<Boolean> {
                        override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                            val mResponse = response.body()
                            if(response.isSuccessful){
                                if(response.code() == 200){
                                    try {
                                        bind.pgr.visibility = View.GONE
                                    }catch (e:Exception){}
                                    if(mResponse == true){

                                        val alertDialog = AlertDialog.Builder(this@UpdateEmailFragment)
                                        alertDialog.setCancelable(false)
                                        alertDialog.setTitle("Update email")
                                        alertDialog.setMessage("Email successfully updated.Verify email to proceed.")
                                        alertDialog.setPositiveButton("Verify Email"){_,_ ->
                                            viewModelClass.deleteProfileDatatbase()
                                            val intent = Intent(this@UpdateEmailFragment, VerifyEmailAdressFragment::class.java)
                                            startActivity(intent)
                                        }.show()
                                    }else{
                                        Snackbar.make(bind.pgr, mResponse.toString(), Snackbar.LENGTH_LONG).show()
                                    }
                                }else{
                                    try {
                                        bind.pgr.visibility = View.GONE
                                    }catch (e:Exception){}

                                    Snackbar.make(bind.pgr, "${mResponse}", Snackbar.LENGTH_LONG).show()
                                }
                            }else{
                                try {
                                    bind.pgr.visibility = View.GONE
                                }catch (e:Exception){}

                                Snackbar.make(bind.pgr, "${mResponse}", Snackbar.LENGTH_LONG).show()
                            }
                        }

                        override fun onFailure(call: Call<Boolean>, t: Throwable) {
                            try {
                                bind.pgr.visibility = View.GONE
                            }catch (e:Exception){}
                            Snackbar.make(bind.pgr, t.message.toString(), Snackbar.LENGTH_LONG).show()
                        }

                    })
                }catch (e:Exception){}


            }catch (e:Exception){}


        } else {
            supportFragmentManager.beginTransaction()
                .replace(android.R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
        }

    }

    override fun onBackPressed() {
        //Todo
    }
}