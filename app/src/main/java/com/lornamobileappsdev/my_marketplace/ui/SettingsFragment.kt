package com.lornamobileappsdev.my_marketplace.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.databinding.FragmentSettingsBinding
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel


class SettingsFragment : AppCompatActivity() {

    lateinit var _bind:FragmentSettingsBinding
    val bind get() = _bind

    val viewModelClass: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout for this fragment
        _bind = FragmentSettingsBinding.inflate(layoutInflater)
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

        bind.pAbout.setOnClickListener {
            val intent = Intent(this@SettingsFragment, AboutUsFragment::class.java)
            startActivity(intent)
        }
        bind.cEmail .setOnClickListener {
            viewModelClass.queryUserDetails().asLiveData().observe(this, Observer {
                if(it == null){
                    try{
                        val alertDialog = AlertDialog.Builder(this@SettingsFragment)
                        alertDialog.setTitle("Alert!")
                        alertDialog.setMessage("Please you have not registered with us or not logged in")
                        alertDialog.setCancelable(true)
                        alertDialog.setNegativeButton("Sign up"){_,_->
                            val intent = Intent(this@SettingsFragment, SignUpFragment::class.java)
                            startActivity(intent)
                        }
                        alertDialog.setPositiveButton("Log in"){_,_->
                            supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
                        }
                        alertDialog.show()
                    }catch (e:Exception){}
                }else{
                    try {
                        val intent = Intent(this@SettingsFragment, UpdateEmailFragment::class.java)
                        startActivity(intent)
                    }catch (e:Exception){}
                }
            })
        }
        bind.cPassword.setOnClickListener {
            viewModelClass.queryUserDetails().asLiveData().observe(this, Observer {
                if(it == null){
                    try{
                        val alertDialog = AlertDialog.Builder(this@SettingsFragment)
                        alertDialog.setTitle("Alert!")
                        alertDialog.setMessage("Please you have not registered with us or not logged in")
                        alertDialog.setCancelable(true)
                        alertDialog.setNegativeButton("Sign up"){_,_->
                            val intent = Intent(this@SettingsFragment, SignUpFragment::class.java)
                            startActivity(intent)
                        }
                        alertDialog.setPositiveButton("Log in"){_,_->
                            val intent = Intent(this@SettingsFragment, LoginFragment::class.java)
                            startActivity(intent)
                        }
                        alertDialog.show()
                    }catch (e:Exception){}
                }else{
                    try {
                        val intent = Intent(this@SettingsFragment, UpdatePasswordFragment::class.java)
                        startActivity(intent)
                    }catch (e:Exception){}
                }
            })
        }
        bind.dComment.setOnClickListener {}
        bind.pDetails.setOnClickListener {
            viewModelClass.queryUserDetails().asLiveData().observe(this, Observer {
                if(it == null){
                    try{
                        val alertDialog = AlertDialog.Builder(this@SettingsFragment)
                        alertDialog.setTitle("Alert!")
                        alertDialog.setMessage("Please you have not registered with us or not logged in")
                        alertDialog.setCancelable(true)
                        alertDialog.setNegativeButton("Sign up"){_,_->
                            val intent = Intent(this@SettingsFragment, SignUpFragment::class.java)
                            startActivity(intent)
                        }
                        alertDialog.setPositiveButton("Log in"){_,_->
                            supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
                        }
                        alertDialog.show()
                    }catch (e:Exception){}
                }else{
                    try {
                        val intent = Intent(this@SettingsFragment, ProfileUpdateFragment::class.java)
                        startActivity(intent)
                    }catch (e:Exception){}
                }
            })
        }
        bind.dPrivacy.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(android.R.id.content, PrivacyFragment()).commit()
        }
        bind.pChangeAvatar.setOnClickListener {
            viewModelClass.queryUserDetails().asLiveData().observe(this, Observer {
                if(it == null){
                    try{
                        val alertDialog = AlertDialog.Builder(this@SettingsFragment)
                        alertDialog.setTitle("Alert!")
                        alertDialog.setMessage("Please you have not registered with us or not logged in")
                        alertDialog.setCancelable(true)
                        alertDialog.setNegativeButton("Sign up"){_,_->
                            val intent = Intent(this@SettingsFragment, SignUpFragment::class.java)
                            startActivity(intent)
                        }
                        alertDialog.setPositiveButton("Log in"){_,_->
                            supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
                        }
                        alertDialog.show()
                    }catch (e:Exception){}
                }else{
                    try {
                        val intent = Intent(this@SettingsFragment, UpdateAvatarFragment::class.java)
                        startActivity(intent)
                    }catch (e:Exception){}
                }
            })
        }
        bind.dPolicy.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(android.R.id.content, PrivacyFragment()).addToBackStack(null).commit()
        }
        bind.dLogout.setOnClickListener {

            viewModelClass.deleteProfileDatatbase()
            /*val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)*/
            finishAffinity()
        }
        bind.deleteMyAccount.setOnClickListener {
            try{
                val alertDialog = AlertDialog.Builder(this@SettingsFragment)
                alertDialog.setTitle("Delete account!")
                alertDialog.setMessage("We are sorry you want to leave. Please email us and share your consent with us. Support will work on your account as requested. Send us email to structuredapps@yahoo.com")
                alertDialog.setCancelable(true)
                alertDialog.setNegativeButton("Cancel"){_,_->

                }
                alertDialog.show()
            }catch (e:Exception){}
        }
        bind.backk.setOnClickListener {
            supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).commit()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                supportFragmentManager.beginTransaction().replace(android.R.id.content, DashboardFragment()).addToBackStack(null).commit()
            }
            R.id.action_logout -> {
                val alertDialog = AlertDialog.Builder(this@SettingsFragment)
                alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to logout?")
                alertDialog.setNegativeButton("Cancel") { _, _ -> }
                alertDialog.setPositiveButton("Logout") { _, _ ->
                    viewModelClass.deleteProfileDatatbase()
                    /*val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)*/
                    this@SettingsFragment.finishAffinity()
                }
                alertDialog.show()
            }
            R.id.action_settings -> {
                val intent = Intent(this@SettingsFragment, SettingsFragment::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        //Todo
    }
}