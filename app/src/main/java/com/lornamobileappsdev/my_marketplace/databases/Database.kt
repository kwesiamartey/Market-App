package com.lornamobileappsdev.my_marketplace.databases

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lornamobileappsdev.my_marketplace.dao.Dao
import com.lornamobileappsdev.my_marketplace.entity.*

@androidx.room.Database(
    entities = [
        CountryListData::class,
        ChatData::class,
        FeaturedProductData::class,
        MessageData::class,
        SignUpDAta::class,
        SignInData::class,
        SendUsersMessageData::class,
        ProfileData::class,
        UserVerificationData::class,
        PostProductResponse::class,
        ProductData::class,
        SignInDataResponse::class,
        SignupResponses::class,
        ProductMySavedData::class,
        RelatedSearchData::class,
        RecentlyViewData::class
    ], exportSchema = false, version = 2
)
abstract class Database : RoomDatabase(){

    abstract fun getRepositoryDao() : Dao

    companion object{
        @Volatile
        var INSTANCE : Database? =null
        val LOCK = Any()

        fun invoke(context: Context) = INSTANCE
            ?: synchronized(LOCK) {
                INSTANCE
                    ?: roomBuilder(
                        context
                    ).also{
                        INSTANCE = it
                    }
            }

        fun roomBuilder(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            Database::class.java,
            "marketplace-tradeTower"
        )
            .build()
    }
}