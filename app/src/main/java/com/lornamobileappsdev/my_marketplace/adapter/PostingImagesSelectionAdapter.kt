package com.lornamobileappsdev.my_marketplace.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lornamobileappsdev.my_marketplace.R
import com.lornamobileappsdev.my_marketplace.constant.url_for_uploading
import com.lornamobileappsdev.my_marketplace.models.SelectGalleryImages
import com.lornamobileappsdev.my_marketplace.ui.Single_Selected_item_show
import kotlinx.coroutines.delay

class PostingImagesSelectionAdapter(val context:Context, val featuredList: MutableList<SelectGalleryImages>, val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<PostingImagesSelectionAdapter.MyViewHolder>(){

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun setData(featuredList: SelectGalleryImages, position: Int){
            val imageView = itemView.findViewById<ImageView>(R.id.pst_gallery_images)


            Glide.with(context)
                .load(url_for_uploading+featuredList.images)
                .placeholder(R.drawable.placeholder).fitCenter()
                .dontAnimate()
                .into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_posting_images_list_item_product, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val hold = featuredList[position]
        holder.setData(hold, position)
        holder.itemView.findViewById<ConstraintLayout>(R.id.container).setOnClickListener {
            val dialogue = Dialog(context)
            dialogue.setContentView(R.layout.activity_progress_loader)
            dialogue.setCancelable(false)
            lifecycleOwner.lifecycleScope.launchWhenStarted {

                for (i in 0..2){
                    for (o in 1.. i+1){
                        delay(800L)
                        dialogue.findViewById<TextView>(R.id.txt_loading).append("*")
                    }
                    dialogue.findViewById<TextView>(R.id.txt_loading).append("")
                }
                dialogue.cancel()
                try{
                    val intent = Intent(context, Single_Selected_item_show::class.java)
                    context.startActivity(intent)
                }catch(e:Exception){}
            }
            dialogue.show()
        }
    }

    override fun getItemCount(): Int {
        return featuredList.size
    }

}