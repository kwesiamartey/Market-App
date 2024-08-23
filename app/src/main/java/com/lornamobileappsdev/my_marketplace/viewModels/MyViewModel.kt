package com.lornamobileappsdev.my_marketplace.viewModels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.GsonBuilder
import com.lornamobileappsdev.my_marketplace.BuildConfig
import com.lornamobileappsdev.my_marketplace.api.Api
import com.lornamobileappsdev.my_marketplace.constant.test_website
import com.lornamobileappsdev.my_marketplace.constant.token
import com.lornamobileappsdev.my_marketplace.constant.url_main
import com.lornamobileappsdev.my_marketplace.databases.Database
import com.lornamobileappsdev.my_marketplace.entity.*
import com.lornamobileappsdev.my_marketplace.models.CategoryList
import com.lornamobileappsdev.my_marketplace.models.ListOfCountries
import com.lornamobileappsdev.my_marketplace.models.OtherDataForViewModel
import com.lornamobileappsdev.my_marketplace.repository.ApiClient
import com.lornamobileappsdev.my_marketplace.repository.Repository
import com.lornamobileappsdev.my_marketplace.responses.CountryListDataResponse
import com.lornamobileappsdev.my_marketplace.singletons.KtorClient
import com.lornamobileappsdev.my_marketplace.singletons.Singleton
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Collections
import java.util.concurrent.TimeUnit


class MyViewModel(application: Application) : AndroidViewModel(application) {

    var channel:ReceiveChannel<PostProductResponse> = Channel()


    val products:MutableLiveData<List<com.lornamobileappsdev.my_marketplace.responses.PostProductResponse>> by lazy {
        MutableLiveData<List<com.lornamobileappsdev.my_marketplace.responses.PostProductResponse>>()
    }

    var recyclerViewState: Parcelable? = null

