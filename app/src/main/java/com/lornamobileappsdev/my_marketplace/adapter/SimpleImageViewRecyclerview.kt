package com.lornamobileappsdev.my_marketplace.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterInside
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.url_for_uploading
import com.lornamobileappsdev.my_marketplace.models.SinglePreviewModel
import com.lornamobileappsdev.my_marketplace.ui.SinglePreviewFragment
import com.lornamobileappsdev.my_marketplace.viewModels.PullingDataFromServerViewModel
import java.lang.Exception

class SimpleImageViewRecyclerview(
    val context: Context,
    val images: MutableList<SinglePreviewModel>,
    val lifecycleOwner: LifecycleOwner,
    val viewModelClass: PullingDataFromServerViewModel
) : RecyclerView.Adapter<SimpleImageViewRecyclerview.MyViewHolder>() {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(b: SinglePreviewModel, position: Int) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()

            try {
                Log.i("Datz", url_for_uploading+b.image)
                val image = itemView.findViewById<ImageView>(R.id.image_0)
                Glide.with(context)
                    .load(url_for_uploading+b.image)
                    .transform(CenterInside(), RoundedCorners(12))
                    .placeholder(circularProgressDrawable)
                    .dontAnimate()
                    .into(image)

                itemView.findViewById<ImageView>(R.id.image_0).setOnClickListener {
                    viewModelClass.post_product_id.value = b.image
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, SinglePreviewFragment()).addToBackStack(null)
                        .commit()
                }



                Log.i("Datz", url_for_uploading+b.image_two)
                val image1 = itemView.findViewById<ImageView>(R.id.image_1)
                Glide.with(context)
                    .load(url_for_uploading+b.image_two)
                    .transform(CenterInside(), RoundedCorners(12))
                    .placeholder(circularProgressDrawable)
                    .dontAnimate()
                    .into(image1)

                itemView.findViewById<ImageView>(R.id.image_1).setOnClickListener {
                    viewModelClass.post_product_id.value = b.image_two
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, SinglePreviewFragment()).addToBackStack(null).commit()
                }


                Log.i("Datz", url_for_uploading+b.image_three)
                val image2 = itemView.findViewById<ImageView>(R.id.image_2)
                Glide.with(context)
                    .load(url_for_uploading+b.image_three)
                    .transform(CenterInside(), RoundedCorners(12))
                    .placeholder(circularProgressDrawable)
                    .dontAnimate()
                    .into(image2)

                itemView.findViewById<ImageView>(R.id.image_2).setOnClickListener {
                    viewModelClass.post_product_id.value = b.image_three
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, SinglePreviewFragment()).addToBackStack(null).commit()
                }

                Log.i("Datz", url_for_uploading+b.image_four)
                val image3 = itemView.findViewById<ImageView>(R.id.image_3)
                Glide.with(context)
                    .load(url_for_uploading+b.image_four)
                    .transform(CenterInside(), RoundedCorners(12))
                    .placeholder(circularProgressDrawable)
                    .dontAnimate()
                    .into(image3)

                itemView.findViewById<ImageView>(R.id.image_3).setOnClickListener {
                    viewModelClass.post_product_id.value = b.image_four
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, SinglePreviewFragment()).addToBackStack(null).commit()
                }


                Log.i("Datz", url_for_uploading+b.image_five)
                val image4 = itemView.findViewById<ImageView>(R.id.image_4)
                Glide.with(context)
                    .load(url_for_uploading+b.image_five)
                    .transform(CenterInside(), RoundedCorners(12))
                    .placeholder(circularProgressDrawable)
                    .dontAnimate()
                    .into(image4)

                itemView.findViewById<ImageView>(R.id.image_4).setOnClickListener {
                    viewModelClass.post_product_id.value = b.image_five
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, SinglePreviewFragment()).addToBackStack(null).commit()
                }


                Log.i("Datz", url_for_uploading+b.image_six)
                val image5 = itemView.findViewById<ImageView>(R.id.image_5)
                Glide.with(context)
                    .load(url_for_uploading+b.image_six)
                    .transform(CenterInside(), RoundedCorners(12))
                    .placeholder(circularProgressDrawable)
                    .dontAnimate()
                    .into(image5)

                itemView.findViewById<ImageView>(R.id.image_5).setOnClickListener {
                    viewModelClass.post_product_id.value = b.image_six
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(android.R.id.content, SinglePreviewFragment()).addToBackStack(null).commit()
                }



            }catch (e:Exception){}

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_simple_imageview_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = images[position]
        holder.setData(hold, position)
    }

    override fun getItemCount(): Int {
        return images.size
    }

}