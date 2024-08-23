package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
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
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.url_for_uploading
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


class InnerRecentlyViewedAdapter(
    val context: Context,
    val productDataList: MutableList<RecentlyViewData>,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: MyViewModel,
    val viewModelTwo:PullingDataFromServerViewModel
) : RecyclerView.Adapter<InnerRecentlyViewedAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(productDataList: RecentlyViewData, position: Int) {
            itemView.findViewById<TextView>(R.id.pr_title_txt).text = productDataList.title
            itemView.findViewById<TextView>(R.id.pr_price_currency_txt).text = "${productDataList.currenxy}"
            itemView.findViewById<TextView>(R.id.pr_price_txt).text = productDataList.price.toString()
            itemView.findViewById<TextView>(R.id.pr_pruct_description).text = productDataList.desc
            itemView.findViewById<TextView>(R.id.pr_product_location).text =
                "from " + productDataList.location + " in " + productDataList.paid_product
            itemView.findViewById<TextView>(R.id.pr_type).text = productDataList.type
            itemView.findViewById<TextView>(R.id.pr_views).text =
                formatViewsCount(productDataList.views!!.toLong())
            //itemView.findViewById<ImageView>(R.id.pr_imgg).setImageBitmap(productDataList.image!!)
            if (productDataList.negotiation == "1") {
               // itemView.findViewById<TextView>(R.id.pr_negotiate).visibility = View.VISIBLE
            } else {
                //itemView.findViewById<TextView>(R.id.pr_negotiate).visibility = View.GONE
            }

           // loadByteArrayIntoImageView(productDataList.image!!, itemView.findViewById(R.id.pr_imgg))

            if(productDataList.rates == 0 || productDataList.rates == null){
                itemView.findViewById<LinearLayout>(R.id.rating_wrapper).visibility = View.GONE
            }else{
                itemView.findViewById<LinearLayout>(R.id.rating_wrapper).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ratings_value).text = calculateDoubleRating(productDataList.rates).toString()
            }


            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            Log.i("imgLocation", "lovicmarketplace.herokuapp.com" + productDataList.image!!)

            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(context)
                .load(url_for_uploading +productDataList.image!!)
                .transform(CenterCrop (), RoundedCorners(10))
                .apply(requestOptions)
                .placeholder(circularProgressDrawable)
                .dontAnimate()
                .into(itemView.findViewById(R.id.pr_imgg))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.recently_viewed_list_item, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = productDataList[position]
        holder.setData(hold, position)

        holder.itemView.findViewById<LinearLayout>(R.id.recent_detail_wrapper).setOnClickListener {

            try {
                var count: Int = hold.views!!
                count + 1
                //store data in the view model
                viewModel.id.value = hold.id.toString()
                viewModel.counts.value = count
                viewModel.productId.value = hold.id.toString()
                viewModel.postuserId.value = hold.userId
                viewModel.user_name.value = hold.user_name
                viewModel.title.value = hold.title
                viewModel.currenxy.value = hold.currenxy.toString()
                viewModel.price.value = hold.price.toString()
                viewModel.category.value = hold.category
                viewModel.categoryName.value = hold.category
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
                viewModel.updateNumberViewsProductList(count, hold.id!!)
                viewModel.Itemsratings.value = hold.rates
                //viewModel.ratings.value = 0

                Log.i("innerData", viewModel.Itemsratings.value.toString())

                (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                    .replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null)
                    .commit()

                //Toast.makeText(context, "${hold.id}", Toast.LENGTH_LONG).show()

            } catch (e: Exception) {
            }

        }


        /* holder.itemView.findViewById<TextView>(R.id.pr_chat_owner).setOnClickListener {
            try{
                (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, ChatPageFragment()).commit()
            }catch(e:Exception){}
        }*/

        /*   holder.itemView.findViewById<TextView>(R.id.pr_call_owner).setOnClickListener {
               holder.itemView.findViewById<TextView>(R.id.pr_call_owner).setText("0${hold.pst_phone_number}")
           }*/


        if (hold.subscription_type == "Silver 15 days") {
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.ads_txt).text = "Silver Ads"
            holder.itemView.findViewById<TextView>(R.id.ads_txt)
                .setTextColor(context.resources.getColor(R.color.deep_grey))
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.white))
        } else if (hold.subscription_type == "Gold 1 month") {
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.ads_txt).text = "Gold Ads"
            holder.itemView.findViewById<TextView>(R.id.ads_txt)
                .setTextColor(context.resources.getColor(R.color.deep_grey))
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                ColorStateList.valueOf(context.resources.getColor(R.color.white))
        } else if (hold.subscription_type == "Bronx 7 days") {
            holder.itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
            holder.itemView.findViewById<TextView>(R.id.ads_txt).text = "Bronx Ads"
            holder.itemView.findViewById<TextView>(R.id.ads_txt)
                .setTextColor(context.resources.getColor(R.color.deep_grey))
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

    fun loadByteArrayIntoImageView(byteArray: ByteArray, imageView: ImageView) {
        val quality = 40 // Set the desired compression quality here (0-100)
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
        imageView.setImageBitmap(bitmap)
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, cornerRadius: Float): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val path = Path()
        val rect = android.graphics.Rect(0, 0, bitmap.width, bitmap.height)
        val radii = floatArrayOf(
            cornerRadius, cornerRadius, cornerRadius, cornerRadius,
            cornerRadius, cornerRadius, cornerRadius, cornerRadius
        )
        path.addRoundRect(android.graphics.RectF(rect), radii, Path.Direction.CW)

        canvas.clipPath(path)
        canvas.drawBitmap(bitmap, rect, rect, null)

        return output
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