    val gridView: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>(0)
    }

    val categoryName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private val context = getApplication<Application>().applicationContext

    val _queryRadioListData: MutableLiveData<List<CountryListDataResponse>> by lazy {
        MutableLiveData<List<CountryListDataResponse>>()
    }

    val _otherDataForViewModel = MutableLiveData<OtherDataForViewModel>()
    val otherDataForViewModel: LiveData<OtherDataForViewModel>
        get() = _otherDataForViewModel

    val _singleItemSelectedData = MutableLiveData<List<PostProductResponse>>()

    val singleItemSelectedData: LiveData<List<PostProductResponse>>
        get() = _singleItemSelectedData

    val completeLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>(false)
    }

    val counts: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val ratings: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val Itemsratings: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val product_item_user_id: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val relateSearchText: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val OtherUser_price: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val OtherUser_productname: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val Otheruser_name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val OtherUser_avatar: MutableLiveData<ByteArray> by lazy {
        MutableLiveData<ByteArray>()
    }

    val other_phone_number: MutableLiveData<ByteArray> by lazy {
        MutableLiveData<ByteArray>()
    }

    val selectedCountryName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    var queryUserDetails: Flow<List<SignupResponses>> =
        MutableStateFlow<List<SignupResponses>>(emptyList())

    val country: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val negotiation: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val paid_product: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val postuserId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val user_name: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val phone_number: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val views: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val chatId: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val userIdChat: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val otherIdChat: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val productId: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val id: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val title: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val currenxy: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val price: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val link: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val category: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val location: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val desc: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val dateAndtimme: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val type: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val image: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val image_two: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val image_three: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val image_four: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val image_five: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val image_six: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val subscription_type: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val subscription_date_start: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val subscription_date_due: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val approval_status: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val pst_phone_number: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val allProduct: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    val towns: MutableLiveData<List<ListOfCountries>> by lazy {
        MutableLiveData<List<ListOfCountries>>()
    }

    val countriesNames: MutableLiveData<List<ListOfCountries>> by lazy {
        MutableLiveData<List<ListOfCountries>>()
    }

    val catesListing: MutableLiveData<MutableList<CategoryList>> by lazy {
        MutableLiveData<MutableList<CategoryList>>()
    }


    val repository: Repository

    init {
        val dao = Database.invoke(application).getRepositoryDao()
        repository = Repository(dao)
        isNetworkAvailables()
        isNetworkAvailable(test_website)

        viewModelScope.launch(Dispatchers.IO) {
            delay(2000L)
            //getProductsItemsMain()
            pullDataFromServer()
            getlistofTown()
            getCountriesNameList()
            getCateListProduct()
        }
    }

    val _postProductResponse: LiveData<List<PostProductResponse>> = repository.queryListOfProductForMainDatabase

    fun queryRelatedSeachDatatbase(): Flow<List<RelatedSearchData>> {
        return repository.queryRelatedSeachDatatbase()
    }

    fun queryListOfCountries(): Flow<List<CountryListData>> {
        return repository.queryListOfCountries()
    }

    fun queryUserDetails(): Flow<SignupResponses> {
        return repository.queryUserDetails()
    }

    fun queryAllListOfroductData(): Flow<List<PostProductResponse>> {
        return repository.queryListOfProduct()
    }

    fun queryStoredListOfroductData(): Flow<List<PostProductResponse>> {
        return repository.queryStoredListOfroductData()
    }

    fun queryMySavedStoredListOfroductData(): Flow<List<ProductMySavedData>> {
        return repository.queryMySavedStoredListOfroductData()
    }

    fun queryCategoryListOfProduct(type: String): Flow<List<PostProductResponse>> {
        return repository.queryCategoryListOfProduct(type)
    }

    fun getAUserPostProduct(status: Int): Flow<List<PostProductResponse>> {
        return repository.getAUserPostProduct(status)
    }

    suspend fun insertIntoProductDatatbase(productData: PostProductResponse) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIntoProductDatatbase(productData)
        }

    suspend fun insertIntoSavedProductDatatbase(productData: ProductData) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIntoSavedProductDatatbase(productData)
        }

    suspend fun insertIntoRelatedSeachDatatbase(relatedSearchData: RelatedSearchData) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIntoRelatedSeachDatatbase(relatedSearchData)
        }

    suspend fun insertIntoCountriesDatatbase(countryListData: CountryListData) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIntoCountriesDatatbase(countryListData)
        }

    suspend fun insertIntoMySavedProductDatatbase(productMySavedData: ProductMySavedData) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertIntoMySavedProductDatatbase(productMySavedData)
        }

    suspend fun storedUserDetails(signupResponses: SignupResponses) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.storedUserDetails(signupResponses)
        }

    fun getSingleProductItemDatabase(searcgQuery: String): LiveData<List<PostProductResponse>> {
        return repository.getSingleProductItemDatabase(searcgQuery)
    }

    fun getProductProductItemDatabase(searcgQuery: String): LiveData<List<PostProductResponse>> {
        return repository.getProductProductItemDatabase(searcgQuery)
    }

    fun searchProductDatabase(searcgQuery: String): LiveData<List<PostProductResponse>> {
        return repository.searchProductDatabase(searcgQuery)
    }

    fun searchqueryMyStoredListOfroductData(
        searcgQuery: String
    ): LiveData<List<PostProductResponse>> {
        return repository.searchqueryMyStoredListOfroductData(searcgQuery)
    }

    fun searchCategoryProductDatabase(
        cate: String,
        searcgQuery: String
    ): LiveData<List<PostProductResponse>> {
        return repository.searchCategoryProductDatabase(cate, searcgQuery)
    }

    fun searchUserProductDatabase(
        cate: String,
        searcgQuery: String
    ): LiveData<List<PostProductResponse>> {
        return repository.searchUserProductDatabase(cate, searcgQuery)
    }

    fun isNetworkAvailables(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                } else {
                    TODO("VERSION.SDK_INT < M")
                }
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }


    fun isNetworkAvailable(websiteUrl:String): Boolean {
        if (isNetworkAvailables()) {
            try {

                return true

            } catch (e: Exception) {
                // Handle any exceptions that occur (e.g., MalformedURLException, IOException)
                return false
            }
        } else {
            Log.d("TAG", "No network available!")

            return false
        }
        return false
    }

    suspend fun deleteproductDatatbase(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteproductDatatbase(id)
    }

    fun deleteMySavedProductAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMySavedProductAll()
    }

    fun deleterecentlyviewdata() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleterecentlyviewdata()
    }

    fun deleteRelatedSeaarchItemAll() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRelatedSeaarchItemAll()
    }

    suspend fun deleteMySavedProduct(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteMySavedProduct(id)
    }

    suspend fun deleteRelatedSeaarchItem(id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteRelatedSeaarchItem(id)
    }

    fun deleteAllproductDatatbase() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAllproductDatatbase()
    }

    fun deleteProfileDatatbase() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteProfileDatatbase()
    }

    fun deleteCountryDatatbase() = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCountryDatatbase()
    }

    suspend fun updateProductList(productData: PostProductResponse) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateProductList(productData)
    }

    fun updateNumberViewsProductList(views_number: Int, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNumberViewsProductList(views_number, id)
    }

    fun updateStoredUserDetauils(signupResponses: SignupResponses) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateStoredUserDetauils(signupResponses)
    }


    suspend fun getUsers(userId: Int): com.lornamobileappsdev.my_marketplace.responses.SignupResponses? {
        delay(1000L)
        try {
            val client = KtorClient.ktorHttpClient
            client.use {
                val response = it.get("${BuildConfig.API_URL}find_user/{$userId}")
                val res: String = response.body()
                return Json.decodeFromString(res)
            }
        }catch (e:Exception){}

        return null
    }


    fun updateRateOnProductList(rate: Int, id: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateRateOnProductList(rate, id)
    }
    suspend fun updateProductRatingsTOServer(user_id:Int, product_id:Int, rates:Int, date_and_time:String)
            = viewModelScope.launch(Dispatchers.IO) {
        delay(100L)
        try {
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addInterceptor { chain ->
                    val request: Request =
                        chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer ${token}")
                            .addHeader("Content-Type", "application/text")
                            .build()
                    chain.proceed(request)
                }
                .build()

            val retrofit: Retrofit.Builder by lazy {
                Retrofit.Builder().baseUrl(BuildConfig.API_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
            }
            val retrofitApi: Api by lazy {
                retrofit.client(okHttpClient)
                retrofit.build().create(Api::class.java)
            }
            val apiClient = ApiClient(retrofitApi)
            apiClient.api.updateRatingsToServer(user_id, product_id, rates, date_and_time).enqueue(object :
                Callback<Boolean> {
                override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                    if (response.isSuccessful) {
                        if (response.code() == 200) {
                            if(response.body()!!){
                                Log.i("rateding", response.body()!!.toString())
                                Log.i("rated_product", "${Itemsratings.value!!} + ${rates}")
                                val sum = Itemsratings.value!! + rates
                                Itemsratings.postValue(sum)
                                Log.i("rated_product", "${sum}${product_id}${user_id}")
                                updateRateOnProductList(sum, product_id)
                            }
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


    suspend fun pullDataFromServer()  = viewModelScope.launch(Dispatchers.IO){
        delay(2000L)

            try {
                val client = Singleton.Singleton.apiClient.api.getAllProductlistthisIsForUpdateWhenSwipeRefresh()
                if (client.isSuccessful) {
                    if (client.code() == 200) {
                        products.postValue(client.body())

                    } else {
                        Log.i("retrofit", client.body()!!.toString())
                    }
                } else {
                    Log.i("retrofit", client.body()!!.toString())
                }
                completeLoading.postValue(true)
            } catch (e: Exception) { }


    }
   suspend fun getlistofTown() = viewModelScope.launch(Dispatchers.IO){
    delay(100L)
        try {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addInterceptor { chain ->
                    val request: Request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $token")
                            .addHeader("Content-Type", "application/json")
                            .build()
                    chain.proceed(request)
                }
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()

            val retrofit: Retrofit.Builder by lazy {
                Retrofit.Builder().baseUrl(url_main)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create()) //important
                    .addConverterFactory(GsonConverterFactory.create(gson))
            }
            val retrofitApi: Api by lazy {
                retrofit.client(okHttpClient)
                retrofit.build().create(Api::class.java)
            }

            val rees = retrofitApi.get_town_list().body()
            if (rees != null) {
                if (rees.isNotEmpty()){
                    towns.postValue(rees)
                    Log.i("town_list", rees.toString())
                }
            }
        }catch (e:Exception){ }
    }

    fun getCateListProduct() = viewModelScope.launch(Dispatchers.IO){

        try {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addInterceptor { chain ->
                    val request: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .addHeader("Content-Type", "application/json")
                        .build()
                    chain.proceed(request)
                }
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()

            val retrofit: Retrofit.Builder by lazy {
                Retrofit.Builder().baseUrl(url_main)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create()) //important
                    .addConverterFactory(GsonConverterFactory.create(gson))
            }
            val retrofitApi: Api by lazy {
                retrofit.client(okHttpClient)
                retrofit.build().create(Api::class.java)
            }

            val rees = retrofitApi.get_cate_list().body()
            if (rees != null) {
                if (rees.isNotEmpty()){
                    catesListing.postValue(rees)
                    Log.i("catelist_list", rees.toString())
                }
            }
        }catch (e:Exception){ }
    }

    fun getCountriesNameList() = viewModelScope.launch(Dispatchers.IO){

        try {
            val gson = GsonBuilder()
                .setLenient()
                .create()
            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .writeTimeout(3, TimeUnit.MINUTES)
                .addInterceptor { chain ->
                    val request: Request = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $token")
                        .addHeader("Content-Type", "application/json")
                        .build()
                    chain.proceed(request)
                }
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()

            val retrofit: Retrofit.Builder by lazy {
                Retrofit.Builder().baseUrl(url_main)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create()) //important
                    .addConverterFactory(GsonConverterFactory.create(gson))
            }
            val retrofitApi: Api by lazy {
                retrofit.client(okHttpClient)
                retrofit.build().create(Api::class.java)
            }
            val rees = retrofitApi.get_countries_list().body()
            if (rees != null) {
                if (rees.isNotEmpty()){
                    countriesNames.postValue(rees)
                    Log.i("countryNameslist_list", rees.toString())
                }
            }
        }catch (e:Exception){ }
    }
}