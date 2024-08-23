package com.lornamobileappsdev.my_marketplace.repository


import androidx.lifecycle.LiveData
import com.lornamobileappsdev.my_marketplace.dao.Dao
import com.lornamobileappsdev.my_marketplace.entity.*
import kotlinx.coroutines.flow.Flow

class Repository(private val dao:Dao) {

    //val  queryUserDetails:Flow<List<SignupResponses>> = dao.queryUserDetails()

    /*val  queryRecentDashboardListOfRadioData:Flow<List<RecentRadioListData>> = dao.queryRecentDashboardListOfRadioData()
    val  queryTimerData:LiveData<List<TimerData>> = dao.queryTImerData() */

    suspend fun insertIntoProductDatatbaseRecentlyViewedItems(productData: RecentlyViewData){
        dao.insertIntoProductDatatbaseRecentlyViewedItems(productData)
    }

    fun queryRelatedSeachDatatbase(): Flow<List<RelatedSearchData>> = dao.queryRelatedSeachDatatbase()

    val queryListOfProductForMainDatabase: LiveData<List<PostProductResponse>> = dao.queryListOfProductForMainDatabase()

    fun queryListOfCountries(): Flow<List<CountryListData>> = dao.queryListOfCountries()

    fun queryUserDetails(): Flow<SignupResponses> = dao.queryUserDetails()

    fun queryListOfProduct() : Flow<List<PostProductResponse>> = dao.queryListOfProduct()

    fun queryStoredListOfroductData() : Flow<List<PostProductResponse>> = dao.queryStoredListOfroductData()

    fun queryMySavedStoredListOfroductData() : Flow<List<ProductMySavedData>> = dao.queryMySavedStoredListOfroductData()

    fun queryRecentlyViewdListOfroductData() : Flow<List<RecentlyViewData>> = dao.queryRecentlyViewdListOfroductData()

    fun queryCategoryListOfProduct(type:String) = dao.queryCategoryListOfProduct(type)

    fun getAUserPostProduct(status:Int) : Flow<List<PostProductResponse>> = dao.getAUserPostProduct(status)

    suspend fun insertIntoRelatedSeachDatatbase(relatedSearchData: RelatedSearchData){
        dao.insertIntoRelatedSeachDatatbase(relatedSearchData)
    }

    suspend fun insertIntoCountriesDatatbase(countryListData: CountryListData){
        dao.insertIntoCountriesDatatbase(countryListData)
    }
    suspend fun insertIntoProductDatatbase(productData: PostProductResponse){
        dao.insertIntoProductDatatbase(productData)
    }
    suspend fun insertIntoSavedProductDatatbase(productData: ProductData){
        dao.insertIntoSavedProductDatatbase(productData)
    }
    suspend fun insertIntoMySavedProductDatatbase(productMySavedData: ProductMySavedData){
        dao.insertIntoMySavedProductDatatbase(productMySavedData)
    }

    suspend fun storedUserDetails(signupResponses: SignupResponses){
        dao.storedUserDetails(signupResponses)
    }

    /* fun insertIntoTimer(timerData: TimerData){
        dao.insertIntoTimer(timerData)
    }
    suspend fun insertDataListCounts(dataListCounts: DataListCounts){
        dao.insertDataListCount(dataListCounts)
    } */
    suspend fun deleteCountryDatatbase(){
        dao.deleteCountryDatatbase()
    }
    suspend fun deleteproductDatatbase(id:Int){
        dao.deleteproductDatatbase(id)
    }
    suspend fun deleteMySavedProduct(id:Int){
        dao.deleteMySavedProduct(id)
    }
    suspend fun deleteRecentViewedProduct(id:Int){
        dao.deleteRecentViewedProduct(id)
    }
    suspend fun deleteMySavedProductAll(){
        dao.deleteMySavedProductAll()
    }

