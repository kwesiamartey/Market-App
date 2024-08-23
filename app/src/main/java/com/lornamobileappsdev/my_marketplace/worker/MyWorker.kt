package com.lornamobileappsdev.my_marketplace.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class MyWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {


    override suspend fun doWork(): Result {

     /*   val localdataBaseSize = inputData.getInt("localdataBaseSize",0)
        val cloudDatabaseSize = inputData.getInt("cloudDatabaseSize",0)

     var id = inputData.getInt("id", 0)
        var userId = inputData.getInt("userId", 0)
        var user_name = inputData.getString("userId")
        var title = inputData.getString("title")
        var currenxy = inputData.getString("currenxy")
        var price = inputData.getString("price")
        var category = inputData.getString("category")
        var country = inputData.getString("country")
        var location = inputData.getString("location")
        var desc = inputData.getString("desc")
        var dateAndtimme = inputData.getString("dateAndtimme")
        var type = inputData.getString("type")
        var negotiation = inputData.getString("negotiation")
        var image = inputData.getString("image")
        var image_two = inputData.getString("image_two")
        var image_three = inputData.getString("image_three")
        var image_four = inputData.getString("image_four")
        var image_five = inputData.getString("image_five")
        var image_six = inputData.getString("image_six")
        val paid_product = inputData.getString("paid_product")
        val pst_phone_number = inputData.getString("pst_phone_number")
        val subscription_type = inputData.getString("subscription_type")
        val subscription_date_start = inputData.getString("subscription_date_start")
        val subscription_date_due = inputData.getString("subscription_date_due")
        val views = inputData.getInt("localdataBaseSize", 0)
        val approval_status = inputData.getString("approval_status")
        val rates = inputData.getInt("localdataBaseSize", 0)



        if (localdataBaseSize != cloudDatabaseSize) {
         *//*   val postProductResponse = PostProductResponse(
                id,
                userId,
                user_name,
                title,
                currenxy,
                price,
                category,
                country,
                location,
                desc,
                dateAndtimme,
                type,
                negotiation,
                image,
                image_two,
                image_three,
                image_four,
                image_five,
                image_six,
                paid_product,
                pst_phone_number,
                subscription_type,
                subscription_date_start,
                subscription_date_due,
                views,
                approval_status,
                rates
            )*//*

            Log.i("work_log", "not same ${localdataBaseSize} === ${cloudDatabaseSize}")
           // Database.INSTANCE!!.getRepositoryDao().insertIntoProductDatatbase(postProductResponse)

        } else {
            Log.i("work_log", "same ${localdataBaseSize} === ${cloudDatabaseSize}")
        }
*/
        //gettingProductFromMainProducts()


        return Result.success()
    }


}