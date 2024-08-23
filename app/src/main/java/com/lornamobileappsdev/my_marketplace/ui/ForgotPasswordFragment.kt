package com.lornamobileappsdev.my_marketplace.ui

import android.R
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.lornamobileappsdev.my_marketplace.databinding.FragmentForgotPasswordBinding
import com.lornamobileappsdev.my_marketplace.useCases.FindEmail
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel


class ForgotPasswordFragment : AppCompatActivity() {

    lateinit var _bind:FragmentForgotPasswordBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this)[MyViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate the layout for this fragment

        _bind  = FragmentForgotPasswordBinding.inflate(layoutInflater)

        setContentView(bind.root)

        supportActionBar!!.hide()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:

        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this@ForgotPasswordFragment, R.color.black)

        bind.btnNext.setOnClickListener {
            val email = bind.txtREmail.text.toString()
            submit(email)
        }
        bind.bk.setOnClickListener {
            val intent = Intent(this@ForgotPasswordFragment, SignInFragment::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.home -> {
                val intent = Intent(this@ForgotPasswordFragment, SignInFragment::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun submit(email:String){
        bind.progressBar.visibility = View.VISIBLE
        lifecycleScope.launchWhenStarted {
            when(FindEmail.findEmail(email)){
                true -> {
                    viewModelClass.email.value = email
                    //Toast.makeText(requireContext(), email.toString(), Toast.LENGTH_LONG).show()
                    bind.progressBar.visibility = View.GONE
                    try {
                        supportFragmentManager.beginTransaction().replace(R.id.content, ResetPasswordFragment()).addToBackStack(null).commit()
                    }catch (e:Exception){}
                }
                false ->{
                    //Toast.makeText(requireContext(), email.toString(), Toast.LENGTH_LONG).show()
                    bind.progressBar.visibility = View.GONE
                    val alertDialog = AlertDialog.Builder(this@ForgotPasswordFragment)
                    alertDialog.setTitle("Not Found")
                    alertDialog.setMessage("Email not found or doesn't exist, make sure to enter the correct email")
                    alertDialog.setNegativeButton("Cancel"){_,_->

                    }
                    alertDialog.show()
                }
                else ->{}
            }

        }
    }

}