    suspend fun deleteRelatedSeaarchItem(id:Int){
        dao.deleteRelatedSeaarchItem(id)
    }
    suspend fun deleteRelatedSeaarchItemAll(){
        dao.deleteRelatedSeaarchItemAll()
    }
    suspend fun deleteAllproductDatatbase(){
        dao.deleteAllproductDatatbase()
    }
    suspend fun deleteProfileDatatbase(){
        dao.deleteProfileDatatbase()
    }

    suspend fun deleterecentlyviewdata(){
        dao.deleterecentlyviewdata()
    }

    /*fun deleteTImerDatatbase(){
        dao.deleteTImerDatatbase()
    }

    suspend fun deleteDataListCount(dataListCounts: DataListCounts){
        dao.deleteDataListCount(dataListCounts)
    }

    suspend fun insertIntoSingleDatatbase(favRadioListData: FavRadioListData){
        dao.insertIntoSingleDatatbase(favRadioListData)
    } */

    suspend fun updateProductList(productData: PostProductResponse){
        dao.updateProductList(productData)
    }

    suspend fun updateStoredUserDetauils(signupResponses: SignupResponses){
        dao.updateStoredUserDetauils(signupResponses)
    }

    suspend fun updateNumberViewsProductList(views_number:Int, id: Int){
        dao.updateNumberViewsProductList(views_number,id)
    }
    suspend fun updateRateOnProductList(rate:Int, id: Int){
        dao.updateRateOnProductList(rate,id)
    }
    /*suspend fun updatMainDataList(id:Int, name:String,location:String, url:String, added:String,
                                  playing:Boolean, web:String, logo:String,type:String)
    {
        dao.updatMainDataList(id,name,location,url,added,playing,web,logo,type)
    }
    suspend fun insertIntoRecentStattionDatatbase(recentRadioListData: RecentRadioListData){
        dao.insertIntoRecentStattionDatatbase(recentRadioListData)
    }
    suspend fun deleteIntoRecentStattionDatatbase(recentRadioListData: RecentRadioListData){
        dao.deleteIntoRecentStattionDatatbase(recentRadioListData)
    }
    suspend fun deleteIntoFavStattionDatatbase(favRadioListData: FavRadioListData){
        dao.deleteIntoFavStattionDatatbase(favRadioListData)
    }
    fun searchMainDatabase(searchQuery:String) :LiveData<List<MainRadioListData>>{
        return dao.searchMainDatabase(searchQuery)
    }*/
    fun searchProductDatabase(searchQuery:String) :LiveData<List<PostProductResponse>>{
        return dao.searchProductDatabase(searchQuery)
    }
    fun searchqueryMyStoredListOfroductData(searchQuery:String) :LiveData<List<PostProductResponse>>{
        return dao.searchqueryMyStoredListOfroductData(searchQuery)
    }
    fun searchqueryStoredListOfroductData(searchQuery:String) :LiveData<List<PostProductResponse>>{
        return dao.searchqueryStoredListOfroductData(searchQuery)
    }

    fun searchCategoryProductDatabase(cate:String, searchQuery:String) :LiveData<List<PostProductResponse>>{
        return dao.searchCategoryProductDatabase(cate,searchQuery)
    }

    fun searchUserProductDatabase(cate:String, searchQuery:String) :LiveData<List<PostProductResponse>>{
        return dao.searchUserProductDatabase(cate,searchQuery)
    }
    fun getSingleProductItemDatabase(id:String) : LiveData<List<PostProductResponse>> {
        return dao.getSingleProductItemDatabase(id)
    }

    fun getProductProductItemDatabase(id:String) : LiveData<List<PostProductResponse>> {
        return dao.getProductProductItemDatabase(id)
    }
   /* fun queryDataListCounts():List<DataListCounts> {
        val request = dao.queryDataListCounts()
        return request
    } */

   /* suspend fun getSuggestionRadioStation() : List<MainRadioListData> {
        val request=  Singleton.apiClient.getSuggestionRadioStation()
        if (request.isSuccessful){
            delay(3000L)
            return request.body()!!
        }
        return null!!
    }*/

   // suspend fun getLocationStation(): CountryListDataResponse = apiClient.ktorHttpClient.get<CountryListDataResponse>("http://172.17.100.2:8080/country_list")

}
