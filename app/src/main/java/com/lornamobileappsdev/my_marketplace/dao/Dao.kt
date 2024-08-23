package com.lornamobileappsdev.my_marketplace.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

import androidx.room.Dao
import com.lornamobileappsdev.my_marketplace.entity.*


@Dao
interface Dao {

    @Query("SELECT * FROM relatedsearchdata  ORDER BY id DESC")
    fun queryRelatedSeachDatatbase(): Flow<List<RelatedSearchData>>

    @Query("SELECT * FROM countrylistdata  ORDER BY id DESC")
    fun queryListOfCountries(): Flow<List<CountryListData>>

    @Query("SELECT * FROM postProductResponse ORDER BY id DESC")
    fun queryListOfProduct(): Flow<List<PostProductResponse>>

    @Query("SELECT * FROM postProductResponse ORDER BY id DESC LIMIT 200")
    fun queryListOfProductForMainDatabase(): LiveData<List<PostProductResponse>>

    @Query("SELECT * FROM postProductResponse  ORDER BY id DESC")
    fun queryStoredListOfroductData(): Flow<List<PostProductResponse>>

    @Query("SELECT * FROM productmysaveddata ORDER BY id DESC")
    fun queryMySavedStoredListOfroductData(): Flow<List<ProductMySavedData>>

    @Query("SELECT * FROM recentlyviewdata ORDER BY id ASC")
    fun queryRecentlyViewdListOfroductData(): Flow<List<RecentlyViewData>>

    @Query("SELECT * FROM postProductResponse WHERE category LIKE :type ORDER BY id DESC")
    fun queryCategoryListOfProduct(type: String): Flow<List<PostProductResponse>>

    @Query("SELECT * FROM postProductResponse WHERE userId IS :user_id ORDER BY id DESC")
    fun getAUserPostProduct(user_id: Int): Flow<List<PostProductResponse>>

    @Query("SELECT * FROM signupresponses")
    fun queryUserDetails(): Flow<SignupResponses>

    /* @Query("SELECT * FROM timerdata")
     fun queryTImerData(): LiveData<List<TimerData>>*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoProductDatatbaseRecentlyViewedItems(productData: RecentlyViewData)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoRelatedSeachDatatbase(relatedSearchData: RelatedSearchData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoCountriesDatatbase(countryListData: CountryListData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoProductDatatbase(productData: PostProductResponse)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoSavedProductDatatbase(productData: ProductData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoMySavedProductDatatbase(productMySavedData: ProductMySavedData)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun storedUserDetails(signupResponses: SignupResponses)

    /* @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertDataListCount(dataListCounts: DataListCounts) */

    @Query("DELETE FROM postProductResponse WHERE id IS :id")
    suspend fun deleteproductDatatbase(id: Int)

    @Query("DELETE FROM relatedsearchdata WHERE id IS :id")
    suspend fun deleteRelatedSeaarchItem(id: Int)

    @Query("DELETE FROM recentlyviewdata")
    suspend fun deleterecentlyviewdata()

    @Query("DELETE FROM relatedsearchdata")
    suspend fun deleteRelatedSeaarchItemAll()
    @Query("DELETE FROM productmysaveddata WHERE id IS :id ")
    suspend fun deleteMySavedProduct(id: Int)

    @Query("DELETE FROM recentlyviewdata WHERE id IS :id ")
    suspend fun deleteRecentViewedProduct(id: Int)

    @Query("DELETE FROM countrylistdata")
    suspend fun deleteMySavedProductAll()

    @Query("DELETE FROM postProductResponse")
    suspend fun deleteAllproductDatatbase()

    @Query("DELETE FROM signupresponses")
    suspend fun deleteProfileDatatbase()

    @Query("DELETE FROM countrylistdata")
    suspend fun deleteCountryDatatbase()

    /* @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertIntoSingleDatatbase(favRadioListData: FavRadioListData) */

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateProductList(productData: PostProductResponse)

    @Query("UPDATE postProductResponse SET views = :views_number WHERE id = :id")
    suspend fun updateNumberViewsProductList(views_number: Int, id: Int)

    @Query("UPDATE postProductResponse SET rates = :rates WHERE id = :id")
    suspend fun updateRateOnProductList(rates: Int, id: Int)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateStoredUserDetauils(signupResponses: SignupResponses)

    /*@Query("UPDATE mainradiolistdata SET name = :name, location= :location, url= :url, added = :added, playing = :playing, web = :web, logo = :logo, type =:type WHERE id== :id ")
    suspend fun updatMainDataList(
        id: Int, name: String,
        location: String, url: String, added: String,
        playing: Boolean, web: String, logo: String, type: String
    )

    @Query("SELECT * FROM datalistcounts ORDER BY id DESC")
    fun queryDataListCounts(): List<DataListCounts>

    @Query("SELECT * FROM recentradiolistdata ORDER BY id=0 DESC")
    fun queryRecentListOfRadioData(): LiveData<List<RecentRadioListData>>

    @Query("SELECT * FROM recentradiolistdata ORDER BY id=0 DESC LIMIT 4")
    fun queryRecentDashboardListOfRadioData(): Flow<List<RecentRadioListData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntoRecentStattionDatatbase(recentRadioListData: RecentRadioListData)

    @Delete
    suspend fun deleteIntoRecentStattionDatatbase(recentRadioListData: RecentRadioListData)

    @Delete
    suspend fun deleteIntoFavStattionDatatbase(favRadioListData: FavRadioListData)

    @Query("DELETE FROM timerdata")
    fun deleteTImerDatatbase()

    @Delete
    suspend fun deleteDataListCount(dataListCounts: DataListCounts)*/

    @Query("SELECT * FROM postProductResponse WHERE title LIKE :searcgQuery OR desc LIKE :searcgQuery ORDER BY id=0 DESC")
    fun searchProductDatabase(searcgQuery: String): LiveData<List<PostProductResponse>>

    /*@Query("SELECT * FROM productdata WHERE title LIKE :searcgQuery ORDER BY id DESC")
    fun searchProductDatabase(searcgQuery: String): LiveData<List<PostProductResponse>>*/

    @Query("SELECT * FROM postProductResponse WHERE title LIKE :searcgQuery ORDER BY id DESC")
    fun searchqueryMyStoredListOfroductData(searcgQuery: String): LiveData<List<PostProductResponse>>

    @Query("SELECT * FROM postProductResponse WHERE title LIKE :searcgQuery ORDER BY id DESC")
    fun searchqueryStoredListOfroductData(searcgQuery: String): LiveData<List<PostProductResponse>>

    @Query("SELECT * FROM postProductResponse WHERE category IS :cate And title LIKE :searcgQuery ORDER BY id DESC")
    fun searchCategoryProductDatabase(
        cate: String,
        searcgQuery: String
    ): LiveData<List<PostProductResponse>>

    @Query("SELECT * FROM postProductResponse WHERE userid IS :userId And title LIKE :searcgQuery OR desc LIKE :searcgQuery  ORDER BY id DESC")
    fun searchUserProductDatabase(
        userId: String,
        searcgQuery: String
    ): LiveData<List<PostProductResponse>>

    @Query("SELECT * FROM postProductResponse WHERE id IS :id ")
    fun getSingleProductItemDatabase(id: String): LiveData<List<PostProductResponse>>

    @Query("SELECT * FROM postProductResponse WHERE userId LIKE :id ")
    fun getProductProductItemDatabase(id: String): LiveData<List<PostProductResponse>>

}