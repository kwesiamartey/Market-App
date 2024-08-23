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


class AllProductListAdapter(
    val context: Context,
    val productDataList: MutableList<PostProductResponse>,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: MyViewModel,
    val viewModeltwo: PullingDataFromServerViewModel
) : RecyclerView.Adapter<AllProductListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(productDataList: PostProductResponse, position: Int) {
            itemView.findViewById<TextView>(R.id.pr_title_txt).text = productDataList.title
            itemView.findViewById<TextView>(R.id.pr_price_currency_txt).text =
                "${productDataList.currenxy}"
            itemView.findViewById<TextView>(R.id.pr_price_txt).text = "${productDataList.price!!}"
            itemView.findViewById<TextView>(R.id.pr_pruct_description).text = productDataList.desc
            itemView.findViewById<TextView>(R.id.pr_type).text = productDataList.type
            itemView.findViewById<TextView>(R.id.store_name).text = productDataList.user_name
            itemView.findViewById<TextView>(R.id.pr_product_location).text =
                "from " + productDataList.location + " in " + productDataList.paid_product
            itemView.findViewById<TextView>(R.id.pr_views).text =
                formatViewsCount(productDataList.views!!.toLong())
            //itemView.findViewById<ImageView>(R.id.pr_imgg).setImageBitmap(productDataList.image!!)

            if (productDataList.rates == 0 || productDataList.rates == null) {
                itemView.findViewById<LinearLayout>(R.id.rating_wrapper).visibility = View.INVISIBLE
            } else {
                itemView.findViewById<LinearLayout>(R.id.rating_wrapper).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ratings_value).text =
                    calculateDoubleRating(productDataList.rates).toString()
            }

            if (productDataList.negotiation == "1") {
                itemView.findViewById<TextView>(R.id.negotiate_text).visibility = View.VISIBLE
            } else {
                itemView.findViewById<TextView>(R.id.negotiate_text).visibility = View.INVISIBLE
            }


            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Log.i("imgLocation", "lovicmarketplace.herokuapp.com" + productDataList.image!!)

            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)
                 // resize does not respect aspect ratio

            Glide.with(context)
                .load(url_for_uploading+productDataList.image!! + "?size=200")
                .transform(RoundedCorners(10))
                .apply(requestOptions)
                .placeholder(circularProgressDrawable)
                .dontAnimate()
                .into(itemView.findViewById(R.id.pr_imgg))

            itemView.findViewById<LinearLayout>(R.id.post_detail_wrapper).setOnClickListener {
                var count: Int = productDataList.views
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

                    viewModel.id.value = productDataList.id.toString()
                    viewModel.counts.value = count
                    viewModel.productId.value = productDataList.id.toString()
                    viewModel.postuserId.value = productDataList.userId
                    viewModel.user_name.value = productDataList.user_name
                    viewModel.title.value = productDataList.title
                    viewModel.currenxy.value = productDataList.currenxy.toString()
                    viewModel.price.value = productDataList.price.toString()
                    viewModel.category.value = productDataList.category
                    viewModel.location.value = productDataList.location
                    viewModel.country.value = productDataList.country
                    viewModel.negotiation.value = productDataList.negotiation
                    viewModel.paid_product.value = productDataList.paid_product
                    viewModel.desc.value = productDataList.desc
                    viewModel.dateAndtimme.value = productDataList.dateAndtimme
                    viewModel.type.value = productDataList.type
                    viewModel.image.value = productDataList.image
                    viewModel.image_two.value = productDataList.image_two
                    viewModel.image_three.value = productDataList.image_three
                    viewModel.image_four.value = productDataList.image_four
                    viewModel.image_five.value = productDataList.image_five
                    viewModel.image_six.value = productDataList.image_six
                    viewModel.subscription_type.value = productDataList.subscription_type
                    viewModel.subscription_date_due.value = productDataList.subscription_date_due
                    viewModel.approval_status.value = productDataList.approval_status
                    viewModel.phone_number.value = productDataList.pst_phone_number
                    viewModel.views.value = productDataList.views.toString()
                    viewModel.Itemsratings.value = productDataList.rates
                    viewModel.ratings.value = 0
                    viewModel.updateNumberViewsProductList(count, productDataList.id!!)
                    /*val intent = Intent(context, Single_Selected_item_show::class.java)
                    context.startActivity(intent)*/

                    viewModel.queryUserDetails().asLiveData().observe(lifecycleOwner, androidx.lifecycle.Observer {
                        if (it == null) {

                            //(activity as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, SignInFragment()).show()

                        } else {

                            try {
                                val products = RecentlyViewData(
                                    id = productDataList.id,
                                    userId = productDataList.userId,
                                    user_name = productDataList.user_name,
                                    title = productDataList.title,
                                    currenxy = productDataList.currenxy,
                                    price = productDataList.price.toString(),
                                    category = productDataList.category,
                                    country = productDataList.country,
                                    location = productDataList.location,
                                    desc = productDataList.desc,
                                    dateAndtimme = productDataList.dateAndtimme,
                                    type = productDataList.type,
                                    negotiation = productDataList.negotiation,
                                    image = productDataList.image,
                                    image_two = productDataList.image_two,
                                    image_three = productDataList.image_three,
                                    image_four = productDataList.image_four,
                                    image_five = productDataList.image_five,
                                    image_six = productDataList.image_six,
                                    paid_product = productDataList.paid_product,
                                    pst_phone_number = productDataList.pst_phone_number.toString(),
                                    subscription_type = productDataList.subscription_type,
                                    subscription_date_start = productDataList.subscription_date_start,
                                    subscription_date_due = productDataList.subscription_date_due,
                                    views = productDataList.views,
                                    approval_status = productDataList.approval_status,
                                    rates = viewModel.Itemsratings.value
                                )

                                lifecycleOwner.lifecycleScope.launchWhenStarted {
                                    viewModeltwo.insertIntoProductDatatbaseRecentlyViewedItems(
                                        products
                                    )
                                }

                            } catch (e: Exception) {
                            }

                        }

                    })


                    (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null).commit()

                } catch (e: Exception) { }

            }

            if (productDataList.subscription_type == "Silver 15 days") {
                itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ads_txt).text = "Silver Ads"
                itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
                itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.white))
            } else if (productDataList.subscription_type == "Gold 1 month") {
                itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ads_txt).text = "Gold Ads"
                itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
                itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.white))
            } else if (productDataList.subscription_type == "Bronx 7 days") {
                itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ads_txt).text = "Bronx Ads"
                itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
                itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.white))
            } else if (productDataList.subscription_type == "Standard") {
                itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ads_txt).text = "Standard Ads"
                itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
                itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.white))
            }

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
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size)
        imageView.setImageBitmap(bitmap)
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