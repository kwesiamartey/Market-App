package com.lornamobileappsdev.my_marketplace

import android.R
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.google.android.gms.ads.MobileAds
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.firestore.FirebaseFirestore
import com.lornamobileappsdev.my_marketplace.databinding.ActivityMainBinding
import com.lornamobileappsdev.my_marketplace.internet_connectivity.IsInternetConnectedWithData
import com.lornamobileappsdev.my_marketplace.ui.*
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import com.lornamobileappsdev.my_marketplace.worker.MyWorker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val viewModel: MyViewModel by lazy {
        ViewModelProvider(this).get(MyViewModel::class.java)
    }

    val viewModelTwo: PullingDataFromServerViewModel by lazy {
        ViewModelProvider(this).get(PullingDataFromServerViewModel::class.java)
    }

    private var appUpdate: AppUpdateManager? =null
    private val REQUEST_CODE = 100

    val IsInternetConnectedWithData = IsInternetConnectedWithData()

    lateinit var firestore : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // viewModelTwo.MyViewModelWorkee()

        appUpdate = AppUpdateManagerFactory.create(this)

        supportActionBar!!.hide()
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        MobileAds.initialize(this) {}

        val window: Window = this.window

        // clear FLAG_TRANSLUCENT_STATUS flag:
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        // finally change the color

        // finally change the color
        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        setContentView(binding.root)

        //checkUpdate()

        GlobalScope.launch(Dispatchers.IO) {
            val isConnectedWithData =  IsInternetConnectedWithData.isInternetConnectedWithData(this@MainActivity)
            runOnUiThread {
                if (isConnectedWithData) {
                    // Internet is connected and there is data access
                    try {
                        supportFragmentManager.beginTransaction().replace(R.id.content, SplashScreenFragment()).commit()
                    }catch (e:Exception){}
                    Log.i("internet_connectivity", "internet_connectivity " +isConnectedWithData.toString())
                } else {
                    Log.i("internet_connectivity", "internet_connectivity " +isConnectedWithData.toString())
                    // Internet is connected, but there is no data access
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.content, NoSignalWifiFragment()).addToBackStack(null).commit()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inProgresscheckUpdate()
    }

    fun startWorkManagerFunc(){
        val constraints:Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this@MainActivity).enqueue(workRequest )
    }

    fun startWorkManagerConinuesFunc(){
        val constraints:Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        val workerPeriodicWorkRequest = PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .addTag("my_id")
            .build()

        WorkManager.getInstance(this@MainActivity).enqueueUniquePeriodicWork("my_id",ExistingPeriodicWorkPolicy.KEEP, workerPeriodicWorkRequest )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        //Todo
    }

    fun checkUpdate() {
        appUpdate?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
            ) {
                startUpdateFlow(appUpdateInfo)
            }
        }
    }

    fun inProgresscheckUpdate() {
        appUpdate?.appUpdateInfo?.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo)
            }
        }
    }

    private fun startUpdateFlow(appUpdateInfo: AppUpdateInfo) {
        appUpdate?.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE, this, REQUEST_CODE)
    }

    private fun canAccessServer(urlString: String): Boolean {
        var connection: HttpURLConnection? = null
        return try {
            val url = URL(urlString)
            connection = url.openConnection() as HttpURLConnection
            connection.connectTimeout = 1500 // Set connection timeout
            connection.connect()
            connection.responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            false
        } finally {
            connection?.disconnect()
        }
    }
}