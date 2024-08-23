package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.models.CategoryList
import com.lornamobileappsdev.my_marketplace.ui.CategoryFragment
import com.lornamobileappsdev.my_marketplace.viewModels.MyViewModel


class CategoryListAdapter(
    val context: Context,
    val catelist: MutableList<CategoryList>,
    val lifecycleOwner: LifecycleOwner,
    val viewModel: MyViewModel
) : RecyclerView.Adapter<CategoryListAdapter.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun setData(cateLists: CategoryList, position: Int) {
            itemView.findViewById<TextView>(R.id.txttd_name).text = cateLists.name


            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            //loadByteArrayIntoImageView(productDataList.image!!, itemView.findViewById<ImageView?>(R.id.pr_imgg))
            val requestOptions = RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
            // resize does not respect aspect ratio

            Glide.with(context)
                .load("${cateLists.image!!}")
                .transform( RoundedCorners(10))
                .apply(requestOptions)
                .placeholder(circularProgressDrawable)
                .dontAnimate()
                .into(itemView.findViewById(R.id.img_image))



            itemView.findViewById<LinearLayout>(R.id.cate_detail_wrapper).setOnClickListener {

                try {
                    //store data in the view model
                    viewModel.category.value = cateLists.name.toString()

                    (context as AppCompatActivity).supportFragmentManager.beginTransaction().replace(android.R.id.content, CategoryFragment()).addToBackStack(null).commit()

                } catch (e: Exception) { }
            }



        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_category_lbutton_ist_item, parent, false)
        return MyViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = catelist[position]
        holder.setData(hold, position)
    }

    override fun getItemCount(): Int {
        return catelist.size
    }

}
