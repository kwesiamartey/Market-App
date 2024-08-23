package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
import android.os.Build
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

class ProductListAdapter(
    val context:Context,
    val productDataList: MutableList<PostProductResponse>,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: MyViewModel,
    val viewModeltwo: PullingDataFromServerViewModel
) : RecyclerView.Adapter<ProductListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(productDataList: PostProductResponse, position: Int){
            itemView.findViewById<TextView>(R.id.pr_title_txt).text = productDataList.title
            itemView.findViewById<TextView>(R.id.pr_price_currency_txt).text = "${productDataList.currenxy}"
            itemView.findViewById<TextView>(R.id.pr_price_txt).text = productDataList.price.toString()
            itemView.findViewById<TextView>(R.id.pr_pruct_description).text = productDataList.desc
            itemView.findViewById<TextView>(R.id.pr_product_location).text = "from "+productDataList.location +" in "+productDataList.paid_product
            itemView.findViewById<TextView>(R.id.pr_type).text = productDataList.type
            itemView.findViewById<TextView>(R.id.pr_views).text = formatViewsCount(productDataList.views!!.toLong())
            //Toast.makeText(context, productDataList.subscription_type.toString(), Toast.LENGTH_LONG).show()
            //itemView.findViewById<ImageView>(R.id.pr_imgg).setImageBitmap(byteArrayToBitmap(productDataList.image!!))

            if(productDataList.rates == 0 || productDataList.rates == null){
                itemView.findViewById<LinearLayout>(R.id.rating_wrapper).visibility = View.GONE
            }else{
                itemView.findViewById<LinearLayout>(R.id.rating_wrapper).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ratings_value).text = calculateDoubleRating(productDataList.rates).toString()
            }

            if(productDataList.negotiation == "1"){
                itemView.findViewById<TextView>(R.id.pr_negotiate).visibility = View.VISIBLE
            }else{
                itemView.findViewById<TextView>(R.id.pr_negotiate).visibility = View.GONE
            }

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(context)
                .load(url_for_uploading+productDataList.image!!)
                .transform(CenterInside(), RoundedCorners(10))
                .apply(requestOptions)
                .placeholder(circularProgressDrawable)
                .dontAnimate()
                .into(itemView.findViewById(R.id.pr_imgg))





            itemView.findViewById<LinearLayout>(R.id.post_detail_wrapper).setOnClickListener {

                //store data in the view model
                viewModel.id.value = productDataList.id.toString()
                viewModel.productId.postValue(productDataList.id.toString())
                viewModel.postuserId.postValue(productDataList.userId)
                viewModel.user_name.postValue(productDataList.user_name)
                viewModel.title.postValue(productDataList.title)
                viewModel.price.postValue(productDataList.price.toString())
                viewModel.currenxy.value = productDataList.currenxy.toString()
                viewModel.category.postValue(productDataList.category)
                viewModel.location.postValue(productDataList.location)
                viewModel.country.postValue(productDataList.country)
                viewModel.negotiation.postValue(productDataList.negotiation)
                viewModel.paid_product.postValue(productDataList.paid_product)
                viewModel.desc.postValue(productDataList.desc)
                viewModel.dateAndtimme.postValue(productDataList.dateAndtimme)
                viewModel.type.postValue(productDataList.type)
                viewModel.image.postValue(productDataList.image)
                viewModel.image_two.postValue(productDataList.image_two)
                viewModel.image_three.postValue(productDataList.image_three)
                viewModel.image_four.postValue(productDataList.image_four)
                viewModel.image_five.postValue(productDataList.image_five)
                viewModel.image_six.postValue(productDataList.image_six)
                viewModel.subscription_type.postValue(productDataList.subscription_type)
                viewModel.subscription_date_due.postValue(productDataList.subscription_date_due)
                viewModel.approval_status.postValue(productDataList.approval_status)
                viewModel.pst_phone_number.postValue(productDataList.pst_phone_number.toString())
                viewModel.views.postValue(productDataList.views.toString())
                viewModel.Itemsratings.value = productDataList.rates
                viewModel.ratings.value = 0

                try{
                    try{
                        var count:Int = productDataList.views
                        count+1
                        viewModel.updateNumberViewsProductList(count, productDataList.id!!)
                        (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null).commit()
                    }catch(e:Exception){}

                }catch(e:Exception){}


                //Save into Recent view db
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

            }

            if (productDataList.subscription_type == "Silver 15 days") {
                itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ads_txt).text = "Silver Ads"
                itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
                itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.white))
            } else if (productDataList.subscription_type == "Gold 1 month" ) {
                itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ads_txt).text = "Gold Ads"
                itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
                itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.white))
            } else if(productDataList.subscription_type == "Bronx 7 days" ) {
                itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.VISIBLE
                itemView.findViewById<TextView>(R.id.ads_txt).text = "Bronx Ads"
                itemView.findViewById<TextView>(R.id.ads_txt).setTextColor(context.resources.getColor(R.color.deep_grey))
                itemView.findViewById<LinearLayout>(R.id.ads_image).backgroundTintList =
                    ColorStateList.valueOf(context.resources.getColor(R.color.white))
            } else if(productDataList.subscription_type == "Standard" ) {
                itemView.findViewById<LinearLayout>(R.id.ads_image).visibility = View.GONE
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

    fun resizePhoto(bitmap: Bitmap): Bitmap {
        val w = bitmap.width
        val h = bitmap.height
        val aspRat = w / h
        val W = 500
        val H = W * aspRat
        val b = Bitmap.createScaledBitmap(bitmap, W, H, false)

        return b
    }

    fun Bitmap.toByteArray(quality: Int = 50): ByteArray {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, quality, stream)
        return stream.toByteArray()
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
        val radii = floatArrayOf(cornerRadius, cornerRadius, cornerRadius, cornerRadius,
            cornerRadius, cornerRadius, cornerRadius, cornerRadius)
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