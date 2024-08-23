package com.lornamobileappsdev.my_marketplace.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.databases.Database
import com.lornamobileappsdev.my_marketplace.entity.PostProductResponse
import com.lornamobileappsdev.my_marketplace.entity.RecentlyViewData
import com.lornamobileappsdev.my_marketplace.repository.Repository
import com.lornamobileappsdev.my_marketplace.singletons.KtorClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class PullingDataFromServerViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>().applicationContext

    val post_product_list: MutableLiveData<List<com.lornamobileappsdev.my_marketplace.responses.PostProductResponse>> by lazy {
        MutableLiveData<List<com.lornamobileappsdev.my_marketplace.responses.PostProductResponse>>()
    }

    val cloudDatabaseSize: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val comparasionOfDatabase: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val post_product_id: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val localListSize: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val decision: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val uploadProductText: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val globalText: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val repository: Repository

    init {
        val dao = Database.invoke(application).getRepositoryDao()
        repository = Repository(dao)
    }


    fun queryRecentlyViewdListOfroductData(): Flow<List<RecentlyViewData>> {
        return repository.queryRecentlyViewdListOfroductData()
    }

    suspend fun insertIntoProductDatatbaseRecentlyViewedItems(productData: RecentlyViewData) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIntoProductDatatbaseRecentlyViewedItems(productData)
        }

    suspend fun updateProductList(productData: PostProductResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProductList(productData)
        }

    fun deleteAllproductDatatbase() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllproductDatatbase()
    }

    suspend fun deleteRecentViewedProduct(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRecentViewedProduct(id)
    }

    fun queryAllListOfroductData(): Flow<List<PostProductResponse>> {
        return repository.queryListOfProduct()
    }

    suspend fun insertIntoProductDatatbase(productData: PostProductResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIntoProductDatatbase(productData)
        }

    suspend fun gettingProductFromMainProducts(): List<PostProductResponse> {
          delay(1000L)
          try {
              val client = KtorClient.ktorHttpClient
              client.use {
                  val response = it.get("${BuildConfig.API_URL}allproducts")
                  val res: String = response.body()
                  val ress: List<PostProductResponse> = response.body()
                  return Json.decodeFromString(res)
                  Log.i("viewModle_ktor_client_data", ress.toString())
              }
          } catch (e: Exception) {
          }

          return emptyList()
      }


    /*fun MyViewModelWorkee() = viewModelScope.launch(Dispatchers.IO) {
        // Create constraints for the Worker
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresCharging(true)
            .build()

        // Create a Data object to pass data to the Worker
        val data = Data.Builder()

        // Get the cloudDatabaseSize value if it's not null and add it to the data
        cloudDatabaseSize?.value?.let { cl ->
            data.putInt("cloudDatabaseSize", cl)
        }

        // Perform the query to get the local database size and add it to the data
        val localDataBaseSize = queryAllListOfroductData().onStart {  }.single().size
        data.putInt("localdataBaseSize", localDataBaseSize)
        Log.i("viewmodel_retrofit_client_data", "localdataBaseSize $localDataBaseSize")

        // Create the PeriodicWorkRequest and set the constraints and input data
        val workerPeriodicWorkRequest = PeriodicWorkRequest.Builder(
            MyWorker::class.java,
            15,
            TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setInputData(data.build())
            .addTag("my_id")


        // Enqueue the work request with unique periodic work policy
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "my_id",
            ExistingPeriodicWorkPolicy.KEEP,
            workerPeriodicWorkRequest.build()
        )
    }*/


}