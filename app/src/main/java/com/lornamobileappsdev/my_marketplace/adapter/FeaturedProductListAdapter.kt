package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.url_for_uploading
import com.lornamobileappsdev.my_marketplace.responses.PostProductResponse
import com.lornamobileappsdev.my_marketplace.entity.RecentlyViewData
import com.lornamobileappsdev.my_marketplace.ui.Single_Selected_item_show
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import java.util.zip.GZIPInputStream


class FeaturedProductListAdapter(
    val context: Context,
    val productDataList: List<PostProductResponse>,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: MyViewModel,
    val viewModeltwo: PullingDataFromServerViewModel
) : RecyclerView.Adapter<FeaturedProductListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(productDataList: PostProductResponse, position: Int) {
            itemView.findViewById<TextView>(R.id.pr_title_txt).text = productDataList.title
            itemView.findViewById<TextView>(R.id.pr_price_currency_txt).text = "${productDataList.currenxy}"
            itemView.findViewById<TextView>(R.id.pr_price_txt).text = "${productDataList.price!!}"
            itemView.findViewById<TextView>(R.id.pr_pruct_description).text = productDataList.desc
            itemView.findViewById<TextView>(R.id.pr_type).text = productDataList.type
            itemView.findViewById<TextView>(R.id.store_name).text = productDataList.user_name
            itemView.findViewById<TextView>(R.id.pr_product_location).text = "from " + productDataList.location +" in "+productDataList.paid_product
            itemView.findViewById<TextView>(R.id.pr_views).text = formatViewsCount(productDataList.views!!.toLong())
            //itemView.findViewById<ImageView>(R.id.pr_imgg).setImageBitmap(productDataList.image!!)

            if(productDataList.rates == 0){
                itemView.findViewById<LinearLayout>(R.id.rating_wrapper).visibility = View.GONE
            }else{
                itemView.findViewById<LinearLayout>(R.id.rating_wrapper).visibility = View.INVISIBLE
                itemView.findViewById<TextView>(R.id.ratings_value).text = calculateDoubleRating(
                    productDataList.rates!!).toString()
            }

            if(productDataList.negotiation == "1"){
                itemView.findViewById<TextView>(R.id.negotiate_text).visibility = View.VISIBLE
            }else{
                itemView.findViewById<TextView>(R.id.negotiate_text).visibility = View.INVISIBLE
            }

            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Log.i("imgLocation", "lovicmarketplace.herokuapp.com" + productDataList.image!!)


            //loadByteArrayIntoImageView(productDataList.image!!, itemView.findViewById<ImageView?>(R.id.pr_imgg))

            Glide.with(context)
                .load(url_for_uploading+productDataList.image!!)
                .transform(CenterInside(), RoundedCorners(10))
                .apply(requestOptions)
                .placeholder(circularProgressDrawable)
                .dontAnimate()
                .into(itemView.findViewById(R.id.pr_imgg))

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.category_product_item_list, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = productDataList[position]
        holder.setData(hold, position)

        holder.itemView.findViewById<LinearLayout>(R.id.post_detail_wrapper).setOnClickListener {
            var count: Int = hold.views!!
            count + 1
            //store data in the view model

            /* viewModel.id.value = ""
             viewModel.counts.value = 0
             viewModel.productId.value = ""
             viewModel.postuserId.value = 0
             viewModel.user_name.value = ""
             viewModel.title.value = ""
             viewModel.price.value = ""
             viewModel.category.value = ""
             viewModel.location.value = ""
             viewModel.country.value = ""
             viewModel.negotiation.value = ""
             viewModel.paid_product.value = ""
             viewModel.desc.value = ""
             viewModel.dateAndtimme.value = ""
             viewModel.type.value = ""
             viewModel.image.value = null
             viewModel.image_two.value = null
             viewModel.image_three.value = null
             viewModel.image_four.value = null
             viewModel.image_five.value = null
             viewModel.image_six.value = null
             viewModel.subscription_type.value = ""
             viewModel.subscription_date_due.value = ""
             viewModel.approval_status.value = ""
             viewModel.phone_number.value = ""
             viewModel.views.value = ""*/

            try {
                //store data in the view model

                viewModel.id.value = hold.id.toString()
                viewModel.counts.value = count
                viewModel.productId.value = hold.id.toString()
                viewModel.postuserId.value = hold.userId
                viewModel.user_name.value = hold.user_name
                viewModel.title.value = hold.title
                viewModel.price.value = hold.price.toString()
                viewModel.category.value = hold.category
                viewModel.currenxy.value = hold.currenxy.toString()
                viewModel.location.value = hold.location
                viewModel.country.value = hold.country
                viewModel.negotiation.value = hold.negotiation
                viewModel.paid_product.value = hold.paid_product
                viewModel.desc.value = hold.desc
                viewModel.dateAndtimme.value = hold.dateAndtimme
                viewModel.type.value = hold.type
                viewModel.image.value = hold.image
                viewModel.image_two.value = hold.image_two
                viewModel.image_three.value = hold.image_three
                viewModel.image_four.value = hold.image_four
                viewModel.image_five.value = hold.image_five
                viewModel.image_six.value = hold.image_six
                viewModel.subscription_type.value = hold.subscription_type
                viewModel.subscription_date_due.value = hold.subscription_date_due
                viewModel.approval_status.value = hold.approval_status
                viewModel.phone_number.value = hold.pst_phone_number
                viewModel.views.value = hold.views.toString()
                viewModel.Itemsratings.value = hold.rates
                viewModel.ratings.value = 0
                viewModel.updateNumberViewsProductList(count, hold.id!!)
                /*val intent = Intent(context, Single_Selected_item_show::class.java)
                context.startActivity(intent)*/


                viewModel.queryUserDetails().asLiveData().observe(lifecycleOwner, androidx.lifecycle.Observer {
                    if (it == null) {

                        //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                    } else {

                        try {
                            val products = RecentlyViewData(
                                id = hold.id,
                                userId = hold.userId,
                                user_name = hold.user_name,
                                title = hold.title,
                                currenxy =hold.currenxy,
                                price = hold.price.toString(),
                                category = hold.category,
                                country = hold.country,
                                location = hold.location,
                                desc = hold.desc,
                                dateAndtimme = hold.dateAndtimme,
                                type = hold.type,
                                negotiation = hold.negotiation,
                                image = hold.image,
                                image_two = hold.image_two,
                                image_three = hold.image_three,
                                image_four = hold.image_four,
                                image_five = hold.image_five,
                                image_six = hold.image_six,
                                paid_product = hold.paid_product,
                                pst_phone_number = hold.pst_phone_number.toString(),
                                subscription_type = hold.subscription_type,
                                subscription_date_start = hold.subscription_date_start,
                                subscription_date_due = hold.subscription_date_due,
                                views = hold.views,
                                approval_status = hold.approval_status,
                                rates = viewModel.Itemsratings.value
                            )
                            lifecycleOwner.lifecycleScope.launchWhenStarted {
                                viewModeltwo.insertIntoProductDatatbaseRecentlyViewedItems(
                                    products
                                )
                            }

                        } catch (e: Exception) { }

                    }

                })



                try{
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null).commit()
                }catch (e:Exception){}






            } catch (e: Exception) { }


            //Save into Recent view db



        }

        /* holder.itemView.findViewById<TextView>(R.id.pr_chat_owner).setOnClickListener {
            try{
                (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, ChatPageFragment()).commit()
            }catch(e:Exception){}
        }*/

        /*holder.itemView.findViewById<TextView>(R.id.pr_call_owner).setOnClickListener {
            holder.itemView.findViewById<TextView>(R.id.pr_call_owner).setText("0${hold.pst_phone_number}")
        }*/


        if (hold.subscription_type == "Silver 15 days") {
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.ads_txt).text = "Silver Ads"
            holder.itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.white))
        } else if (hold.subscription_type == "Gold 1 month") {
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.ads_txt).text = "Gold Ads"
            holder.itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.white))
        } else if (hold.subscription_type == "Bronx 7 days") {
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.ads_txt).text = "Bronx Ads"
            holder.itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.white))
        } else if (hold.subscription_type == "Standard") {
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.ads_txt).text = "Standard Ads"
            holder.itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.white))
        }

    }

    override fun getItemCount(): Int {
        return productDataList.size
    }

    fun byteArrayToBitmap(data: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    fun currencyFormat(amount: String): String? {
        val formatter = DecimalFormat("###,###,##0.00")
        return formatter.format(amount.toDouble())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun stringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte: ByteArray = Base64.decode(encodedString, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            bitmap
        } catch (e: java.lang.Exception) {
            e.message
            null
        }
    }

    fun resizePhoto(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val aspRat = w / h
        val W = 500
        val H = W * aspRat
        val b = Bitmap.createScaledBitmap(bitmap, W, H, false)

        return b
    }

    fun formatViewsCount(viewsCount: Long): String {
        val suffixes = arrayOf("K", "M", "B", "T")
        val formatter: NumberFormat = NumberFormat.getInstance(Locale.US)
        formatter.maximumFractionDigits = 1

        var value = viewsCount.toDouble()
        var suffix = ""
        for (i in suffixes.indices) {
            if (value < 1000.0) {
                break
            }
            value /= 1000.0
            suffix = suffixes[i]
        }
        return formatter.format(value) + suffix + " views"
    }

    fun loadByteArrayIntoImageView(imageByteArray: ByteArray, imageView: ImageView) {
        val quality = 40 // Set the desired compression quality here (0-100)
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        imageView.setImageBitmap(bitmap)
    }

    /* val ratings = arrayOf(4, 5, 3, 2, 5)
     val avgRating = calculateRating(ratings)
     println("Average rating: $avgRating")
     */

    fun calculateRating(ratings: Array<Int>): Double {
        val numRatings = ratings.size

        if (numRatings == 0) {
            return 0.0
        }

        val totalRating = ratings.sum()
        val avgRating = totalRating.toDouble() / numRatings.toDouble()

        return avgRating
    }

    fun calculateDoubleRating(intRating: Int): Double {
        // Perform any necessary calculations or conversions here
        val doubleRating = intRating.toDouble() / 10.0 // Assuming the integer rating is on a scale of 1 to 10

        return doubleRating
    }

    fun decodeGzipToByteArray(gzipData: ByteArray): ByteArray {
        val inputStream = ByteArrayInputStream(gzipData)
        val gzipInputStream = GZIPInputStream(inputStream)
        val outputStream = ByteArrayOutputStream()

        val buffer = ByteArray(1024)
        var len: Int
        while (gzipInputStream.read(buffer).also { len = it } > 0) {
            outputStream.write(buffer, 0, len)
        }

        gzipInputStream.close()
        outputStream.close()

        return outputStream.toByteArray()
    }

}