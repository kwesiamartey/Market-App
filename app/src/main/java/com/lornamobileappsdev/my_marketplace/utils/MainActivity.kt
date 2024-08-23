package com.example.imageproject

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.databinding.ActivityMainBinding
import com.lornamobileappsdev.my_marketplace.ui.NoSignalWifiFragment
import com.lornamobileappsdev.my_marketplace.ui.SplashScreenFragment
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val viewModel: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    private var appUpdate: AppUpdateManager? =null
    private val REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        appUpdate = AppUpdateManagerFactory.create(this)

        supportActionBar!!.hide()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        checkUpdate()


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


/*    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_logout -> {
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle("Logout")
                alertDialog.setMessage("Are you sure you want to logout?")
                alertDialog.setNegativeButton("Cancel"){_,_ ->

                }
                alertDialog.setPositiveButton("Logout"){_,_->
                    viewModel.deleteProfileDatatbase()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }*/

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResumeFragments() {
        super.onResumeFragments()
        inProgresscheckUpdate()
        Handler(Looper.myLooper()!!).postDelayed({
            try {
                if(viewModel.isNetworkAvailable(test_website) == true){
                    try {
                        Handler(Looper.myLooper()!!).postDelayed({
                            supportFragmentManager.beginTransaction().replace(android.R.id.content, SplashScreenFragment()).commit()
                        }, 3000L)
                    }catch (e:Exception){}
                }else{
                    try {
                        val intent = Intent(this@MainActivity, NoSignalWifiFragment::class.java)
                        startActivity(intent)

                    }catch (e:Exception){}
                }

            }catch (e: Exception){
                Log.i("TAG", e.message.toString())
            }
        }, 3000L)
    }


    fun checkUpdate(){
        appUpdate!!.appUpdateInfo.addOnSuccessListener {
            if(it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                appUpdate!!.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE,this, REQUEST_CODE)
            }
        }
    }

    fun inProgresscheckUpdate(){
        appUpdate!!.appUpdateInfo.addOnSuccessListener {
            if(it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
                appUpdate!!.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE,this, REQUEST_CODE)
            }
        }
    }
}