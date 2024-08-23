package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
import android.os.Build
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.room.TypeConverter
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.entity.RelatedSearchData
import com.lornamobileappsdev.my_marketplace.ui.Single_Selected_item_show
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.text.DecimalFormat
import java.util.*
import java.util.zip.GZIPInputStream


class RelatedSearchListAdapter(
    val context: Context,
    val productDataList: MutableList<RelatedSearchData>,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: MyViewModel
) : RecyclerView.Adapter<RelatedSearchListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(productDataList: RelatedSearchData, position: Int) {
            itemView.findViewById<TextView>(R.id.txt_related_name).text = productDataList.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.activity_related_search_list_item, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = productDataList[position]
        holder.setData(hold, position)

        holder.itemView.findViewById<TextView>(R.id.txt_related_delete).setOnClickListener {

            lifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.deleteRelatedSeaarchItem(hold.id!!)
            }
        }
        holder.itemView.findViewById<TextView>(R.id.txt_related_name).setOnClickListener {
            viewModel.productId.value = hold.postid.toString()
            viewModel.category.value = hold.cate
            viewModel.postuserId.value = hold.postUserId
            viewModel.user_name.value = hold.full_name
            viewModel.Itemsratings.value = hold.rates
            viewModel.ratings.value = 0

            (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, Single_Selected_item_show()).addToBackStack(null).commit()
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

    fun loadByteArrayIntoImageView(byteArray: ByteArray, imageView: ImageView) {
        val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        val roundedBitmap = getRoundedCornerBitmap(bitmap, 40f) // Specify the desired corner radius here

        imageView.setImageBitmap(roundedBitmap)
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